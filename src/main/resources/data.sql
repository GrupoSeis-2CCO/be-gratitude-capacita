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


--Para feedbacks
    -- Inserir 10 usuários (ids explícitos 10..19)
INSERT INTO usuario (id_usuario, nome, cpf, email, senha, fk_cargo) VALUES
(10, 'Ana Lima', '20000000001', 'ana.lima@example.com', 'senhaAna1', 1),
(11, 'Bruno Costa', '20000000002', 'bruno.costa@example.com', 'senhaBruno2', 1),
(12, 'Carla Nunes', '20000000003', 'carla.nunes@example.com', 'senhaCarla3', 2),
(13, 'Diego Alves', '20000000004', 'diego.alves@example.com', 'senhaDiego4', 1),
(14, 'Elisa Prado', '20000000005', 'elisa.prado@example.com', 'senhaElisa5', 2),
(15, 'Felipe Rocha', '20000000006', 'felipe.rocha@example.com', 'senhaFelipe6', 1),
(16, 'Gabriela Moraes', '20000000007', 'gabriela.moraes@example.com', 'senhaGabriela7', 2),
(17, 'Hugo Pereira', '20000000008', 'hugo.pereira@example.com', 'senhaHugo8', 1),
(18, 'Isabela Santos', '20000000009', 'isabela.santos@example.com', 'senhaIsabela9', 2),
(19, 'João Pedro', '20000000010', 'joao.pedro@example.com', 'senhaJoao10', 1)
ON DUPLICATE KEY UPDATE
  nome = VALUES(nome),
  cpf = VALUES(cpf),
  email = VALUES(email),
  senha = VALUES(senha),
  fk_cargo = VALUES(fk_cargo);

-- Inserir 10 feedbacks para o curso 1 (usuarios 10..19)
INSERT INTO feedback (fk_usuario, fk_curso, estrelas, motivo) VALUES
(10, 1, 5, 'Excelente conteúdo, muito didático.'),
(11, 1, 4, 'Bom curso, alguns pontos rápidos.'),
(12, 1, 3, 'Intermediário, faltaram exercícios.'),
(13, 1, 5, 'Ótimo professor e material.'),
(14, 1, 2, 'Ritmo acelerado, difícil acompanhar.'),
(15, 1, 4, 'Conteúdo relevante, mas poderia ter mais exemplos.'),
(16, 1, 1, 'Não gostei da abordagem.'),
(17, 1, 5, 'Amei! Recomendo.'),
(18, 1, 3, 'Ok, mas preciso praticar mais.'),
(19, 1, 4, 'Bom resumo dos conceitos.')
ON DUPLICATE KEY UPDATE
  estrelas = VALUES(estrelas),
  motivo = VALUES(motivo);