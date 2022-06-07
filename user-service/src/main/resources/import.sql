INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('andres', '$2a$10$0jAZasXfH.13LkO9/kj8LOdqU3O0TN/hCDTazV./zBFaOYO3zZr.m', true, 'Andres', 'Guzman', 'andres@gmail.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('admin', '$2a$10$ObFIvyYYoWPcj9p28T31g.d/QCWnYyO.mgjmwkkf3R2kvvHotaqzW', true, 'Jhon', 'Doe', 'jhon@gmail.com');

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO user_roles (user_id , role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id , role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id , role_id) VALUES (2, 1);
