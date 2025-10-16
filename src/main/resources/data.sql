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

-- Adicionar um novo curso e conteúdo para John Doe

-- Curso 5 criado para testes, sem avaliação associada
INSERT INTO curso (id_curso, titulo_curso, descricao, imagem, ocultado, duracao_estimada) VALUES
  (5, 'Python para Iniciantes', 'Curso básico de Python para novos alunos', 'python.jpg', 0, 18)
ON DUPLICATE KEY UPDATE titulo_curso = VALUES(titulo_curso), descricao = VALUES(descricao), imagem = VALUES(imagem), ocultado = VALUES(ocultado), duracao_estimada = VALUES(duracao_estimada);

INSERT INTO curso (id_curso, titulo_curso, descricao, imagem, ocultado, duracao_estimada) VALUES
  (4, 'Node.js Básico', 'Curso introdutório de Node.js', 'nodejs.jpg', 0, 25)
ON DUPLICATE KEY UPDATE titulo_curso = VALUES(titulo_curso), descricao = VALUES(descricao), imagem = VALUES(imagem), ocultado = VALUES(ocultado), duracao_estimada = VALUES(duracao_estimada);

-- Vídeo e apostila de exemplo para o curso 4
INSERT INTO video (id_video, nome_video, descricao_video, url_video, ordem_video, fk_curso) VALUES
  (5, 'Introdução à Regularização Fundiária', 'Visão geral da regularização fundiária: conceitos, objetivos e impacto social.', 'http://exemplo.com/video_regulacao_intro', 1, 4)
ON DUPLICATE KEY UPDATE nome_video = VALUES(nome_video), descricao_video = VALUES(descricao_video), url_video = VALUES(url_video), ordem_video = VALUES(ordem_video), fk_curso = VALUES(fk_curso);

INSERT INTO apostila (id_apostila, nome_apostila_original, nome_apostila_armazenamento, descricao_apostila, tamanho_bytes, is_apostila_oculto, ordem_apostila, fk_curso) VALUES
  (5, 'Guia de Regularização Fundiária - Introdução.pdf', 'regulacao_apostila_intro.pdf', 'Guia introdutório com conceitos e procedimentos iniciais para processos de regularização fundiária.', 512000, 0, 1, 4)
ON DUPLICATE KEY UPDATE nome_apostila_original = VALUES(nome_apostila_original), nome_apostila_armazenamento = VALUES(nome_apostila_armazenamento), descricao_apostila = VALUES(descricao_apostila), tamanho_bytes = VALUES(tamanho_bytes), is_apostila_oculto = VALUES(is_apostila_oculto), ordem_apostila = VALUES(ordem_apostila), fk_curso = VALUES(fk_curso);

-- Matricular John Doe (se existir) no curso 4 e adicionar materiais iniciais
-- (matriculas do John Doe para curso 4 serão adicionadas mais abaixo, depois de garantirmos que o usuário existe)

-- Garantir que avaliações base existem antes de qualquer tentativa
INSERT INTO avaliacao (id_avaliacao, nota_minima, fk_curso) VALUES
  (1, 7.5, 1),
  (2, 6.0, 2),
  (3, 8.0, 3),
  (4, 7.0, 4)
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
    id_usuario,
    nome,
    cpf,
    email,
    senha,
    fk_cargo,
    data_entrada,
    ultimo_acesso
) VALUES
(20, 'John Doe', '12345678900', 'john@doe.com', '$2a$10$QQPobUtOp3Gwh3P94Itu0u/e3jGNDRt6WHhIqz2TdDFpXaK6y6lw6', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
  nome = VALUES(nome),
  cpf = VALUES(cpf),
  email = VALUES(email),
  senha = VALUES(senha),
  fk_cargo = VALUES(fk_cargo),
  data_entrada = VALUES(data_entrada),
  ultimo_acesso = VALUES(ultimo_acesso);

-- Agora que John Doe está garantido, matriculamos no curso 4 e adicionamos material
INSERT INTO matricula (fk_usuario, fk_curso, fk_inicio, ultimo_senso, completo, data_finalizado)
  SELECT id_usuario, 4, '2025-04-01 08:00:00', '2025-04-01 08:00:00', 0, NULL FROM usuario WHERE email = 'john@doe.com'
ON DUPLICATE KEY UPDATE fk_inicio = VALUES(fk_inicio), ultimo_senso = VALUES(ultimo_senso), completo = VALUES(completo), data_finalizado = VALUES(data_finalizado);

INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso)
  SELECT id_usuario, 4, 5, 5, 0, NULL FROM usuario WHERE email = 'john@doe.com'
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

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

select * from tentativa
where fk_usuario = 1;
SELECT * FROM resposta_do_usuario WHERE fk_usuario = 20;

-- Garantir matrículas de John Doe (id 20) em todos os cursos necessários
INSERT INTO matricula (fk_usuario, fk_curso, fk_inicio, ultimo_senso, completo, data_finalizado) VALUES
  (20, 2, '2025-03-01 08:00:00', '2025-03-15 10:00:00', 1, '2025-03-20 10:00:00'),
  (20, 3, '2025-04-01 08:00:00', '2025-04-15 10:00:00', 1, '2025-04-20 10:00:00')
ON DUPLICATE KEY UPDATE
  fk_inicio = VALUES(fk_inicio),
  ultimo_senso = VALUES(ultimo_senso),
  completo = VALUES(completo),
  data_finalizado = VALUES(data_finalizado);

-- Inserir mais 5 tentativas para John Doe (usuario 20) com cursos variados
-- (As respostas serão inseridas depois da criação das questões)
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  (40, 20, 1, '2025-07-01 10:00:00', 1),
  (41, 20, 2, '2025-07-02 11:00:00', 2),
  (42, 20, 3, '2025-07-03 12:00:00', 3),
  (43, 20, 4, '2025-07-04 13:00:00', 4),
  (44, 20, 1, '2025-06-30 09:00:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

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

-- Garantir que John Doe (se existir) tem matrícula no curso 1 para que inserts posteriores em material_aluno e tentativa não quebrem FKs
INSERT INTO matricula (fk_usuario, fk_curso, fk_inicio, ultimo_senso, completo, data_finalizado)
  SELECT id_usuario, 1, '2025-02-01 08:00:00', '2025-03-05 14:20:00', 1, '2025-03-05 14:20:00' FROM usuario WHERE email = 'john@doe.com'
ON DUPLICATE KEY UPDATE fk_inicio = VALUES(fk_inicio), ultimo_senso = VALUES(ultimo_senso), completo = VALUES(completo), data_finalizado = VALUES(data_finalizado);



-- Dados iniciais para tabela video (focado em Regularização Fundiária)
INSERT INTO video (id_video, nome_video, descricao_video, url_video, ordem_video, fk_curso) VALUES
  (1, 'Introdução à Regularização Fundiária', 'Visão geral e contexto histórico da regularização fundiária no Brasil.', 'https://www.youtube.com/watch?v=-jkPxfTDeHw&pp=ygUZcmVndWxhcml6YcOnw6NvIGZ1bmRpYXJpYQ%3D%3D', 1, 1),
  (2, 'Marco Legal e Políticas Públicas', 'Principais leis, políticas e instrumentos legais relacionados à regularização fundiária.', 'http://exemplo.com/video_regulacao_legal', 2, 1),
  (3, 'Procedimentos Técnicos e Sociais', 'Etapas operacionais e participação social em processos de regularização fundiária.', 'http://exemplo.com/video_regulacao_procedimentos', 3, 1)
ON DUPLICATE KEY UPDATE nome_video = VALUES(nome_video), descricao_video = VALUES(descricao_video), url_video = VALUES(url_video), ordem_video = VALUES(ordem_video), fk_curso = VALUES(fk_curso);

-- Adiciona um vídeo extra para o curso 1 (conteúdo prático)
INSERT INTO video (id_video, nome_video, descricao_video, url_video, ordem_video, fk_curso) VALUES
  (4, 'Boas Práticas para Levantamento de Dados', 'Técnicas e checklist para levantamento de dados em campo e cadastro de áreas.', 'http://exemplo.com/video_regulacao_boaspraticas', 4, 1)
ON DUPLICATE KEY UPDATE nome_video = VALUES(nome_video), descricao_video = VALUES(descricao_video), url_video = VALUES(url_video), ordem_video = VALUES(ordem_video), fk_curso = VALUES(fk_curso);

-- Dados iniciais para tabela apostila (focado em Regularização Fundiária)
INSERT INTO apostila (id_apostila, nome_apostila_original, nome_apostila_armazenamento, descricao_apostila, tamanho_bytes, is_apostila_oculto, ordem_apostila, fk_curso) VALUES
  (1, 'Manual de Introdução à Regularização Fundiária.pdf', 'regulacao_manual_intro.pdf', 'Manual com conceitos básicos e panorama geral da regularização fundiária.', 225252, 0, 1, 1),
  (2, 'Guia Prático de Instrumentos Jurídicos.pdf', 'regulacao_guia_instrumentos.pdf', 'Guia sobre instrumentos legais e procedimentos administrativos.', 158000, 0, 2, 1),
  (3, 'Modelos e Formulários para Cadastro.pdf', 'regulacao_modelos_formularios.pdf', 'Modelos de formulários e planilhas para apoio ao processo de cadastro de beneficiários.', 2542, 0, 3, 1)
ON DUPLICATE KEY UPDATE nome_apostila_original = VALUES(nome_apostila_original), nome_apostila_armazenamento = VALUES(nome_apostila_armazenamento), descricao_apostila = VALUES(descricao_apostila), tamanho_bytes = VALUES(tamanho_bytes), is_apostila_oculto = VALUES(is_apostila_oculto), ordem_apostila = VALUES(ordem_apostila), fk_curso = VALUES(fk_curso);

-- Adiciona uma apostila extra para o curso 1 (material complementar)
INSERT INTO apostila (id_apostila, nome_apostila_original, nome_apostila_armazenamento, descricao_apostila, tamanho_bytes, is_apostila_oculto, ordem_apostila, fk_curso) VALUES
  (4, 'Participação Social e Comunicação.pdf', 'regulacao_participacao.pdf', 'Recursos e orientações para mobilização social e comunicação em processos de regularização.', 102400, 0, 4, 1)
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
(1, 1, 1, 1, 0, '2025-01-22 06:00:00'),
(2, 2, 2, 2, 1, '2025-01-18 09:30:00'),
(2, 2, 3, 3, 0, NULL)
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

-- Dados iniciais para tabela questao (ANTES das alternativas para que a FK funcione)
INSERT INTO questao (id_questao, fk_avaliacao, enunciado, numero_questao, fk_alternativa_correta) VALUES
  (1, 1, 'O que é Regularização Fundiária Urbana (Reurb)?', 1, NULL),
  (2, 1, 'Qual é o principal objetivo da Reurb?', 2, NULL),
  (3, 2, 'Para que serve o @RestController no Spring?', 1, NULL),
  (4, 3, 'Qual comando SQL consulta dados?', 1, NULL),
  (5, 4, 'Como instalar dependências no Node.js?', 1, NULL)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), enunciado = VALUES(enunciado), numero_questao = VALUES(numero_questao);

-- Questões adicionais para a avaliação 1 para totalizar 10 questões (IDs 6..13)
INSERT INTO questao (id_questao, fk_avaliacao, enunciado, numero_questao, fk_alternativa_correta) VALUES
  (6, 1, 'O que é o Cadastro Social no processo de Reurb?', 3, NULL),
  (7, 1, 'Qual documento delimita a área e os lotes a serem regularizados?', 4, NULL),
  (8, 1, 'Qual instrumento titula o direito real ao morador na Reurb-S?', 5, NULL),
  (9, 1, 'Quem é o ente competente para instaurar o procedimento de Reurb?', 6, NULL),
  (10, 1, 'Qual etapa verifica a conformidade ambiental e urbanística?', 7, NULL),
  (11, 1, 'Qual cartório pratica os atos de registro da Reurb?', 8, NULL),
  (12, 1, 'Na Reurb-S, quais taxas podem ser isentadas para famílias de baixa renda?', 9, NULL),
  (13, 1, 'Qual participação social é recomendada durante a Reurb?', 10, NULL)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), enunciado = VALUES(enunciado), numero_questao = VALUES(numero_questao);

-- Dados iniciais para tabela alternativa (com fk_questao e IDs únicos)
-- Questão 1 (avaliação 1): Reurb - conceito
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (1, 1, 1, 'Processo de despejo coletivo', 1),
  (2, 1, 1, 'Medidas para integrar assent. ao sistema formal', 2)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (11, 1, 1, 'Apenas registro cartorial', 3),
  (12, 1, 1, 'Apenas construção de moradias', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 2 (avaliação 1): objetivo da Reurb
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (3, 1, 2, 'Integrar assentamentos ao ordenamento formal', 1),
  (4, 1, 2, 'Punir ocupações irregulares', 2)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (13, 1, 2, 'Privatizar áreas públicas', 3),
  (14, 1, 2, 'Aumentar valor venal', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questões 6..13 (avaliação 1): inserir 4 alternativas por questão (Reurb)
-- Questão 6: Cadastro Social
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (15, 1, 6, 'Levantamento socioeconômico das famílias', 1),
  (16, 1, 6, 'Perícia judicial', 2),
  (17, 1, 6, 'Registro de hipotecas', 3),
  (18, 1, 6, 'Projeto de drenagem', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 7: Documento que delimita área e lotes
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (19, 1, 7, 'Projeto de Regularização Fundiária (PRF)', 1),
  (20, 1, 7, 'Contrato de compra e venda', 2),
  (21, 1, 7, 'Habite-se', 3),
  (22, 1, 7, 'Carnê de IPTU', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 8: Instrumento de titulação na Reurb-S
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (23, 1, 8, 'Título de legitimação fundiária', 1),
  (24, 1, 8, 'Contrato de locação', 2),
  (25, 1, 8, 'Termo de guarda', 3),
  (26, 1, 8, 'Escritura de doação simples', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 9: Ente competente para instaurar Reurb
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (27, 1, 9, 'Município', 1),
  (28, 1, 9, 'Cartório de Registro de Imóveis', 2),
  (29, 1, 9, 'União', 3),
  (30, 1, 9, 'Condomínio', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 10: Etapa de verificação de conformidade
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (31, 1, 10, 'Análise técnica do PRF pelos órgãos', 1),
  (32, 1, 10, 'Emissão do carnê de IPTU', 2),
  (33, 1, 10, 'Audiência pública exclusiva', 3),
  (34, 1, 10, 'Registro de contrato particular', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 11: Cartório competente
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (35, 1, 11, 'Cartório de Registro de Imóveis', 1),
  (36, 1, 11, 'Cartório de Notas', 2),
  (37, 1, 11, 'Cartório de Protesto', 3),
  (38, 1, 11, 'Cartório Eleitoral', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 12: Isenções na Reurb-S
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (39, 1, 12, 'Custas e emolumentos de registro', 1),
  (40, 1, 12, 'Tarifa de água', 2),
  (41, 1, 12, 'Multa de trânsito', 3),
  (42, 1, 12, 'Taxa de lixo', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 13: Participação social na Reurb
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (43, 1, 13, 'Reuniões e audiências com a comunidade', 1),
  (44, 1, 13, 'Somente comunicação por edital', 2),
  (45, 1, 13, 'Apenas informativo online', 3),
  (46, 1, 13, 'Sem participação', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 3 (avaliação 2): Para que serve o @RestController no Spring?
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (5, 2, 3, 'Para definir controllers REST', 1),
  (6, 2, 3, 'Para conectar ao banco de dados', 2)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 4 (avaliação 3): Qual comando SQL consulta dados?
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (7, 3, 4, 'SELECT * FROM tabela', 1),
  (8, 3, 4, 'INSERT INTO tabela', 2)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Questão 5 (avaliação 4): Como instalar dependências no Node.js?
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (9, 4, 5, 'npm install', 1),
  (10, 4, 5, 'npm start', 2)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Atualizar questões com as alternativas corretas
UPDATE questao SET fk_alternativa_correta = 2 WHERE id_questao = 1;  -- Reurb: conceito (integração ao ordenamento)
UPDATE questao SET fk_alternativa_correta = 3 WHERE id_questao = 2;  -- Reurb: objetivo (integrar assentamentos)
UPDATE questao SET fk_alternativa_correta = 5 WHERE id_questao = 3;  -- "Para definir controllers REST"
UPDATE questao SET fk_alternativa_correta = 7 WHERE id_questao = 4;  -- "SELECT * FROM tabela"
UPDATE questao SET fk_alternativa_correta = 9 WHERE id_questao = 5;  -- "npm install"

-- Define alternativas corretas para as novas questões da avaliação 1 (Reurb)
UPDATE questao SET fk_alternativa_correta = 15 WHERE id_questao = 6;   -- Cadastro Social
UPDATE questao SET fk_alternativa_correta = 19 WHERE id_questao = 7;   -- PRF delimita área/lotes
UPDATE questao SET fk_alternativa_correta = 23 WHERE id_questao = 8;   -- Título de legitimação fundiária
UPDATE questao SET fk_alternativa_correta = 27 WHERE id_questao = 9;   -- Município
UPDATE questao SET fk_alternativa_correta = 31 WHERE id_questao = 10;  -- Análise técnica do PRF
UPDATE questao SET fk_alternativa_correta = 35 WHERE id_questao = 11;  -- CRI (Registro de Imóveis)
UPDATE questao SET fk_alternativa_correta = 39 WHERE id_questao = 12;  -- Isenção de custas/emolumentos
UPDATE questao SET fk_alternativa_correta = 43 WHERE id_questao = 13;  -- Reuniões/audiências com comunidade

-- Inserir respostas de exemplo para as tentativas de John Doe (para calcular notas)
-- IMPORTANTE: Estas inserções devem vir DEPOIS da criação das questões e alternativas

-- Tentativa 40 (curso 1, avaliação 1): responde questões 1 e 2 corretamente (2/2)
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (20, 1, 40, 1, 1, 2),  -- questão 1, alternativa correta (conceito Reurb)
  (20, 1, 40, 1, 2, 3)   -- questão 2, alternativa correta (objetivo Reurb)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- Complemento da tentativa 40: adiciona respostas para as novas questões (6..13) com acerto total de 6/10 em avaliação 1
-- Correções adicionais: acerta Q6, Q7, Q8, Q9 (4 acertos); erra Q10, Q11, Q12, Q13 (4 erros)
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (20, 1, 40, 1, 6, 15),  -- correto
  (20, 1, 40, 1, 7, 19),  -- correto
  (20, 1, 40, 1, 8, 23),  -- correto
  (20, 1, 40, 1, 9, 27),  -- correto
  (20, 1, 40, 1, 10, 32), -- errado (correta é 31)
  (20, 1, 40, 1, 11, 36), -- errado (correta é 35)
  (20, 1, 40, 1, 12, 40), -- errado (correta é 39)
  (20, 1, 40, 1, 13, 44)  -- errado (correta é 43)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- Tentativa 41 (curso 2, avaliação 2): responde questão 3 corretamente (1/1)
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (20, 2, 41, 2, 3, 5)   -- questão 3, alternativa correta "Para definir controllers REST" (id 5)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- Tentativa 42 (curso 3, avaliação 3): sem respostas (para teste de "Sem respostas" - 0/0)

-- Tentativa 43 (curso 4, avaliação 4): responde questão 5 incorretamente (0/1)
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (20, 4, 43, 4, 5, 10)  -- questão 5, alternativa incorreta "npm start" (id 10, correta seria id 9)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- Tentativa 44 (curso 1, avaliação 1): responde apenas questão 1 corretamente (1/2)
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (20, 1, 44, 1, 1, 2)   -- questão 1, alternativa correta "Java Virtual Machine" (id 2)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

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

-- Bloco adicional para popular engajamento (material_aluno + tentativa) com mais datas
-- Gera atividade espalhada para os usuários do curso 1 entre 2025-02-01 e 2025-03-15
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  (10,1,1,NULL,1,'2025-02-01 09:00:00'),
  (10,1,NULL,1,1,'2025-02-02 10:00:00'),
  (10,1,4,NULL,1,'2025-02-08 11:00:00'),
  (10,1,NULL,4,1,'2025-02-18 12:00:00'),

  (11,1,1,NULL,1,'2025-02-02 09:30:00'),
  (11,1,NULL,1,1,'2025-02-04 10:15:00'),
  (11,1,4,NULL,1,'2025-02-12 10:05:00'),
  (11,1,NULL,4,1,'2025-03-01 11:05:00'),

  (12,1,1,NULL,1,'2025-02-03 11:20:00'),
  (12,1,NULL,1,1,'2025-02-06 11:20:00'),
  (12,1,4,NULL,1,'2025-02-16 10:10:00'),
  (12,1,NULL,4,1,'2025-03-11 11:10:00'),

  (13,1,1,NULL,0,NULL),
  (13,1,NULL,1,0,NULL),

  (14,1,1,NULL,1,'2025-02-05 13:10:00'),
  (14,1,NULL,1,0,NULL),

  (15,1,1,NULL,1,'2025-02-06 14:25:00'),
  (15,1,NULL,1,1,'2025-02-07 14:25:00'),
  (15,1,4,NULL,1,'2025-03-11 10:20:00'),

  (16,1,1,NULL,0,NULL),
  (16,1,NULL,1,0,NULL),

  (17,1,1,NULL,1,'2025-02-08 15:55:00'),
  (17,1,NULL,1,1,'2025-02-09 16:00:00'),

  (19,1,1,NULL,0,'2025-02-10 09:45:00'),
  (19,1,NULL,1,0,'2025-02-11 09:45:00')
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

-- Tentativas adicionais distribuídas
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  (30, 10, 1, '2025-02-01 10:30:00', 1),
  (31, 10, 1, '2025-02-08 12:00:00', 1),
  (32, 11, 1, '2025-02-04 11:00:00', 1),
  (33, 11, 1, '2025-02-12 11:30:00', 1),
  (34, 12, 1, '2025-02-06 12:40:00', 1),
  (35, 15, 1, '2025-02-07 15:00:00', 1),
  (36, 15, 1, '2025-03-10 09:50:00', 1),
  (37, 19, 1, '2025-02-11 10:00:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

-- Duas tentativas adicionais para John Doe (usuário com email john@doe.com / id esperado 20)
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  (38, 20, 1, '2025-06-03 06:15:00', 1),
  (39, 20, 1, '2025-06-04 07:20:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

-- Ensure composite index exists for (fk_curso, fk_usuario, id_tentativa) to allow
-- composite foreign keys referencing these columns (some DBs require an index on
-- referenced columns in the same order).
ALTER TABLE tentativa ADD UNIQUE INDEX IF NOT EXISTS ux_tentativa_comp (fk_curso, fk_usuario, id_tentativa);

-- Inserir atividades para John Doe (referenciado pelo email john@doe.com)
-- MaterialAluno: apenas insere se o usuário existir
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso)
  SELECT id_usuario, 1, 1, NULL, 1, '2025-02-03 09:30:00' FROM usuario WHERE email = 'john@doe.com'
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);


select * from video;
SELECT id_material_aluno, fk_video, fk_apostila, finalizada, ultimo_acesso, fk_curso, fk_usuario FROM material_aluno WHERE fk_usuario=1 AND fk_curso=1 ORDER BY id_material_aluno;

SELECT id_questao, fk_alternativa_correta FROM questao WHERE fk_avaliacao = 1;
SELECT * FROM avaliacao WHERE fk_curso = 1;