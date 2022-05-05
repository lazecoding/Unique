DROP TABLE IF EXISTS `unique_record`;

CREATE TABLE `unique_record` (
  `bus_tag` varchar(128)  NOT NULL DEFAULT '',
 `max_id` bigint(20) NOT NULL DEFAULT '1',
  `step` int(11) NOT NULL,
  `description` varchar(256)  DEFAULT NULL,
  `namespace_id` varchar(36) NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`bus_tag`)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS `namespace`;

CREATE TABLE `namespace` (
  `namespace_id` varchar(36)  NOT NULL DEFAULT '',
  `type` smallint (5) NOT NULL,
  `description` varchar(256)  DEFAULT NULL,
  PRIMARY KEY (`namespace_id`)
) ENGINE=InnoDB;

INSERT INTO unique_record(bus_tag, max_id, step, description,namespace_id) VALUES ('unique-record-segment-test', 1, 20000, 'Test Unique Record Segment Mode Get Id','7580ab88-ed46-4cc6-8847-c9d0bafd24b9');
