insert into users (email, username, password, enabled) values ('tataabancens@gmail.com','tata','$2a$10$XYb4zvDqCL4xbYnaTLORkuSbEYyTiEFMw6yzBZcGmJy41iKBPywQu',true);
insert into users (email, username, password, enabled) values ('cliente1@gmail.com','det_1_cliente_1','$2a$10$zNiZLgf19ioka/72KHvHcOyW4gIfmZa0wzmFyS6vlFZV.xsL8AGeG',true);
insert into users (email, username, password, enabled) values ('cliente1@gmail.com','det_2_cliente_1','$2a$10$GibWXRYu1NIrwG6oJzQRYeLLMCSlN5Ag9IX1eO',true);
insert into users (email, username, password, enabled) values ('carlos@gmail.com','Carlos','$2a$10$XYb4zvDqCL4xbYnaTLORkuSbEYyTiEFMw6yzBZcGmJy41iKBPywQu',true);
insert into users (email, username, password, enabled) values ('carlos@gmail.com','det_1_Carlos','$2a$10$GibWXRYu1NIrwG6oJzQRYeLLMCSlN5Ag9IX1eO',true);
insert into users (email, username, password, enabled) values ('carlos@gmail.com','det_2_Carlos','$2a$10$GibWXRYu1NIrwG6oJzQRYeLLMCSlN5Ag9IX1eO',true);
insert into users (email, username, password, enabled) values ('marta@gmail.com','Marta','$2a$10$XYb4zvDqCL4xbYnaTLORkuSbEYyTiEFMw6yzBZcGmJy41iKBPywQu',true);
insert into users (email, username, password, enabled) values ('marta@gmail.com','det_1_Marta','$2a$10$GibWXRYu1NIrwG6oJzQRYeLLMCSlN5Ag9IX1eO',true);
insert into users (email, username, password, enabled) values ('marta@gmail.com','det_2_Marta','$2a$10$GibWXRYu1NIrwG6oJzQRYeLLMCSlN5Ag9IX1eO',true);

insert into user_role values (1, 'ADMIN');
insert into user_role values (2, 'DETECTOR');
insert into user_role values (3, 'DETECTOR');
insert into user_role values (4, 'USER');
insert into user_role values (5, 'DETECTOR');

insert into detectors (user_id, owner_id, last_heartbeat, version, name, description) values (2,1,null,1.0,'Est 1A','Estacionamiento 1. Atras de la columna 6H.');
insert into detectors (user_id, owner_id, last_heartbeat, version, name, description) values (3,1,null,1.0,'Est 2A','Estacionamiento 2. Al lado del matafuegos.');
insert into detectors (user_id, owner_id, last_heartbeat, version, name, description) values (5,4,null,1.0,'Jardín','Jardín de Carlos');
insert into detectors (user_id, owner_id, last_heartbeat, version, name, description) values (6,4,null,1.0,'Garage','Garage de Carlos');
insert into detectors (user_id, owner_id, last_heartbeat, version, name, description) values (8,7,null,1.0,'Lámpara','Abajo de la lampara');
insert into detectors (user_id, owner_id, last_heartbeat, version, name, description) values (9,7,null,1.0,'Portón','Atras del portón');






