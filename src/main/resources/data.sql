-- Utwórz role i autoryzację dla administratora
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Sprawdź, czy autoryzacja dla administratora nie istnieje przed dodaniem
INSERT INTO authorities (username, authority)
SELECT 'admin', 'ROLE_ADMIN'
FROM dual
WHERE NOT EXISTS (
        SELECT 1
        FROM authorities
        WHERE username = 'admin' AND authority = 'ROLE_ADMIN'
    );

-- Utwórz rolę i autoryzację dla użytkownika, jeśli nie istnieje
INSERT IGNORE INTO roles (name) VALUES ('ROLE_USER');

-- Sprawdź, czy autoryzacja dla użytkownika nie istnieje przed dodaniem
INSERT IGNORE INTO authorities (username, authority)
SELECT 'user', 'ROLE_USER'
FROM dual
WHERE NOT EXISTS (
        SELECT 2
        FROM authorities
        WHERE username = 'user' AND authority = 'ROLE_USER'
    );

-- Dodaj przykładowych użytkowników (admin, user)
INSERT INTO users (username, email, password, enabled)
VALUES ('admin', 'admin@example.com', '$2a$12$EYK.8IPJ.LGSzQzzbI4XYudHTuxuTziJEdKiViuzvz5pNouGCixly', 1);

INSERT INTO users (username, email, password, enabled)
VALUES ('user', 'user@example.com', '$2a$12$1kskjU9yQ7.thXERATW2K.jM3.tDWrR8YHLI08pza.SWeH4r01UOO', 1);

-- Przypisz role do użytkowników
INSERT INTO users_role (user_id, role_id)
VALUES ((SELECT user_id FROM users WHERE username = 'admin'),
        (SELECT role_id FROM roles WHERE name = 'ROLE_ADMIN'));

INSERT INTO users_role (user_id, role_id)
VALUES ((SELECT user_id FROM users WHERE username = 'user'),
        (SELECT role_id FROM roles WHERE name = 'ROLE_USER'));
