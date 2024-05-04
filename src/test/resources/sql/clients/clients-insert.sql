insert into tb_user(id, username, password, role) values (100, 'joel@gmail.com','$2a$12$IuMxPJ6HiJm.yUdjJbWuiuhZd.zCQn5j02C2Ll6/MR0anUOHhOS7O', 'ROLE_ADMIN');
insert into tb_user(id, username, password, role) values (101, 'ellie@gmail.com','$2a$12$IuMxPJ6HiJm.yUdjJbWuiuhZd.zCQn5j02C2Ll6/MR0anUOHhOS7O', 'ROLE_CLIENT');
insert into tb_user(id, username, password, role) values (102, 'abby@gmail.com','$2a$12$IuMxPJ6HiJm.yUdjJbWuiuhZd.zCQn5j02C2Ll6/MR0anUOHhOS7O', 'ROLE_CLIENT');
insert into tb_user(id, username, password, role) values (103, 'tommy@gmail.com','$2a$12$IuMxPJ6HiJm.yUdjJbWuiuhZd.zCQn5j02C2Ll6/MR0anUOHhOS7O', 'ROLE_CLIENT');

insert into tb_client(id, name, cpf, id_user) values (10, 'Ellie Williams', '42104344000', 101);
insert into tb_client(id, name, cpf, id_user) values (11, 'Abigail Anderson', '66484519048', 102);