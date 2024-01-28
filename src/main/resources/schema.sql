
-- #Tabela Auction

create table if not exists `auctions` (
                            `auction_id` bigint NOT NULL AUTO_INCREMENT,
                            `seller_id` bigint DEFAULT NULL,
                            `category_id` bigint DEFAULT NULL,
                            `title` varchar(255) DEFAULT NULL,
                            `description` varchar(255) DEFAULT NULL,
                            `startingPrice` decimal(10,2) NOT NULL DEFAULT '0.00',
                            `currentPrice` decimal(10,2) DEFAULT '0.00',
                            `startTime` timestamp NULL DEFAULT NULL,
                            `endTime` timestamp NULL DEFAULT NULL,
                            `current_price` decimal(10,2) NOT NULL DEFAULT '0.00',
                            `end_time` datetime(6) DEFAULT NULL,
                            `start_time` datetime(6) DEFAULT NULL,
                            `starting_price` decimal(10,2) NOT NULL DEFAULT '0.00',
                            PRIMARY KEY (`auction_id`),
                            KEY `seller_id` (`seller_id`),
                            KEY `category_id` (`category_id`),
                            CONSTRAINT `auctions_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `users` (`user_id`),
                            CONSTRAINT `auctions_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
-- #Tabela Autentykacji

create table if not exists `authorities` (
                               `username` varchar(255) NOT NULL,
                               `authority` varchar(255) NOT NULL,
                               PRIMARY KEY (`username`,`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
-- #Tabela Licytacji

create table if not exists `bids` (
                        `bid_id` bigint NOT NULL AUTO_INCREMENT,
                        `auction_id` bigint DEFAULT NULL,
                        `user_id` bigint DEFAULT NULL,
                        `bidTime` datetime(6) DEFAULT NULL,
                        `bid_amount` decimal(38,2) DEFAULT NULL,
                        `bid_time` datetime(6) DEFAULT NULL,
                        PRIMARY KEY (`bid_id`),
                        KEY `auction_id` (`auction_id`),
                        KEY `user_id` (`user_id`),
                        CONSTRAINT `bids_ibfk_1` FOREIGN KEY (`auction_id`) REFERENCES `auctions` (`auction_id`),
                        CONSTRAINT `bids_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
-- #Tabela Kategirii

create table if not exists `categories` (
                              `category_id` bigint NOT NULL AUTO_INCREMENT,
                              `categoryName` varchar(255) DEFAULT NULL,
                              `description` varchar(255) DEFAULT NULL,
                              `category_name` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
-- #Tabela Roli

create table if not exists `roles` (
                         `role_id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
-- #Tabela Transaction Ratings

create table if not exists `transaction_ratings` (
                                       `transaction_rating_id` bigint NOT NULL AUTO_INCREMENT,
                                       `user_id` bigint DEFAULT NULL,
                                       `ratingTime` datetime(6) DEFAULT NULL,
                                       `rating` int DEFAULT NULL,
                                       `comment` varchar(255) DEFAULT NULL,
                                       `rating_time` datetime(6) DEFAULT NULL,
                                       PRIMARY KEY (`transaction_rating_id`),
                                       KEY `user_id` (`user_id`),
                                       CONSTRAINT `transaction_ratings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
#Tabela Użytkowników

create table if not exists users (
                                    `user_id` bigint NOT NULL AUTO_INCREMENT,
                                    `username` varchar(255) DEFAULT NULL,
                                    `email` varchar(255) DEFAULT NULL,
                                    `password` varchar(255) DEFAULT NULL,
                                    `enabled` bit(1) NOT NULL,
                                    PRIMARY KEY (`user_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
#Tabela Roli Użytkowników

create table if not exists `users_role` (
                              `user_id` bigint NOT NULL,
                              `role_id` int NOT NULL,
                              PRIMARY KEY (`user_id`,`role_id`),
                              KEY `FKeejqlb4gq1av9540jg66ju2pi` (`role_id`),
                              CONSTRAINT `FKeejqlb4gq1av9540jg66ju2pi` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
                              CONSTRAINT `FKqpe36jsen4rslwfx5i6dj2fy8` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;