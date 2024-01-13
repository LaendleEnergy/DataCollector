DROP TABLE IF EXISTS public.measurement CASCADE;
DROP TABLE IF EXISTS public.tag;
DROP TABLE IF EXISTS public.devicecategory;
DROP TABLE IF EXISTS public.measurement_w_t CASCADE;
DROP TABLE IF EXISTS public.averagepriceperkwh CASCADE;
DROP VIEW IF EXISTS public.measurement_w_t CASCADE;
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
CREATE UNIQUE INDEX idx_timestamp_device_id ON public.measurement USING btree (device_id, reading_time);
CREATE INDEX measurement_reading_time_idx ON public.measurement USING btree (reading_time DESC);
SELECT create_hypertable('measurement', 'reading_time',  partitioning_column => 'device_id', number_partitions => 10);

CREATE TABLE public.devicecategory (
                                       category_name varchar(255) NOT NULL,
                                       description varchar(255) NULL,
                                       CONSTRAINT devicecategory_pkey PRIMARY KEY (category_name)
);

CREATE TABLE public.tag (
                            measurement_reading_time timestamp NOT NULL,
                            devicecategory_category_name varchar(255) NOT NULL,
                            measurement_device_id varchar(255) NOT NULL,
                            "name" varchar(255) NOT NULL,
                            CONSTRAINT tag_pkey PRIMARY KEY (measurement_reading_time, devicecategory_category_name, measurement_device_id, name),
                            CONSTRAINT fk_devicecategory FOREIGN KEY (devicecategory_category_name) REFERENCES public.devicecategory(category_name)
);


CREATE TABLE public.averagepriceperwh (
    device_id varchar(255) NOT NULL,
    start_date timestamp NOT NULL,
    average_price_Wh float4 NOT NULL,
    PRIMARY KEY (device_id, start_date)
);

CREATE TYPE TAG_RECORD as (devicecategory_category_name text, name text);

CREATE OR REPLACE VIEW measurement_w_t AS
(
SELECT m.device_id, m.reading_time,
       m.current_l1a, m.current_l2a, m.current_l3a,
       m.voltage_l1v, m.voltage_l2v, m.voltage_l3v,
       m.instantaneous_active_power_plus_w,
       m.instantaneous_active_power_minus_w,
       m.total_energy_consumed_wh, m.total_energy_delivered_wh,
       NULLIF(ARRAY_AGG((t.devicecategory_category_name, t.name)::TAG_RECORD), array['(,)'::tag_record]) as tags
FROM measurement m
         LEFT OUTER JOIN TAG t
                         ON m.device_id = t.measurement_device_id
                             AND m.reading_time = t.measurement_reading_time
GROUP BY m.device_id, m.reading_time
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
            FOREACH t in ARRAY NEW.tags
                LOOP
                    INSERT INTO tag (measurement_device_id, measurement_reading_time, devicecategory_category_name, name)
                    VALUES(NEW.device_id, NEW.reading_time, t.devicecategory_category_name, t.name);
                END LOOP;
        END IF;
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
                INSERT INTO tag (measurement_device_id, measurement_reading_time, devicecategory_category_name, name)
                VALUES(NEW.device_id, NEW.reading_time, t.devicecategory_category_name, t.name)
                ON CONFLICT (measurement_device_id, measurement_reading_time, devicecategory_category_name, name)
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

CREATE TRIGGER update_measurement_trigger
    INSTEAD OF UPDATE
    ON measurement_w_t
    FOR EACH ROW
EXECUTE FUNCTION update_measurement_trigger_fkt();