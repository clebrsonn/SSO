CREATE TABLE users(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	pwd VARCHAR(150) NOT NULL
);

CREATE TABLE roles(
	id BIGSERIAL PRIMARY KEY,
	role VARCHAR(50) NOT NULL
);

CREATE TABLE role_user(
  user_id BIGSERIAL NOT NULL,
	role_id BIGSERIAL NOT NULL,
	PRIMARY KEY (user_id, role_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (role_id) REFERENCES roles(id)
);
--senha: admin
INSERT INTO users (id, name, email, pwd) values (1, 'Administrador', 'admin@admin.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
--senha: maria
INSERT INTO users (id, name, email, pwd) values (2, 'Maria Silva', 'maria@admin.com', '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq');

INSERT INTO roles (id, role) values (1, 'ROLE_ADMINISTRADOR');

-- admin
INSERT INTO role_user (user_id, role_id) values (1, 1);

-- maria
INSERT INTO role_user (user_id, role_id) values (2, 1);
