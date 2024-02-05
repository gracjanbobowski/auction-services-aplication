-- Auction Table
CREATE TABLE IF NOT EXISTS auctions (
                                        auction_id BIGINT NOT NULL AUTO_INCREMENT,
                                        user_id BIGINT,
                                        category_id BIGINT NOT NULL,
                                        title VARCHAR(255) NOT NULL,
                                        description VARCHAR(255) NOT NULL,
                                        starting_price DECIMAL(10,2) NOT NULL DEFAULT '0.00',
                                        current_price DECIMAL(10,2) NOT NULL DEFAULT '0.00',
                                        start_time DATETIME(6) NOT NULL,
                                        end_time DATETIME(6) NOT NULL,
                                        PRIMARY KEY (auction_id),
                                        FOREIGN KEY (user_id) REFERENCES users(user_id),
                                        FOREIGN KEY (category_id) REFERENCES categories(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Categories Table
CREATE TABLE IF NOT EXISTS categories (
                                          category_id BIGINT NOT NULL AUTO_INCREMENT,
                                          category_name VARCHAR(255) NOT NULL UNIQUE,
                                          description VARCHAR(255),
                                          PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
                                     user_id BIGINT NOT NULL AUTO_INCREMENT,
                                     username VARCHAR(255) NOT NULL UNIQUE,
                                     email VARCHAR(255) NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     enabled BOOLEAN NOT NULL,
                                     PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Roles Table
CREATE TABLE IF NOT EXISTS roles (
                                     role_id INT NOT NULL AUTO_INCREMENT,
                                     name VARCHAR(255) NOT NULL UNIQUE,
                                     PRIMARY KEY (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Authorities Table
CREATE TABLE IF NOT EXISTS authorities (
                                           authority_id BIGINT NOT NULL AUTO_INCREMENT,
                                           user_id BIGINT NOT NULL,
                                           authority VARCHAR(255) NOT NULL,
                                           PRIMARY KEY (authority_id),
                                           FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bids Table
CREATE TABLE IF NOT EXISTS bids (
                                    bid_id BIGINT NOT NULL AUTO_INCREMENT,
                                    auction_id BIGINT NOT NULL,
                                    user_id BIGINT NOT NULL,
                                    bid_amount DECIMAL(10,2),
                                    bid_time DATETIME(6) NOT NULL,
                                    PRIMARY KEY (bid_id),
                                    FOREIGN KEY (auction_id) REFERENCES auctions(auction_id),
                                    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TransactionRatings Table
CREATE TABLE IF NOT EXISTS transaction_ratings (
                                                   transaction_rating_id BIGINT NOT NULL AUTO_INCREMENT,
                                                   user_id BIGINT,
                                                   ratingTime DATETIME(6),
                                                   rating INT,
                                                   comment VARCHAR(255),
                                                   PRIMARY KEY (transaction_rating_id),
                                                   FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- UsersRoles Join Table
CREATE TABLE IF NOT EXISTS users_role (
                                          user_id BIGINT NOT NULL,
                                          role_id INT NOT NULL UNIQUE,
                                          PRIMARY KEY (user_id, role_id),
                                          FOREIGN KEY (user_id) REFERENCES users(user_id),
                                          FOREIGN KEY (role_id) REFERENCES roles(role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;