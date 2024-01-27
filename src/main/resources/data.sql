
--
-- INSERT INTO roles (role_id, name) VALUES
--                                       (1, 'ROLE_USER'),
--                                       (2, 'ROLE_ADMIN');

INSERT INTO categories (category_name, description) VALUES
                                                        ('Electronics', 'Electronic gadgets and devices'),
                                                        ('Books', 'Various types of books'),
                                                        ('Clothing', 'Clothing items and accessories'),
                                                        ('Home and Garden', 'Items for home and garden'),
                                                        ('Toys and Games', 'Toys and games for all ages');

DELETE FROM users;

INSERT INTO users (user_id, username, email, password, enabled) VALUES
                                                                    (1, 'a', 'a', 'a', 0),
                                                                    (2, 'admin', 'admin', '$2a$12$EYK.8IPJ.LGSzQzzbI4XYudHTuxuTziJEdKiViuzvz5pNouGCixly', 1),
                                                                    (3, 'aq', 'q', 'aq', 1),
                                                                    (4, 'G', 'G', '$2a$10$MRkUz9ia1SLmjxTN83gL6uJVzLY/xGXKUzzZk/3qZli0IAurhcfjm', 1),
                                                                    (5, 'w', 'w', '$2a$10$3OXovnVoqFJer8lnNLYjgerI6fhA0WL0KCfJ1VTyiALo2O30O1Uhy', 1),
                                                                    (7, 'me', 'me', '$2a$10$49WGo6A/jjfCS.kzN1sAput9eRw6urNMh2OhnnHIFPhPmlrTZAdY6', 1),
                                                                    (8, '1', '1', '$2a$10$g2fGFCVNf/UXvSV/ylOfmee5OIfOzJ3v.54LN5cErOX8iNGdny.OW', 1),
                                                                    (9, 'ad', 'ad', '$2a$10$U2Q0FDTsWzsKyjwqwry6Hewm7a7n2lgaXepWms0eHhLqABG2oNCsS', 1),
                                                                    (10, 'adasdsa', 'fasfsaf', '$2a$10$vvYFH0X20t27hvjpHmUf9.0EOQZccVQuiu52MT0TxrHW1wYlF4h6e', 1),
                                                                    (12, 'user', NULL, '$2a$10$whHv4tfXCvFS8GlbzxuAHeH7QpZdfyw/gAqxlRH9TmeaYKVxiCiGm', 1);
