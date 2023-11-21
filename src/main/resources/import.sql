SELECT create_hypertable('measurement', 'timestamp',  partitioning_column => 'device_id', number_partitions => 10);

INSERT INTO public.devicecategory (category_name, description) VALUES ('washing_machine', 'felix');
INSERT INTO public.measurement (current_l1a, current_l1v, current_l2a, current_l2v, current_l3a, current_l3v, instantaneous_active_power_minus_w, instantaneous_active_power_plus_w, total_energy_consumed_wh, total_energy_delivered_wh, timestamp, device_id) VALUES (1, 0, 0, 1, 1, 1, 1, 1, 1, 1, '2023-11-19 11:59:17.000000', '1');