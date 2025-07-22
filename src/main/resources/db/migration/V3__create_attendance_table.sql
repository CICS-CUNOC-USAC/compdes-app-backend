CREATE TABLE IF NOT EXISTS `attendance` (
  `id` VARCHAR(36) NOT NULL,
  `created_at` DATETIME(6) NOT NULL,
  `deleted_at` DATETIME(6) DEFAULT NULL,
  `desactivated_at` DATETIME(6) DEFAULT NULL,
  `updated_at` DATETIME(6) DEFAULT NULL,
  `activity_id` VARCHAR(36) NOT NULL,
  `participant_id` VARCHAR(36) COLLATE utf8mb4_general_ci NOT NULL,
  `entry_time` DATETIME(6) DEFAULT NULL,
  `exit_time` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAttendanceActivity` (`activity_id`),
  KEY `FKAttendanceParticipant` (`participant_id`),
  CONSTRAINT `FKAttendanceActivity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `FKAttendanceParticipant` FOREIGN KEY (`participant_id`) REFERENCES `participant` (`id`)
)