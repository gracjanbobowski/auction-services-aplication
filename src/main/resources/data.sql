-- Add sample categories if they don't exist
INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Electronics', 'Electronic gadgets and devices') AS temp
WHERE NOT EXISTS (
    SELECT category_name FROM categories WHERE category_name = 'Electronics'
);
INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Books', 'Various types of books') AS temp
WHERE NOT EXISTS (
    SELECT category_name FROM categories WHERE category_name = 'Books'
);
INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Clothing', 'Clothing items and accessories') AS temp
WHERE NOT EXISTS (
    SELECT category_name FROM categories WHERE category_name = 'Clothing'
);
INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Home and Garden', 'Items for home and garden') AS temp
WHERE NOT EXISTS (
    SELECT category_name FROM categories WHERE category_name = 'Home and Garden'
);
INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Toys and Games', 'Toys and games for all ages') AS temp
WHERE NOT EXISTS (
    SELECT category_name FROM categories WHERE category_name = 'Toys and Games'
);

-- Create roles if they don't exist
INSERT INTO roles (name)
SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (
    SELECT name FROM roles WHERE name = 'ROLE_ADMIN'
);
INSERT INTO roles (name)
SELECT 'ROLE_USER' WHERE NOT EXISTS (
    SELECT name FROM roles WHERE name = 'ROLE_USER'
);

-- Create authorities for sample users if they don't exist
INSERT INTO authorities (username, authority)
SELECT 'admin', 'ROLE_ADMIN' WHERE NOT EXISTS (
    SELECT 1 FROM authorities WHERE username = 'admin' AND authority = 'ROLE_ADMIN'
);
INSERT INTO authorities (username, authority)
SELECT 'user', 'ROLE_USER' WHERE NOT EXISTS (
    SELECT 1 FROM authorities WHERE username = 'user' AND authority = 'ROLE_USER'
);
INSERT INTO authorities (username, authority)
SELECT 'sda', 'ROLE_USER' WHERE NOT EXISTS (
    SELECT 1 FROM authorities WHERE username = 'sda' AND authority = 'ROLE_USER'
);

-- Insert users only if they don't exist
INSERT INTO users (username, email, password, enabled)
SELECT 'admin', 'admin@example.com', '$2a$12$EYK.8IPJ.LGSzQzzbI4XYudHTuxuTziJEdKiViuzvz5pNouGCixly', true WHERE NOT EXISTS (
    SELECT username FROM users WHERE username = 'admin'
);
INSERT INTO users (username, email, password, enabled)
SELECT 'user', 'user@example.com', '$2a$12$1kskjU9yQ7.thXERATW2K.jM3.tDWrR8YHLI08pza.SWeH4r01UOO', true WHERE NOT EXISTS (
    SELECT username FROM users WHERE username = 'user'
);
INSERT INTO users (username, email, password, enabled)
SELECT 'sda', 'sda@example.com', '$2a$12$Np1WLOduRQC/wAWVVYrsKuQXWSOHMlS8XTgDKQ88WNWpjJjElRuWq', true WHERE NOT EXISTS (
    SELECT username FROM users WHERE username = 'sda'
);

-- Assign roles to users only if they don't have them
INSERT INTO users_role (user_id, role_id)
SELECT (SELECT user_id FROM users WHERE username = 'admin'), (SELECT role_id FROM roles WHERE name = 'ROLE_ADMIN')
WHERE NOT EXISTS (
    SELECT 1 FROM users_role WHERE user_id = (SELECT user_id FROM users WHERE username = 'admin') AND role_id = (SELECT role_id FROM roles WHERE name = 'ROLE_ADMIN')
);
INSERT INTO users_role (user_id, role_id)
SELECT (SELECT user_id FROM users WHERE username = 'user'), (SELECT role_id FROM roles WHERE name = 'ROLE_USER')
WHERE NOT EXISTS (
    SELECT 1 FROM users_role WHERE user_id = (SELECT user_id FROM users WHERE username = 'user') AND role_id = (SELECT role_id FROM roles WHERE name = 'ROLE_USER')
);
INSERT INTO users_role (user_id, role_id)
SELECT (SELECT user_id FROM users WHERE username = 'sda'), (SELECT role_id FROM roles WHERE name = 'ROLE_USER')
WHERE NOT EXISTS (
    SELECT 1 FROM users_role WHERE user_id = (SELECT user_id FROM users WHERE username = 'sda') AND role_id = (SELECT role_id FROM roles WHERE name = 'ROLE_USER')
);

-- Add auctions for each user only if they don't exist
-- Add auctions for user 'admin'
INSERT INTO auctions (user_id, category_id, title, description, starting_price, current_price, start_time, end_time)
SELECT (SELECT user_id FROM users WHERE username = 'admin'), 1, 'Vintage Camera', 'Rare vintage camera in excellent condition.', 150.00, 150.00, '2024-01-10 10:00:00', '2024-01-20 20:00:00'
WHERE NOT EXISTS (
    SELECT 1 FROM auctions WHERE title = 'Vintage Camera' AND user_id = (SELECT user_id FROM users WHERE username = 'admin')
);
INSERT INTO auctions (user_id, category_id, title, description, starting_price, current_price, start_time, end_time)
SELECT (SELECT user_id FROM users WHERE username = 'admin'), 2, 'Signed Novel', 'First edition novel signed by the author.', 75.00, 75.00, '2024-02-15 09:00:00', '2024-02-25 23:00:00'
WHERE NOT EXISTS (
    SELECT 2 FROM auctions WHERE title = 'Signed Novel' AND user_id = (SELECT user_id FROM users WHERE username = 'admin')
);
-- Add auctions for user 'user'
INSERT INTO auctions (user_id, category_id, title, description, starting_price, current_price, start_time, end_time)
SELECT (SELECT user_id FROM users WHERE username = 'user'), 3, 'Handmade Scarf', 'Handmade wool scarf, very warm and stylish.', 40.00, 40.00, '2024-03-20 09:00:00', '2024-03-30 21:00:00'
WHERE NOT EXISTS (
    SELECT 3 FROM auctions WHERE title = 'Handmade Scarf' AND user_id = (SELECT user_id FROM users WHERE username = 'user')
);
INSERT INTO auctions (user_id, category_id, title, description, starting_price, current_price, start_time, end_time)
SELECT (SELECT user_id FROM users WHERE username = 'user'), 4, 'BBQ Grill Set', 'High-end BBQ grill set with accessories.', 120.00, 120.00, '2024-04-15 13:00:00', '2024-04-25 17:00:00'
WHERE NOT EXISTS (
    SELECT 4 FROM auctions WHERE title = 'BBQ Grill Set' AND user_id = (SELECT user_id FROM users WHERE username = 'user')
);
-- Add auctions for user 'sda'
INSERT INTO auctions (user_id, category_id, title, description, starting_price, current_price, start_time, end_time)
SELECT (SELECT user_id FROM users WHERE username = 'sda'), 1, 'Professional Microphone', 'Studio-quality microphone, ideal for recording.', 250.00, 250.00, '2024-01-05 11:00:00', '2024-01-15 19:00:00'
WHERE NOT EXISTS (
    SELECT 1 FROM auctions WHERE title = 'Professional Microphone' AND user_id = (SELECT user_id FROM users WHERE username = 'sda')
);
INSERT INTO auctions (user_id, category_id, title, description, starting_price, current_price, start_time, end_time)
SELECT (SELECT user_id FROM users WHERE username = 'sda'), 2, 'Sci-Fi Book Series', 'Complete sci-fi book series in hardcover.', 90.00, 90.00, '2024-02-05 10:00:00', '2024-02-15 20:00:00'
WHERE NOT EXISTS (
    SELECT 2 FROM auctions WHERE title = 'Sci-Fi Book Series' AND user_id = (SELECT user_id FROM users WHERE username = 'sda')
);
