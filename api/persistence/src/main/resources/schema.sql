CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN      NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT current_timestamp,
    unique (username)
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT NOT NULL,
    role    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS detectors
(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    owner_id INT,
    is_online bool default false not null,
    version varchar(10) default '1' not null,
    name varchar(30) not null,
    description varchar(255),
    FOREIGN KEY (user_id) REFERENCES users (id) on DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS signals
(
    id SERIAL PRIMARY KEY,
    detector_id INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    is_heartbeat boolean NOT NULL,
    FOREIGN KEY (detector_id) REFERENCES detectors (id) on DELETE CASCADE
);


insert into users (email, username, password, enabled) values ('tataabancens@gmail.com','tata','$2a$10$XYb4zvDqCL4xbYnaTLORkuSbEYyTiEFMw6yzBZcGmJy41iKBPywQu',true);
insert into users (email, username, password, enabled) values ('cliente1@gmail.com','det_1_cliente_1','$2a$10$zNiZLgf19ioka/72KHvHcOyW4gIfmZa0wzmFyS6vlFZV.xsL8AGeG',true);
insert into users (email, username, password, enabled) values ('cliente1@gmail.com','det_2_cliente_1','$2a$10$GibWXRYu1NIrwG6oJzQRYeLLMCSlN5Ag9IX1eO',true);
insert into users (email, username, password, enabled) values ('tatino@gmail.com','tatino_2','$2a$10$GibWXRYu1NIrwG6oJzQRYeLLMCSlN5Ag9IX1eO',true);
insert into users (email, username, password, enabled) values ('cliente2@gmail.com','det_1_cliente_2','$2a$10$GibWXRYu1NIrwG6oJzQRYeLLMCSlN5Ag9IX1eO',true);

insert into user_role values (1, 'ADMIN');
insert into user_role values (2, 'DETECTOR');
insert into user_role values (3, 'DETECTOR');
insert into user_role values (4, 'USER');
insert into user_role values (5, 'DETECTOR');

insert into detectors (user_id, owner_id, is_online, version, name, description) values (2,1,false,1.0,'Est 1','Estacionamiento 1. Atras de la columna 6H apuntando en direccion al mostrador');
insert into detectors (user_id, owner_id, is_online, version, name, description) values (3,1,false,1.0,'Est 2','Al fondo atras de la maceta');
insert into detectors (user_id, owner_id, is_online, version, name, description) values (5,4,false,1.0,'Est 1','Soy un detector del cliente 2');