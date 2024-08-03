CREATE DATABASE coupons;

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20),
    description VARCHAR(100),
    price DECIMAL(8,3)
);

CREATE TABLE coupon (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20),
    discount DECIMAL(8,3),
    exp_date VARCHAR(100)
);

INSERT INTO coupon (id, code, discount, exp_date)
VALUES (1, '50SALE', 50.0, '2025-12-20');

CREATE TABLE user (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(20),
	last_name VARCHAR(20),
	email VARCHAR(20) UNIQUE KEY,
	password VARCHAR(250)
);

CREATE TABLE role (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(20)
);

CREATE TABLE user_role (
	user_id INT,
	role_id INT,
	FOREIGN KEY(user_id) REFERENCES user(id),
	FOREIGN KEY(role_id) REFERENCES role(id)
);

INSERT INTO role (id, name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

INSERT INTO user (id, first_name, last_name, email, password)
VALUES
	(1, 'Bijoy', 'Biswas', 'bijoy@email.com', '$2a$10$EMw9AqUTYnl2ancNh8HrXeKFxksMcNW/81ldemXOIcFGh68stoE/O'),
	(2, 'Sanskruti', 'Adhikari', 'sanskruti@email.com', '$2a$10$L4JWlZMAgqjdh.Aa.eqcAulANeRmdLPRvF3bNgoMLby7UaeDUSEYu');

INSERT INTO user_role (user_id, role_id)
VALUES
	(1, 1),
	(2, 2);