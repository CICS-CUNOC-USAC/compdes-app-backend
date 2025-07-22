ALTER TABLE `classroom` ADD COLUMN `capacity` INT NOT NULL DEFAULT 30;

CREATE TABLE IF NOT EXISTS `reservation` (
  `id` varchar(36) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `desactivated_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `attended_date_time` datetime(6) DEFAULT NULL,
  `activity_id` varchar(36) NOT NULL,
  `participant_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf56tgucgqr62fno50bpmhdc99` (`activity_id`),
  KEY `FKParticipantId` (`participant_id`),
  CONSTRAINT `FKf56tgucgqr62fno50bpmhdc99` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `FKParticipantId` FOREIGN KEY (`participant_id`) REFERENCES `participant` (`id`)
) 
