-- Dados iniciais para tabela cargo
INSERT INTO cargo (nome_cargo) VALUES
('Colaborador'),
('Funcionário');

-- Dados iniciais para tabela usuario (dados originais do script)
INSERT INTO usuario (nome, cpf, email, senha, fk_cargo) VALUES
('João Silva', '12345678901', 'joao@email.com', 'senha123', 1),
('Maria Souza', '98765432109', 'maria@email.com', 'senha456', 2),
('Carlos Oliveira', '11122233344', 'carlos@email.com', 'senha789', 1);

-- Dados adicionais de usuario (com timestamps e senhas específicas)
INSERT INTO usuario (
    nome,
    cpf,
    email,
    senha,
    fk_cargo,
    data_entrada,
    ultimo_acesso
) VALUES
('John Doe', '12345678900', 'john@doe.com', '$2a$10$QQPobUtOp3Gwh3P94Itu0u/e3jGNDRt6WHhIqz2TdDFpXaK6y6lw6', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cintia Tanivaro', '98765432100', 'cintia@tanivaro.com', '123123', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserir 10 usuários adicionais com IDs explícitos para cenários de feedback
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

-- Dados iniciais para tabela curso
INSERT INTO curso (titulo_curso, descricao, imagem, ocultado, duracao_estimada) VALUES
('Java Básico', 'Curso introdutório de Java', 'java.jpg', 0, 20),
('Spring Boot', 'API REST com Spring Boot', 'spring.jpg', 0, 40),
('MySQL Essencial', 'Fundamentos do banco de dados MySQL', 'mysql.jpg', 0, 30);

-- Dados iniciais para tabela matricula
INSERT INTO matricula (fk_usuario, fk_curso) VALUES
(2, 2),
(1, 1);

-- Dados iniciais para tabela video
INSERT INTO video (nome_video, descricao_video, url_video, ordem_video, fk_curso) VALUES
('Introdução', 'video1', 'http://exemplo.com/video1', 1, 1),
('Aula 1', 'video2', 'http://exemplo.com/video2', 2, 2),
('Aula 2', 'video3', 'http://exemplo.com/video3', 3, 3);

-- Dados iniciais para tabela apostila
INSERT INTO apostila (nome_apostila_original, nome_apostila_armazenamento, descricao_apostila, tamanho_bytes, is_apostila_oculto, ordem_apostila, fk_curso) VALUES
('Apostila 1.pdf', 'apostila1_armazenada.pdf', 'apostila 1', 225252, 1, 1, 1),
('Apostila 2.pdf', 'apostila2_armazenada.pdf', 'apostila 2', 25425, 0, 2, 2),
('Apostila 3.pdf', 'apostila3_armazenada.pdf', 'apostila 3', 2542, 1, 3, 3);

-- Dados iniciais para tabela material_aluno
INSERT INTO material_aluno (fk_usuario, fk_cargo, fk_video, fk_apostila) VALUES
(1, 1, 1, 1),
(2, 2, 2, 2),
(2, 1, 3, 3);

-- Dados iniciais para tabela avaliacao
INSERT INTO avaliacao (nota_minima, fk_curso) VALUES
(7.5, 1),
(6.0, 2),
(8.0, 3);

-- Dados iniciais para tabela alternativa
INSERT INTO alternativa (fk_avaliacao, texto, ordem_alternativa) VALUES
(1, 'Alternativa A', 1),
(1, 'Alternativa B', 2),
(2, 'Alternativa C', 1);

-- Dados iniciais para tabela tentativa
INSERT INTO tentativa (fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
(1, 1, '2025-06-01 10:00:00', 1),
(2, 2, '2025-06-02 14:30:00', 2),
(1, 1, '2025-06-03 09:15:00', 1);

-- Dados iniciais para tabela questao
INSERT INTO questao (fk_avaliacao, enunciado, numero_questao, fk_alternativa_correta) VALUES
(1, 'O que é JVM?', 1, 2),
(1, 'Qual comando compila um arquivo Java?', 2, 3),
(2, 'Para que serve o @RestController no Spring?', 1, 4);

-- Dados iniciais para tabela resposta_do_usuario
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
(1, 1, 1, 1, 1, 1),
(2, 2, 2, 2, 2, 2),
(1, 1, 3, 1, 2, 3);

-- Feedbacks originais do script
INSERT INTO feedback (fk_usuario, fk_curso, estrelas, motivo) VALUES
(1, 1, 5, 'Bonzão!'),
(2, 2, 4, 'Muito bom, mas pode melhorar.');

-- Feedbacks adicionais para o curso 1 (usuários 10..19)
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
(19, 1, 4, 'Bom resumo dos conceitos.');