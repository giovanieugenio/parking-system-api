CREATE TABLE IF NOT EXISTS tb_client (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    id_user INT,
    FOREIGN KEY (id_user) REFERENCES tb_user(id)
);

insert into tb_client(id, name, cpf, id_user) values (10, 'Ellie Williams', '42104344000', 101);
insert into tb_client(id, name, cpf, id_user) values (11, 'Abigail Anderson', '66484519048', 102);