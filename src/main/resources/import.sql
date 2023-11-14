CREATE TABLE measurement(
                timestamp TIMESTAMPTZ NOT NULL,
                device_id varchar(255),
                current_l1a float,
                current_l2a float,
                current_l3a float,
                voltage_l1v float,
                voltage_l2v float,
                voltage_l3v float,
                instantaneous_active_power_plus_w float,
                instantaneous_active_power_minus_w float,
                total_energy_consumed_wh float,
                total_energy_delivered_wh float,
                PRIMARY KEY (timestamp, device_id)
                );

CREATE UNIQUE INDEX idx_timestamp_device_id ON measurement(device_id, timestamp);

SELECT create_hypertable('measurement', 'timestamp',  partitioning_column => 'device_id', number_partitions => 10);
