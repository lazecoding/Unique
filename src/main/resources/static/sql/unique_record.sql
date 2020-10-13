DROP TABLE IF EXISTS `unique_record`;

CREATE TABLE `unique_record` (
  `bus_tag` varchar(128)  NOT NULL DEFAULT '',
  `max_id` bigint(20) NOT NULL DEFAULT '1',
  `step` int(11) NOT NULL,
  `description` varchar(256)  DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`bus_tag`)
) ENGINE=InnoDB;

INSERT INTO unique_record(bus_tag, max_id, step, description) VALUES ('unique-record-segment-test', 1, 2000, 'Test Unique Record Segment Mode Get Id');