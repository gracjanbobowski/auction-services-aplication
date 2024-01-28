
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO categories (category_name, description) VALUES
                                                        ('Electronics', 'Electronic gadgets and devices'),
                                                        ('Books', 'Various types of books'),
                                                        ('Clothing', 'Clothing items and accessories'),
                                                        ('Home and Garden', 'Items for home and garden'),
                                                        ('Toys and Games', 'Toys and games for all ages');

DELETE FROM users;

DELETE FROM users;
INSERT INTO users (username, email, password, enabled) VALUES
                                                           ('admin', 'admin@example.com', '$2a$12$tWEXDNqDa2HVwpNVWX4J/uCwPR0wtRaM60jU2Do/fDxFgurRF/3KS', 1),
                                                           ('user', 'user@example.com', '$2a$12$cPClNuojjZCbnGOGB0oqPO4knZayTs7oArj17pjsZOotjkA.7GJLa', 1);

DELETE FROM authorities;
INSERT INTO authorities (username, authority) VALUES
                                                  ('admin', 'ROLE_ADMIN'),
                                                  ('user', 'ROLE_USER');

DELETE FROM users_role;
INSERT INTO users_role (user_id, role_id) VALUES
                                                  ('1', '2'),
                                                  ('2', '1');