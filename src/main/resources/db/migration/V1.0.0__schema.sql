DROP TABLE IF EXISTS public.measurement CASCADE;
DROP TABLE IF EXISTS public.tag;
DROP TABLE IF EXISTS public.devicecategory;
DROP TABLE IF EXISTS public.measurement_w_t CASCADE;
DROP TABLE IF EXISTS public.averagepriceperkwh CASCADE;
DROP VIEW IF EXISTS public.measurement_w_t CASCADE;
DROP VIEW IF EXISTS public.tag_view CASCADE;
DROP TYPE IF EXISTS TAG_RECORD;

CREATE TABLE public.measurement (
                                    current_l1a float4 NULL,
                                    current_l2a float4 NULL,
                                    current_l3a float4 NULL,
                                    instantaneous_active_power_minus_w float4 NULL,
                                    instantaneous_active_power_plus_w float4 NULL,
                                    total_energy_consumed_wh float4 NULL,
                                    total_energy_delivered_wh float4 NULL,
                                    voltage_l1v float4 NULL,
                                    voltage_l2v float4 NULL,
                                    voltage_l3v float4 NULL,
                                    reading_time timestamp NOT NULL,
                                    device_id varchar(255) NOT NULL,
                                    CONSTRAINT measurement_pkey PRIMARY KEY (reading_time, device_id)
);


CREATE EXTENSION btree_gist;


CREATE UNIQUE INDEX idx_timestamp_device_id ON public.measurement USING btree (device_id, reading_time);
CREATE INDEX measurement_reading_time_idx ON public.measurement USING btree (reading_time DESC);
SELECT create_hypertable('measurement', 'reading_time',  partitioning_column => 'device_id', number_partitions => 10);

CREATE TABLE public.devicecategory (
                                       category_name varchar(255) NOT NULL,
                                       CONSTRAINT devicecategory_pkey PRIMARY KEY (category_name)
);

CREATE TABLE public.device(
        device_name text NOT NULL,
        category_name text NOT NULL,
        device_id text NOT NULL,
        CONSTRAINT fk_devicecategory FOREIGN KEY (category_name) REFERENCES public.devicecategory(category_name),
        CONSTRAINT device_pkey PRIMARY KEY (device_name, device_id)
);

CREATE TYPE tsrange AS RANGE (
    subtype = timestamp
                             );

CREATE TABLE public.tag (
                            measurement_reading_timerange tsrange,
                            devicecategory_category_name varchar(255) NOT NULL,
                            measurement_device_id text NOT NULL,
                            device_name text NOT NULL,
                            CONSTRAINT tag_pkey PRIMARY KEY (measurement_reading_timerange, devicecategory_category_name, measurement_device_id, device_name),
                            CONSTRAINT tag_exclusion_constraint EXCLUDE USING gist (
                                measurement_reading_timerange WITH &&,
                                devicecategory_category_name WITH =,
                                measurement_device_id WITH =,
                                device_name WITH =
                                ),
                            CONSTRAINT fk_devicecategory FOREIGN KEY (devicecategory_category_name) REFERENCES public.devicecategory(category_name),
                            CONSTRAINT fk_device FOREIGN KEY (device_name, measurement_device_id) REFERENCES public.device(device_name, device_id)
);


CREATE TABLE public.averagepriceperwh (
    device_id varchar(255) NOT NULL,
    start_date timestamp NOT NULL,
    average_price_Wh float4 NOT NULL,
    PRIMARY KEY (device_id, start_date)
);

CREATE TYPE TAG_RECORD as (devicecategory_category_name text, device_name text, measurement_reading_timerange tsrange);

CREATE OR REPLACE VIEW measurement_w_t AS
(
SELECT m.device_id, m.reading_time,
       m.current_l1a, m.current_l2a, m.current_l3a,
       m.voltage_l1v, m.voltage_l2v, m.voltage_l3v,
       m.instantaneous_active_power_plus_w,
       m.instantaneous_active_power_minus_w,
       m.total_energy_consumed_wh, m.total_energy_delivered_wh,
       NULLIF(ARRAY_AGG((t.devicecategory_category_name, t.device_name, t.measurement_reading_timerange)::TAG_RECORD), array['(,,)'::tag_record]) as tags
FROM measurement m
         LEFT OUTER JOIN TAG t
                         ON m.device_id = t.measurement_device_id
                             AND t.measurement_reading_timerange @> m.reading_time
GROUP BY m.device_id, m.reading_time
    );

CREATE OR REPLACE VIEW tag_view AS
(
SELECT LOWER(measurement_reading_timerange) as time_start, UPPER(measurement_reading_timerange) as time_end,
       measurement_device_id, device_name, devicecategory_category_name
FROM tag
    );




CREATE OR REPLACE FUNCTION insert_measurement_trigger_fkt()
    RETURNS TRIGGER AS
'
    DECLARE
        t TAG_RECORD;
    BEGIN
        INSERT INTO measurement(reading_time, device_id, current_l1a, current_l2a, current_l3a,
                                voltage_l1v, voltage_l2v, voltage_l3v, instantaneous_active_power_plus_w,
                                instantaneous_active_power_minus_w, total_energy_consumed_wh, total_energy_delivered_wh)
        SELECT NEW.reading_time, NEW.device_id, NEW.current_l1a, NEW.current_l2a, NEW.current_l3a,
               NEW.voltage_l1v, NEW.voltage_l2v, NEW.voltage_l3v, NEW.instantaneous_active_power_plus_w,
               NEW.instantaneous_active_power_minus_w, NEW.total_energy_consumed_wh, NEW.total_energy_delivered_wh;
        IF NEW.tags IS NOT NULL THEN
            FOR t IN SELECT * FROM unnest(NEW.tags) AS t
                LOOP
                    INSERT INTO tag (measurement_device_id, measurement_reading_timerange, devicecategory_category_name, device_name, measurement_reading_timerange)
                    VALUES (NEW.device_id, NEW.reading_time, t.devicecategory_category_name, t.device_name, t.measurement_reading_timerange)
                    ON CONFLICT (measurement_device_id, measurement_reading_timerange, devicecategory_category_name, device_name, measurement_reading_timerange)
                        DO NOTHING;
                END LOOP;
        END IF;
        RETURN NEW;
    END;
'
    LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION insert_tag_trigger_fkt()
    RETURNS TRIGGER AS
'
    BEGIN
        INSERT INTO tag(measurement_reading_timerange, devicecategory_category_name, measurement_device_id, device_name)
        SELECT tsrange(NEW.time_start, NEW.time_end), NEW.devicecategory_category_name, NEW.measurement_device_id, NEW.device_name;
        RETURN NEW;
    EXCEPTION
        WHEN exclusion_violation THEN
            WITH deleted_rows AS (DELETE FROM tag
            WHERE tsrange(NEW.time_start, NEW.time_end) && tag.measurement_reading_timerange
                    AND measurement_device_id = NEW.measurement_device_id
                    AND device_name = NEW.device_name
            RETURNING *),
            insert_timespan AS (
                SELECT CASE WHEN MIN(LOWER(measurement_reading_timerange)) < NEW.time_start
                                THEN MIN(LOWER(measurement_reading_timerange)) ELSE NEW.time_start END AS time_start,
                       CASE WHEN MAX(UPPER(measurement_reading_timerange)) > NEW.time_end
                                THEN MAX(UPPER(measurement_reading_timerange)) ELSE NEW.time_end END AS time_end,
                      measurement_device_id, device_name, devicecategory_category_name
                FROM deleted_rows
                GROUP BY measurement_device_id, device_name, devicecategory_category_name
            )
            INSERT INTO tag(measurement_reading_timerange, devicecategory_category_name, measurement_device_id, device_name)
            SELECT tsrange(time_start, time_end), devicecategory_category_name, measurement_device_id, device_name
            FROM insert_timespan
            GROUP BY measurement_device_id, device_name, devicecategory_category_name, time_start, time_end;
            RETURN NEW;
    END;
'
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_measurement_trigger_fkt()
    RETURNS TRIGGER AS
'
    DECLARE
        t TAG_RECORD;
    BEGIN
        UPDATE measurement
        SET
            reading_time = NEW.reading_time,
            device_id = NEW.device_id,
            current_l1a = NEW.current_l1a,
            current_l2a = NEW.current_l2a,
            current_l3a = NEW.current_l3a,
            voltage_l1v = NEW.voltage_l1v,
            voltage_l2v = NEW.voltage_l2v,
            voltage_l3v = NEW.voltage_l3v,
            instantaneous_active_power_plus_w = NEW.instantaneous_active_power_plus_w,
            instantaneous_active_power_minus_w = NEW.instantaneous_active_power_minus_w,
            total_energy_consumed_wh = NEW.total_energy_consumed_wh,
            total_energy_delivered_wh = NEW.total_energy_delivered_wh
        WHERE reading_time = NEW.reading_time AND device_id = NEW.device_id;
        FOREACH t in ARRAY NEW.tags
            LOOP
                INSERT INTO tag (measurement_device_id,  devicecategory_category_name, device_name, measurement_reading_timerange)
                VALUES(NEW.device_id, t.devicecategory_category_name, t.device_name, t.measurement_reading_timerange)
                ON CONFLICT (measurement_device_id, devicecategory_category_name, device_name, measurement_reading_timerange)
                    DO NOTHING;
            END LOOP;
        RETURN NEW;
    END;
'
    LANGUAGE plpgsql;


CREATE TRIGGER insert_measurement_trigger
    INSTEAD OF INSERT
    ON measurement_w_t
    FOR EACH ROW
EXECUTE FUNCTION insert_measurement_trigger_fkt();

CREATE TRIGGER insert_measurement_trigger
    INSTEAD OF INSERT
    ON tag_view
    FOR EACH ROW
EXECUTE FUNCTION insert_tag_trigger_fkt();

CREATE TRIGGER update_measurement_trigger
    INSTEAD OF UPDATE
    ON measurement_w_t
    FOR EACH ROW
EXECUTE FUNCTION update_measurement_trigger_fkt();