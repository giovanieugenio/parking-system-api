insert into tb_user(id, username, password, role) values (100, 'joel@gmail.com','$2a$12$lRWY2B8d314F8IO2Ogq0C.5KFbG1yph3nuh3V034Ncypdg7/FYguq', 'ROLE_ADMIN');
insert into tb_user(id, username, password, role) values (101, 'ellie@gmail.com','$2a$12$lRWY2B8d314F8IO2Ogq0C.5KFbG1yph3nuh3V034Ncypdg7/FYguq', 'ROLE_CLIENT');
insert into tb_user(id, username, password, role) values (102, 'abby@gmail.com','$2a$12$lRWY2B8d314F8IO2Ogq0C.5KFbG1yph3nuh3V034Ncypdg7/FYguq', 'ROLE_CLIENT');


insert into tb_client(id, name, cpf, id_user) values (10, 'Ellie Williams', '42104344000', 101);
insert into tb_client(id, name, cpf, id_user) values (22, 'Abigail Anderson', '66484519048', 102);

insert into tb_vacancy (id, code, status) values (100, 'A-01', 'OCCUPIED');
insert into tb_vacancy (id, code, status) values (200, 'A-02', 'OCCUPIED');
insert into tb_vacancy (id, code, status) values (300, 'A-03', 'OCCUPIED');
insert into tb_vacancy (id, code, status) values (400, 'A-04', 'FREE');
insert into tb_vacancy (id, code, status) values (500, 'A-05', 'FREE');

insert into tb_client_have_vacancy (receipt_number, plate, brand, model, color, entry_date, id_client, id_vacancy)
    values ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00', 22, 100);
insert into tb_client_have_vacancy (receipt_number, plate, brand, model, color, entry_date, id_client, id_vacancy)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2023-03-14 10:15:00', 21, 200);
insert into tb_client_have_vacancy (receipt_number, plate, brand, model, color, entry_date, id_client, id_vacancy)
    values ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-14 10:15:00', 22, 300);