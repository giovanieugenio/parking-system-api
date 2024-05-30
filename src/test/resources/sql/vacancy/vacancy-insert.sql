insert into tb_user(id, username, password, role) values (100, 'joel@gmail.com','$2a$12$lRWY2B8d314F8IO2Ogq0C.5KFbG1yph3nuh3V034Ncypdg7/FYguq', 'ROLE_ADMIN');
insert into tb_user(id, username, password, role) values (101, 'ellie@gmail.com','$2a$12$lRWY2B8d314F8IO2Ogq0C.5KFbG1yph3nuh3V034Ncypdg7/FYguq', 'ROLE_CLIENT');
insert into tb_user(id, username, password, role) values (102, 'abby@gmail.com','$2a$12$lRWY2B8d314F8IO2Ogq0C.5KFbG1yph3nuh3V034Ncypdg7/FYguq', 'ROLE_CLIENT');

CREATE TABLE IF NOT EXISTS tb_vacancy (
    id INT PRIMARY KEY,
    code VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL
);

insert into tb_vacancy(id, code, status) values (10, 'A-01', 'FREE');
insert into tb_vacancy(id, code, status) values (20, 'A-02', 'FREE');
insert into tb_vacancy(id, code, status) values (30, 'A-03', 'OCCUPIED');
insert into tb_vacancy(id, code, status) values (40, 'A-04', 'FREE');