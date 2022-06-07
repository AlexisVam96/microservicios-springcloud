
INSERT INTO usuarios (username, password, enabled) values ('alexis', '$2a$10$GwZdVSR5rRYzgPoKO61u2.OqSITlCoSFJ2DouLlKWeWitgmKOL.Oq', '');
INSERT INTO usuarios (username, password, enabled) values ('admin','$2a$10$5IQZYrzdpciTn00ztIZ9/uPIc7O6PtAjJMgnsmZgcUOWZBZgE0z.y', '');


INSERT INTO roles (nombre) values ('ROLE_USER');
INSERT INTO roles (nombre) values ('ROLE_ADMIN');

INSERT INTO usuarios_roles (id_usuarios, id_roles) values (1, 1);
INSERT INTO usuarios_roles (id_usuarios, id_roles) values (2, 2);
INSERT INTO usuarios_roles (id_usuarios, id_roles) values (2, 1);