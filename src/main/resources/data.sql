INSERT INTO
    cargo (nome_cargo)
VALUES ('Funcionario'),
    ('Colaborador');

INSERT INTO
    usuario (
        nome,
        cpf,
        email,
        senha,
        fk_cargo,
        data_entrada,
        ultimo_acesso
    )
VALUES (
        'John Doe',
        '12345678900',
        'john@doe.com',
        '$2a$10$QQPobUtOp3Gwh3P94Itu0u/e3jGNDRt6WHhIqz2TdDFpXaK6y6lw6',
        1,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Cintia Tanivaro',
        '98765432100',
        'cintia@tanivaro.com',
        '123123',
        2,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );