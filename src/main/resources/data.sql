-- Garantir que usuários base (id 1, 2, 3) existem

INSERT INTO cargo (id_cargo, nome_cargo) VALUES
  (1, 'Colaborador'),
  (2, 'Funcionário')
ON DUPLICATE KEY UPDATE nome_cargo = VALUES(nome_cargo);

-- Garantir que cursos base existem antes das avaliações
INSERT INTO curso (id_curso, titulo_curso, descricao, imagem, ocultado, duracao_estimada) VALUES
  (1, 'Java Básico', 'Curso introdutório de Java', 'java.jpg', 0, 20),
  (2, 'Spring Boot', 'API REST com Spring Boot', 'spring.jpg', 0, 40),
  (3, 'MySQL Essencial', 'Fundamentos do banco de dados MySQL', 'mysql.jpg', 0, 30)
ON DUPLICATE KEY UPDATE titulo_curso = VALUES(titulo_curso), descricao = VALUES(descricao), imagem = VALUES(imagem), ocultado = VALUES(ocultado), duracao_estimada = VALUES(duracao_estimada);

-- Garantir que avaliações base existem antes de qualquer tentativa
INSERT INTO avaliacao (id_avaliacao, nota_minima, fk_curso) VALUES
  (1, 7.5, 1),
  (2, 6.0, 2),
  (3, 8.0, 3)
ON DUPLICATE KEY UPDATE nota_minima = VALUES(nota_minima), fk_curso = VALUES(fk_curso);

INSERT INTO usuario (id_usuario, nome, cpf, email, senha, fk_cargo) VALUES
  (1, 'João Silva', '12345678901', 'joao@email.com', 'senha123', 1),
  (2, 'Maria Souza', '98765432109', 'maria@email.com', 'senha456', 2),
  (3, 'Carlos Oliveira', '11122233344', 'carlos@email.com', 'senha789', 1)
ON DUPLICATE KEY UPDATE nome = VALUES(nome), cpf = VALUES(cpf), email = VALUES(email), senha = VALUES(senha), fk_cargo = VALUES(fk_cargo);
-- Dados adicionais de usuario (id 10 a 19) para testes variados
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
ON DUPLICATE KEY UPDATE nome = VALUES(nome), cpf = VALUES(cpf), email = VALUES(email), senha = VALUES(senha), fk_cargo = VALUES(fk_cargo);




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
('John Doe', '12345678900', 'john@doe.com', '$2a$10$QQPobUtOp3Gwh3P94Itu0u/e3jGNDRt6WHhIqz2TdDFpXaK6y6lw6', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
  nome = VALUES(nome),
  cpf = VALUES(cpf),
  email = VALUES(email),
  senha = VALUES(senha),
  fk_cargo = VALUES(fk_cargo),
  data_entrada = VALUES(data_entrada),
  ultimo_acesso = VALUES(ultimo_acesso);

INSERT INTO usuario (
    nome,
    cpf,
    email,
    senha,
    fk_cargo,
    data_entrada,
    ultimo_acesso
) VALUES
('Cintia Tanivaro', '98765432100', 'cintia@tanivaro.com', '123123', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
  nome = VALUES(nome),
  cpf = VALUES(cpf),
  email = VALUES(email),
  senha = VALUES(senha),
  fk_cargo = VALUES(fk_cargo),
  data_entrada = VALUES(data_entrada),
  ultimo_acesso = VALUES(ultimo_acesso);

-- Dados iniciais para tabela matricula

INSERT INTO matricula (fk_usuario, fk_curso, fk_inicio, ultimo_senso, completo, data_finalizado) VALUES
  (1, 1, '2025-01-11 08:00:00', '2025-01-22 06:00:00', 0, NULL),
  (2, 2, '2025-01-12 09:00:00', '2025-01-18 09:30:00', 1, '2025-01-20 15:00:00')
ON DUPLICATE KEY UPDATE
  fk_inicio = VALUES(fk_inicio),
  ultimo_senso = VALUES(ultimo_senso),
  completo = VALUES(completo),
  data_finalizado = VALUES(data_finalizado);

-- Novos participantes do curso 1
-- Nunca acessou, tem material
INSERT INTO matricula (fk_usuario, fk_curso, fk_inicio, ultimo_senso, completo, data_finalizado) VALUES
  (10, 1, '2025-02-01 08:00:00', '2025-02-05 10:00:00', 1, '2025-02-10 10:00:00'), -- agora concluído
  (11, 1, '2025-02-02 08:00:00', '2025-02-10 10:00:00', 1, '2025-02-15 12:00:00'),
  (12, 1, '2025-02-03 08:00:00', '2025-02-12 11:20:00', 1, '2025-02-16 11:20:00'), -- agora concluído
  (13, 1, '2025-02-04 08:00:00', '2025-02-14 12:00:00', 0, NULL),
  (14, 1, '2025-02-05 08:00:00', '2025-02-20 13:00:00', 1, '2025-02-25 14:00:00'),
  (15, 1, '2025-02-06 08:00:00', '2025-02-21 14:00:00', 1, '2025-02-26 14:00:00'), -- agora concluído
  (16, 1, '2025-02-07 08:00:00', '2025-02-18 09:00:00', 1, '2025-02-20 09:00:00'), -- agora concluído
  (17, 1, '2025-02-08 08:00:00', '2025-02-22 15:00:00', 1, '2025-02-28 16:00:00'),
  (19, 1, '2025-02-10 08:00:00', '2025-02-24 09:45:00', 1, '2025-02-27 09:45:00') -- agora concluído
ON DUPLICATE KEY UPDATE
  fk_inicio = VALUES(fk_inicio),
  ultimo_senso = VALUES(ultimo_senso),
  completo = VALUES(completo),
  data_finalizado = VALUES(data_finalizado);



-- Dados iniciais para tabela video
INSERT INTO video (nome_video, descricao_video, url_video, ordem_video, fk_curso) VALUES
('Introdução', 'video1', 'http://exemplo.com/video1', 1, 1),
('Aula 1', 'video2', 'http://exemplo.com/video2', 2, 2),
('Aula 2', 'video3', 'http://exemplo.com/video3', 3, 3);

-- Adiciona um vídeo extra para o curso 1 (teste)
INSERT INTO video (id_video, nome_video, descricao_video, url_video, ordem_video, fk_curso) VALUES
  (4, 'Conteúdo Extra', 'Vídeo extra do curso 1', 'http://exemplo.com/video_extra', 2, 1)
ON DUPLICATE KEY UPDATE nome_video = VALUES(nome_video), descricao_video = VALUES(descricao_video), url_video = VALUES(url_video), ordem_video = VALUES(ordem_video), fk_curso = VALUES(fk_curso);

-- Dados iniciais para tabela apostila
INSERT INTO apostila (id_apostila, nome_apostila_original, nome_apostila_armazenamento, descricao_apostila, tamanho_bytes, is_apostila_oculto, ordem_apostila, fk_curso) VALUES
  (1, 'Apostila 1.pdf', 'apostila1_armazenada.pdf', 'apostila 1', 225252, 1, 1, 1),
  (2, 'Apostila 2.pdf', 'apostila2_armazenada.pdf', 'apostila 2', 158000, 0, 2, 2),
  (3, 'Apostila 3.pdf', 'apostila3_armazenada.pdf', 'apostila 3', 2542, 1, 3, 3)
ON DUPLICATE KEY UPDATE nome_apostila_original = VALUES(nome_apostila_original), nome_apostila_armazenamento = VALUES(nome_apostila_armazenamento), descricao_apostila = VALUES(descricao_apostila), tamanho_bytes = VALUES(tamanho_bytes), is_apostila_oculto = VALUES(is_apostila_oculto), ordem_apostila = VALUES(ordem_apostila), fk_curso = VALUES(fk_curso);

-- Adiciona uma apostila extra para o curso 1 (teste)
INSERT INTO apostila (id_apostila, nome_apostila_original, nome_apostila_armazenamento, descricao_apostila, tamanho_bytes, is_apostila_oculto, ordem_apostila, fk_curso) VALUES
  (4, 'Apostila Extra.pdf', 'apostila4_armazenada.pdf', 'Apostila extra do curso 1', 102400, 0, 2, 1)
ON DUPLICATE KEY UPDATE nome_apostila_original = VALUES(nome_apostila_original), nome_apostila_armazenamento = VALUES(nome_apostila_armazenamento), descricao_apostila = VALUES(descricao_apostila), tamanho_bytes = VALUES(tamanho_bytes), is_apostila_oculto = VALUES(is_apostila_oculto), ordem_apostila = VALUES(ordem_apostila), fk_curso = VALUES(fk_curso);

INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  -- For course 1 we want exactly 1 video + 1 apostila as materials
  -- Insert two material_aluno rows per student (one for the video, one for the apostila)
  -- Users 10,11,12,15 will have both finalized (to reach 4/4 when combined with 2 question answers)
  -- Other users get representative rows (may be finalized or not)

  -- User 10 (completed)
  (10, 1, 1, NULL, 1, '2025-02-05 10:00:00'),
  (10, 1, NULL, 1, 1, '2025-02-06 10:00:00'),

  -- User 11 (completed)
  (11, 1, 1, NULL, 1, '2025-02-10 10:15:00'),
  (11, 1, NULL, 1, 1, '2025-02-11 10:00:00'),

  -- User 12 (completed)
  (12, 1, 1, NULL, 1, '2025-02-12 11:20:00'),
  (12, 1, NULL, 1, 1, '2025-02-13 11:20:00'),

  -- User 13 (not completed)
  (13, 1, 1, NULL, 0, NULL),
  (13, 1, NULL, 1, 0, NULL),

  -- User 14 (partial)
  (14, 1, 1, NULL, 1, '2025-02-20 13:10:00'),
  (14, 1, NULL, 1, 0, NULL),

  -- User 15 (completed)
  (15, 1, 1, NULL, 1, '2025-02-21 14:25:00'),
  (15, 1, NULL, 1, 1, '2025-02-22 14:25:00'),

  -- User 16 (not completed)
  (16, 1, 1, NULL, 0, NULL),
  (16, 1, NULL, 1, 0, NULL),

  -- User 17 (mostly completed)
  (17, 1, 1, NULL, 1, '2025-02-22 15:55:00'),
  (17, 1, NULL, 1, 1, '2025-02-23 15:55:00'),

  -- User 19 (partial)
  (19, 1, 1, NULL, 0, '2025-02-24 09:45:00'),
  (19, 1, NULL, 1, 0, '2025-02-24 09:45:00')
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

-- Adiciona material_aluno extra para os usuários 10,11,12,15 com o novo vídeo (id_video=4) e a nova apostila (id_apostila=4)
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  (10, 1, 4, NULL, 1, '2025-03-11 10:00:00'),
  (10, 1, NULL, 4, 1, '2025-03-11 11:00:00'),
  (11, 1, 4, NULL, 1, '2025-03-11 10:05:00'),
  (11, 1, NULL, 4, 1, '2025-03-11 11:05:00'),
  (12, 1, 4, NULL, 1, '2025-03-11 10:10:00'),
  (12, 1, NULL, 4, 1, '2025-03-11 11:10:00'),
  (15, 1, 4, NULL, 1, '2025-03-11 10:20:00'),
  (15, 1, NULL, 4, 1, '2025-03-11 11:20:00')
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

-- ...existing code...
-- Dados iniciais para tabela apostila (removido duplicidade)
-- Já inserido anteriormente
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
(1, 1, 1, 1, 1, '2025-01-22 06:00:00'),
(2, 2, 2, 2, 1, '2025-01-18 09:30:00'),
(2, 2, 3, 3, 0, NULL)
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

-- Dados iniciais para tabela alternativa
INSERT INTO alternativa (id_alternativa, fk_avaliacao, texto, ordem_alternativa) VALUES
  (1, 1, 'Alternativa A', 1),
  (2, 1, 'Alternativa B', 2),
  (3, 2, 'Alternativa C', 1),
  (4, 2, 'Alternativa D', 2)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Dados iniciais para tabela questao
INSERT INTO questao (id_questao, fk_avaliacao, enunciado, numero_questao, fk_alternativa_correta) VALUES
  (1, 1, 'O que é JVM?', 1, 2),
  (2, 1, 'Qual comando compila um arquivo Java?', 2, 1),
  (3, 2, 'Para que serve o @RestController no Spring?', 1, 3)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), enunciado = VALUES(enunciado), numero_questao = VALUES(numero_questao), fk_alternativa_correta = VALUES(fk_alternativa_correta);

-- Tentativas de avaliação dos novos participantes
-- Ajuste: deixar alguns usuários sem tentativas (sem nota) para teste
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  (1, 11, 1, '2025-03-02 11:00:00', 1),
  (2, 12, 1, '2025-03-03 12:00:00', 1),
  (6, 15, 1, '2025-03-06 16:00:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

-- Tentativas adicionais para usuários que irão responder avaliações (curso 1)
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  (20, 10, 1, '2025-03-10 10:00:00', 1),
  (21, 11, 1, '2025-03-10 11:00:00', 1),
  (22, 12, 1, '2025-03-10 12:00:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

-- Respostas dos usuários nessas tentativas (exemplo)
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (11, 1, 1, 1, 1, 2),
  (12, 1, 2, 1, 2, 1),
  (15, 1, 6, 1, 2, 1)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- Respostas adicionais para garantir que usuários 10,11,12 responderam as 2 questões do curso 1
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (10, 1, 20, 1, 1, 1),
  (10, 1, 20, 1, 2, 1),
  (11, 1, 21, 1, 1, 2),
  (11, 1, 21, 1, 2, 1),
  (12, 1, 22, 1, 1, 1),
  (12, 1, 22, 1, 2, 1),
  (15, 1, 6, 1, 1, 1),
  (15, 1, 6, 1, 2, 1)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);


-- Tentativas históricas
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  (8, 2, 2, '2025-06-02 14:30:00', 2),
  (9, 1, 1, '2025-06-03 09:15:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

-- Dados iniciais para tabela resposta_do_usuario
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (2, 2, 8, 2, 2, 2)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- Feedbacks originais do script
INSERT INTO feedback (fk_usuario, fk_curso, estrelas, motivo) VALUES
(2, 2, 4, 'Muito bom, mas pode melhorar.')
ON DUPLICATE KEY UPDATE estrelas = VALUES(estrelas), motivo = VALUES(motivo);

-- Feedbacks adicionais para o curso 1 (usuários 10..19)
-- Only users who completed all materials (10,11,12,15) should have feedback entries for course 1
INSERT INTO feedback (fk_usuario, fk_curso, estrelas, motivo) VALUES
(10, 1, 5, 'Excelente conteúdo, muito didático.'),
(11, 1, 4, 'Bom curso, alguns pontos rápidos.'),
(12, 1, 3, 'Intermediário, faltaram exercícios.'),
(15, 1, 4, 'Conteúdo relevante, mas poderia ter mais exemplos.')
ON DUPLICATE KEY UPDATE estrelas = VALUES(estrelas), motivo = VALUES(motivo);

-- Remove avaliações (tentativas, respostas e feedback) para usuários que NÃO têm 4 materiais no curso 1
-- Usuários alvo: 1, 13, 14, 16, 17, 19 (mantemos 10,11,12,15 que têm 4 materiais)
-- (Removed deletes per request; inserts for unwanted users were removed instead)