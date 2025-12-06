-- Garantir que usuários base (id 1, 2, 3) existem

-- ordem_curso já definida no CREATE TABLE em Database/Script.sql (removido ALTER e backfill)

INSERT INTO cargo (id_cargo, nome_cargo) VALUES
  (1, 'Funcionário'),
  (2, 'Colaborador')
ON DUPLICATE KEY UPDATE nome_cargo = VALUES(nome_cargo);

-- Garantir que cursos base existem antes das avaliações (contexto: Regularização Fundiária)
INSERT INTO curso (id_curso, titulo_curso, descricao, imagem, ocultado, duracao_estimada, ordem_curso) VALUES
  (1, 'Regularização Fundiária: Fundamentos', 'Conceitos, etapas e objetivos da Reurb (S/E).', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=800&q=80', 0, 20, 1),
  (2, 'Legislação Urbana e Instrumentos da Reurb', 'Lei 13.465/2017, Decreto 9.310/2018, modalidades Reurb-S/Reurb-E e instrumentos jurídicos.', 'https://images.unsplash.com/photo-1450101499163-c8848c66ca85?w=800&q=80', 0, 40, 2),
  (3, 'Diagnóstico Socioeconômico e Cadastro Social', 'Coleta, análise e gestão de dados para apoiar a regularização fundiária.', 'https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=800&q=80', 0, 30, 3)
ON DUPLICATE KEY UPDATE titulo_curso = VALUES(titulo_curso), descricao = VALUES(descricao), imagem = VALUES(imagem), ocultado = VALUES(ocultado), duracao_estimada = VALUES(duracao_estimada), ordem_curso = VALUES(ordem_curso);

-- Adicionar um novo curso e conteúdo para John Doe

-- Curso 5 criado para testes, sem avaliação associada (contexto Reurb)
INSERT INTO curso (id_curso, titulo_curso, descricao, imagem, ocultado, duracao_estimada, ordem_curso) VALUES
  (5, 'Comunicação e Participação Social na Reurb', 'Mobilização comunitária, comunicação e engajamento social em projetos de regularização fundiária.', 'https://images.unsplash.com/photo-1531545514256-b1400bc00f31?w=800&q=80', 0, 18, 5)
ON DUPLICATE KEY UPDATE titulo_curso = VALUES(titulo_curso), descricao = VALUES(descricao), imagem = VALUES(imagem), ocultado = VALUES(ocultado), duracao_estimada = VALUES(duracao_estimada), ordem_curso = VALUES(ordem_curso);

INSERT INTO curso (id_curso, titulo_curso, descricao, imagem, ocultado, duracao_estimada, ordem_curso) VALUES
  (4, 'Procedimentos Técnicos e Documentais na Reurb', 'Levantamentos, plantas, memoriais descritivos e fluxo documental dos processos de regularização.', 'https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?w=800&q=80', 0, 25, 4)
ON DUPLICATE KEY UPDATE titulo_curso = VALUES(titulo_curso), descricao = VALUES(descricao), imagem = VALUES(imagem), ocultado = VALUES(ocultado), duracao_estimada = VALUES(duracao_estimada), ordem_curso = VALUES(ordem_curso);

-- Curso 6: Gestão de Projetos Urbanos
INSERT INTO curso (id_curso, titulo_curso, descricao, imagem, ocultado, duracao_estimada, ordem_curso) VALUES
  (6, 'Gestão de Projetos Urbanos', 'Planejamento e execução de projetos no contexto urbano.', 'https://images.unsplash.com/photo-1503387762-592deb58ef4e?w=800&q=80', 0, 24, 6)
ON DUPLICATE KEY UPDATE titulo_curso = VALUES(titulo_curso), descricao = VALUES(descricao), imagem = VALUES(imagem), ocultado = VALUES(ocultado), duracao_estimada = VALUES(duracao_estimada), ordem_curso = VALUES(ordem_curso);

-- Vídeo e apostila de exemplo para o curso 4
INSERT INTO video (id_video, nome_video, descricao_video, url_video, ordem_video, fk_curso) VALUES
  (5, 'Introdução à Regularização Fundiária', 'Visão geral da regularização fundiária: conceitos, objetivos e impacto social.', 'http://exemplo.com/video_regulacao_intro', 1, 4)
ON DUPLICATE KEY UPDATE nome_video = VALUES(nome_video), descricao_video = VALUES(descricao_video), url_video = VALUES(url_video), ordem_video = VALUES(ordem_video), fk_curso = VALUES(fk_curso);

INSERT INTO apostila (id_apostila, nome_apostila_original, nome_apostila_armazenamento, descricao_apostila, tamanho_bytes, is_apostila_oculto, ordem_apostila, fk_curso) VALUES
  (5, 'Guia de Regularização Fundiária - Introdução.pdf', 'regulacao_apostila_intro.pdf', 'Guia introdutório com conceitos e procedimentos iniciais para processos de regularização fundiária.', 512000, 0, 1, 4)
ON DUPLICATE KEY UPDATE nome_apostila_original = VALUES(nome_apostila_original), nome_apostila_armazenamento = VALUES(nome_apostila_armazenamento), descricao_apostila = VALUES(descricao_apostila), tamanho_bytes = VALUES(tamanho_bytes), is_apostila_oculto = VALUES(is_apostila_oculto), ordem_apostila = VALUES(ordem_apostila), fk_curso = VALUES(fk_curso);

-- Vincula URL local para a apostila do curso 4 (id_apostila=5)
UPDATE apostila SET url_arquivo = '/uploads/regulacao_apostila_intro.pdf' WHERE id_apostila = 5;

-- Matricular John Doe (se existir) no curso 4 e adicionar materiais iniciais
-- (matriculas do John Doe para curso 4 serão adicionadas mais abaixo, depois de garantirmos que o usuário existe)

-- Garantir que avaliações base existem antes de qualquer tentativa
INSERT INTO avaliacao (id_avaliacao, nota_minima, fk_curso) VALUES
  (1, 7.5, 1),
  (2, 6.0, 2),
  (3, 8.0, 3),
  (4, 7.0, 4)
ON DUPLICATE KEY UPDATE nota_minima = VALUES(nota_minima), fk_curso = VALUES(fk_curso);

INSERT INTO usuario (id_usuario, nome, cpf, email, senha, fk_cargo, telefone, foto_url, data_entrada, ultimo_acesso) VALUES
  (1, 'João Silva', '12345678901', 'joao@email.com', 'senha123', 1, '(11) 99999-0001', NULL, '2025-01-01 08:00:00', '2025-10-20 08:00:00'),
  (2, 'Maria Souza', '98765432109', 'maria@email.com', 'senha456', 2, '(11) 99999-0002', NULL, '2025-01-02 08:00:00', '2025-10-15 10:00:00'),
  (3, 'Carlos Oliveira', '11122233344', 'carlos@email.com', 'senha789', 1, '(11) 99999-0003', NULL, '2025-01-03 08:00:00', '2025-10-25 12:00:00')
ON DUPLICATE KEY UPDATE nome = VALUES(nome), cpf = VALUES(cpf), email = VALUES(email), senha = VALUES(senha), fk_cargo = VALUES(fk_cargo), telefone = VALUES(telefone), foto_url = VALUES(foto_url), data_entrada = VALUES(data_entrada), ultimo_acesso = VALUES(ultimo_acesso);
-- Dados adicionais de usuario (id 10 a 19) para testes variados
INSERT INTO usuario (id_usuario, nome, cpf, email, senha, fk_cargo, telefone, foto_url, data_entrada, ultimo_acesso) VALUES
  (10, 'Ana Lima', '20000000001', 'ana.lima@example.com', 'senhaAna1', 1, NULL, NULL, '2025-01-10 08:00:00', '2025-12-03 10:00:00'),
  (11, 'Bruno Costa', '20000000002', 'bruno.costa@example.com', 'senhaBruno2', 1, NULL, NULL, '2025-01-11 08:00:00', '2025-12-02 11:00:00'),
  (12, 'Carla Nunes', '20000000003', 'carla.nunes@example.com', 'senhaCarla3', 2, NULL, NULL, '2025-01-12 08:00:00', '2025-12-03 09:00:00'),
  (13, 'Diego Alves', '20000000004', 'diego.alves@example.com', 'senhaDiego4', 1, NULL, NULL, '2025-01-13 08:00:00', '2025-11-01 10:00:00'),
  (14, 'Elisa Prado', '20000000005', 'elisa.prado@example.com', 'senhaElisa5', 2, NULL, NULL, '2025-01-14 08:00:00', '2025-11-05 14:00:00'),
  (15, 'Felipe Rocha', '20000000006', 'felipe.rocha@example.com', 'senhaFelipe6', 1, NULL, NULL, '2025-01-15 08:00:00', '2025-12-01 14:00:00'),
  (16, 'Gabriela Moraes', '20000000007', 'gabriela.moraes@example.com', 'senhaGabriela7', 2, NULL, NULL, '2025-01-16 08:00:00', '2025-11-08 09:00:00'),
  (17, 'Hugo Pereira', '20000000008', 'hugo.pereira@example.com', 'senhaHugo8', 1, NULL, NULL, '2025-01-17 08:00:00', '2025-11-30 08:00:00'),
  (18, 'Isabela Santos', '20000000009', 'isabela.santos@example.com', 'senhaIsabela9', 2, NULL, NULL, '2025-01-18 08:00:00', '2025-11-10 09:00:00'),
  (19, 'João Pedro', '20000000010', 'joao.pedro@example.com', 'senhaJoao10', 1, NULL, NULL, '2025-01-19 08:00:00', '2025-12-02 10:30:00')
ON DUPLICATE KEY UPDATE nome = VALUES(nome), cpf = VALUES(cpf), email = VALUES(email), senha = VALUES(senha), fk_cargo = VALUES(fk_cargo), telefone = VALUES(telefone), foto_url = VALUES(foto_url), data_entrada = VALUES(data_entrada), ultimo_acesso = VALUES(ultimo_acesso);




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
(20, 'John Doe', '12345678900', 'john@doe.com', '$2a$10$QQPobUtOp3Gwh3P94Itu0u/e3jGNDRt6WHhIqz2TdDFpXaK6y6lw6', 1, '2025-01-20 08:00:00', '2025-12-03 11:00:00')
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

-- Em material_aluno, cada linha deve referenciar somente um tipo de material (vídeo OU apostila)
-- Vincula John Doe (curso 4) separando vídeo (id 5) e apostila (id 5)
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso)
  SELECT id_usuario, 4, 5, NULL, 0, NULL FROM usuario WHERE email = 'john@doe.com'
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso)
  SELECT id_usuario, 4, NULL, 5, 0, NULL FROM usuario WHERE email = 'john@doe.com'
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

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
(4, 'Cintia Tanivaro', '98765432100', 'cintia@tanivaro.com', '123123', 2, '2025-01-04 08:00:00', '2025-11-15 10:00:00')
ON DUPLICATE KEY UPDATE
  nome = VALUES(nome),
  cpf = VALUES(cpf),
  email = VALUES(email),
  senha = VALUES(senha),
  fk_cargo = VALUES(fk_cargo),
  data_entrada = VALUES(data_entrada),
  ultimo_acesso = VALUES(ultimo_acesso);

-- Adiciona usuário Alexandre (pedido: alexandre.nichiojr@sptech.school)
INSERT INTO usuario (id_usuario, nome, cpf, email, senha, fk_cargo, telefone, foto_url, data_entrada, ultimo_acesso) VALUES
  (21, 'Alexandre Nichio Jr', '30000000001', 'alexandre.nichiojr@sptech.school', 'senhaAlex', 2, NULL, NULL, '2025-02-01 08:00:00', '2025-11-10 09:00:00')
ON DUPLICATE KEY UPDATE nome = VALUES(nome), cpf = VALUES(cpf), email = VALUES(email), senha = VALUES(senha), fk_cargo = VALUES(fk_cargo), telefone = VALUES(telefone), foto_url = VALUES(foto_url), data_entrada = VALUES(data_entrada), ultimo_acesso = VALUES(ultimo_acesso);

-- Adiciona usuário Giorgio (pedido: giorgio.antunes@sptech.school)
INSERT INTO usuario (id_usuario, nome, cpf, email, senha, fk_cargo, telefone, foto_url, data_entrada, ultimo_acesso) VALUES
  (22, 'Giorgio Antunes', '30000000002', 'giorgio.antunes@sptech.school', 'senhaGiorgio', 1, NULL, NULL, '2025-02-01 08:00:00', '2025-11-12 14:00:00')
ON DUPLICATE KEY UPDATE nome = VALUES(nome), cpf = VALUES(cpf), email = VALUES(email), senha = VALUES(senha), fk_cargo = VALUES(fk_cargo), telefone = VALUES(telefone), foto_url = VALUES(foto_url), data_entrada = VALUES(data_entrada), ultimo_acesso = VALUES(ultimo_acesso);

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

-- Garantir que Alexandre (id 21) e Giorgio (id 22) estão matriculados no curso 1 para receber notificações quando o curso for publicado
INSERT INTO matricula (fk_usuario, fk_curso, fk_inicio, ultimo_senso, completo, data_finalizado) VALUES
  (21, 1, '2025-02-01 08:00:00', '2025-03-01 10:00:00', 0, NULL),
  (22, 1, '2025-02-01 08:00:00', '2025-03-01 10:00:00', 0, NULL)
ON DUPLICATE KEY UPDATE fk_inicio = VALUES(fk_inicio), ultimo_senso = VALUES(ultimo_senso), completo = VALUES(completo), data_finalizado = VALUES(data_finalizado);



-- Dados iniciais para tabela video (focado em Regularização Fundiária)
INSERT INTO video (id_video, nome_video, descricao_video, url_video, ordem_video, fk_curso) VALUES
  (1, 'Introdução à Regularização Fundiária', 'Visão geral e contexto histórico da regularização fundiária no Brasil.', 'https://www.youtube.com/watch?v=-jkPxfTDeHw&pp=ygUZcmVndWxhcml6YcOnw6NvIGZ1bmRpYQ%3D%3D', 1, 1),
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

-- Vincula URLs locais para apostilas do curso 1 (ids 1..4)
UPDATE apostila SET url_arquivo = '/uploads/reurb_intro.pdf' WHERE id_apostila = 1;
UPDATE apostila SET url_arquivo = '/uploads/reurb_instrumentos.pdf' WHERE id_apostila = 2;
UPDATE apostila SET url_arquivo = '/uploads/reurb_modelos.pdf' WHERE id_apostila = 3;
UPDATE apostila SET url_arquivo = '/uploads/reurb_participacao.pdf' WHERE id_apostila = 4;

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
-- Corrige duplicidades: não vincular vídeo e apostila na mesma linha
-- Para (1,1): cria duas linhas, uma para vídeo=1 e outra para apostila=1
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
(1, 1, 1, NULL, 0, '2025-01-22 06:00:00'),
(1, 1, NULL, 1, 0, '2025-01-22 06:00:00')
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);
-- Para (2,2): cria quatro linhas separadas (vídeo 2, apostila 2) e (vídeo 3, apostila 3)
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
(2, 2, 2, NULL, 1, '2025-01-18 09:30:00'),
(2, 2, NULL, 2, 1, '2025-01-18 09:30:00'),
(2, 2, 3, NULL, 0, NULL),
(2, 2, NULL, 3, 0, NULL)
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

-- -----------------------------------------------------------------------------
-- Additional distinct questions for evaluations 2, 3 and 4
-- Goal: provide separate question sets (10 questions each) for each evaluation
-- without changing existing question IDs used above (we append new IDs)
-- Evaluation 2 (fk_avaliacao = 2) - Course 2 (Legislação Urbana e Instrumentos da Reurb)
INSERT INTO questao (id_questao, fk_avaliacao, enunciado, numero_questao, fk_alternativa_correta) VALUES
  (101, 2, 'Qual é a lei federal que instituiu a Reurb?', 1, NULL),
  (102, 2, 'O que caracteriza a modalidade Reurb-S?', 2, NULL),
  (103, 2, 'Qual decreto regulamentou aspectos da Reurb em âmbito federal?', 3, NULL),
  (104, 2, 'Que instrumento jurídico pode regularizar a ocupação em áreas urbanas?', 4, NULL),
  (105, 2, 'Quando a Reurb pode dispensar regularização registral imediata?', 5, NULL),
  (106, 2, 'Qual é o papel do cadastro social na Reurb?', 6, NULL),
  (107, 2, 'Qual órgão municipal normalmente conduz a Reurb?', 7, NULL),
  (108, 2, 'O que é titulação fundiária na Reurb?', 8, NULL),
  (109, 2, 'Qual a função do decreto na implementação da Reurb?', 9, NULL),
  (110, 2, 'Qual é a principal diferença entre Reurb-S e Reurb-E?', 10, NULL)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), enunciado = VALUES(enunciado), numero_questao = VALUES(numero_questao), fk_alternativa_correta = VALUES(fk_alternativa_correta);

-- Alternatives for evaluation 2 questions (ids 1001..1040)
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (1001, 2, 101, 'Lei 13.465/2017', 1),
  (1002, 2, 101, 'Lei 8.666/1993', 2),
  (1003, 2, 101, 'Decreto 9.310/2018', 3),
  (1004, 2, 101, 'Constituição Federal', 4),

  (1005, 2, 102, 'Regularização administrativa simplificada', 1),
  (1006, 2, 102, 'Apenas medidas judiciais', 2),
  (1007, 2, 102, 'Exclusivamente contratos privados', 3),
  (1008, 2, 102, 'Medida de expulsão', 4),

  (1009, 2, 103, 'Decreto 9.310/2018', 1),
  (1010, 2, 103, 'Portaria 123/2020', 2),
  (1011, 2, 103, 'Lei 13.465/2017', 3),
  (1012, 2, 103, 'Instrução normativa municipal', 4),

  (1013, 2, 104, 'Usucapião coletivo', 1),
  (1014, 2, 104, 'Contrato de prestação de serviços', 2),
  (1015, 2, 104, 'Licença ambiental', 3),
  (1016, 2, 104, 'Protocolo administrativo', 4),

  (1017, 2, 105, 'Quando há situação de interesse social reconhecido', 1),
  (1018, 2, 105, 'Nunca', 2),
  (1019, 2, 105, 'Sempre que houver pendências registrárias', 3),
  (1020, 2, 105, 'Somente por decisão judicial', 4),

  (1021, 2, 106, 'Identificar beneficiários e sua situação socioeconômica', 1),
  (1022, 2, 106, 'Executar projetos de engenharia', 2),
  (1023, 2, 106, 'Substituir documentos de registro', 3),
  (1024, 2, 106, 'Emitir alvarás', 4),

  (1025, 2, 107, 'Secretaria Municipal de Habitação', 1),
  (1026, 2, 107, 'Tribunal de Justiça', 2),
  (1027, 2, 107, 'Ministério da Fazenda', 3),
  (1028, 2, 107, 'Conselho Tutelar', 4),

  (1029, 2, 108, 'Entrega de título de propriedade', 1),
  (1030, 2, 108, 'Pagamento de impostos atrasados', 2),
  (1031, 2, 108, 'Levantamento topográfico', 3),
  (1032, 2, 108, 'Cadastro ambiental', 4),

  (1033, 2, 109, 'Estabelecer normativas aplicáveis', 1),
  (1034, 2, 109, 'Financiar obras', 2),
  (1035, 2, 109, 'Executar medições', 3),
  (1036, 2, 109, 'Emitir título de propriedade', 4),

  (1037, 2, 110, 'Reurb-S é voltada à regularização fundiária simplificada', 1),
  (1038, 2, 110, 'Reurb-S exige desapropriação', 2),
  (1039, 2, 110, 'Reurb-E é sempre extrajudicial', 3),
  (1040, 2, 110, 'Não há diferenças', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Set correct alternatives for evaluation 2 questions
UPDATE questao SET fk_alternativa_correta = 1001 WHERE id_questao = 101;
UPDATE questao SET fk_alternativa_correta = 1005 WHERE id_questao = 102;
UPDATE questao SET fk_alternativa_correta = 1009 WHERE id_questao = 103;
UPDATE questao SET fk_alternativa_correta = 1013 WHERE id_questao = 104;
UPDATE questao SET fk_alternativa_correta = 1017 WHERE id_questao = 105;
UPDATE questao SET fk_alternativa_correta = 1021 WHERE id_questao = 106;
UPDATE questao SET fk_alternativa_correta = 1025 WHERE id_questao = 107;
UPDATE questao SET fk_alternativa_correta = 1029 WHERE id_questao = 108;
UPDATE questao SET fk_alternativa_correta = 1033 WHERE id_questao = 109;
UPDATE questao SET fk_alternativa_correta = 1037 WHERE id_questao = 110;

-- Evaluation 3 (fk_avaliacao = 3) - Course 3 (Diagnóstico Socioeconômico e Cadastro Social)
INSERT INTO questao (id_questao, fk_avaliacao, enunciado, numero_questao, fk_alternativa_correta) VALUES
  (201, 3, 'O que é o Cadastro Social no contexto da Reurb?', 1, NULL),
  (202, 3, 'Qual dada-chave coleta informações familiares?', 2, NULL),
  (203, 3, 'Para que serve o diagnóstico socioeconômico?', 3, NULL),
  (204, 3, 'Que método é usado para levantamento domiciliar?', 4, NULL),
  (205, 3, 'Como garantir a qualidade dos dados coletados?', 5, NULL),
  (206, 3, 'Que informação é vital para priorizar beneficiários?', 6, NULL),
  (207, 3, 'Quem normalmente aplica entrevistas no campo?', 7, NULL),
  (208, 3, 'O que caracteriza dados socioeconômicos confiáveis?', 8, NULL),
  (209, 3, 'Como tratar dados sensíveis no cadastro?', 9, NULL),
  (210, 3, 'Qual a periodicidade recomendada para atualização do cadastro?', 10, NULL)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), enunciado = VALUES(enunciado), numero_questao = VALUES(numero_questao), fk_alternativa_correta = VALUES(fk_alternativa_correta);

-- Alternatives for evaluation 3 (ids 2001..2040)
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (2001, 3, 201, 'Instrumento para mapear famílias e sua situação', 1),
  (2002, 3, 201, 'Documento de propriedade', 2),
  (2003, 3, 201, 'Relatório de engenharia apenas', 3),
  (2004, 3, 201, 'Lista de materiais didáticos', 4),

  (2005, 3, 202, 'Renda familiar', 1),
  (2006, 3, 202, 'Cor da fachada', 2),
  (2007, 3, 202, 'Distância até o mar', 3),
  (2008, 3, 202, 'Número de quartos', 4),

  (2009, 3, 203, 'Identificar vulnerabilidades e necessidades', 1),
  (2010, 3, 203, 'Emitir certidões', 2),
  (2011, 3, 203, 'Iniciar desapropriação', 3),
  (2012, 3, 203, 'Cobrar tributos', 4),

  (2013, 3, 204, 'Questionário domiciliar', 1),
  (2014, 3, 204, 'Análise de imagens de satélite apenas', 2),
  (2015, 3, 204, 'Laudo pericial exclusivo', 3),
  (2016, 3, 204, 'Reunião pública sem visita', 4),

  (2017, 3, 205, 'Treinamento de entrevistadores e validação cruzada', 1),
  (2018, 3, 205, 'Coleta sem supervisão', 2),
  (2019, 3, 205, 'Apenas uso de formulários eletrônicos sem validação', 3),
  (2020, 3, 205, 'Ignorar inconsistências', 4),

  (2021, 3, 206, 'Renda per capita e composição familiar', 1),
  (2022, 3, 206, 'Tipo de piso da residência', 2),
  (2023, 3, 206, 'Número de árvores', 3),
  (2024, 3, 206, 'Cor do telhado', 4),

  (2025, 3, 207, 'Agentes municipais treinados', 1),
  (2026, 3, 207, 'Técnicos de bancos privados', 2),
  (2027, 3, 207, 'Apenas moradores sem treinamento', 3),
  (2028, 3, 207, 'Empreiteiros de obras', 4),

  (2029, 3, 208, 'Consistência, completude e atualidade', 1),
  (2030, 3, 208, 'Quantidade de fotos', 2),
  (2031, 3, 208, 'Número de páginas do relatório', 3),
  (2032, 3, 208, 'Tamanho do arquivo CSV', 4),

  (2033, 3, 209, 'Anonimização e tratamento conforme LGPD', 1),
  (2034, 3, 209, 'Divulgação pública irrestrita', 2),
  (2035, 3, 209, 'Venda dos dados', 3),
  (2036, 3, 209, 'Armazenamento em papel apenas', 4),

  (2037, 3, 210, 'Atualização anual recomendada', 1),
  (2038, 3, 210, 'Nunca atualizar', 2),
  (2039, 3, 210, 'Atualização diária obrigatória', 3),
  (2040, 3, 210, 'Atualização a cada década', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Set correct alternatives for evaluation 3 questions
UPDATE questao SET fk_alternativa_correta = 2001 WHERE id_questao = 201;
UPDATE questao SET fk_alternativa_correta = 2005 WHERE id_questao = 202;
UPDATE questao SET fk_alternativa_correta = 2009 WHERE id_questao = 203;
UPDATE questao SET fk_alternativa_correta = 2013 WHERE id_questao = 204;
UPDATE questao SET fk_alternativa_correta = 2017 WHERE id_questao = 205;
UPDATE questao SET fk_alternativa_correta = 2021 WHERE id_questao = 206;
UPDATE questao SET fk_alternativa_correta = 2025 WHERE id_questao = 207;
UPDATE questao SET fk_alternativa_correta = 2029 WHERE id_questao = 208;
UPDATE questao SET fk_alternativa_correta = 2033 WHERE id_questao = 209;
UPDATE questao SET fk_alternativa_correta = 2037 WHERE id_questao = 210;

-- Evaluation 4 (fk_avaliacao = 4) - Course 4 (Procedimentos Técnicos e Documentais na Reurb)
INSERT INTO questao (id_questao, fk_avaliacao, enunciado, numero_questao, fk_alternativa_correta) VALUES
  (301, 4, 'Qual documento técnico descreve a topografia do lote?', 1, NULL),
  (302, 4, 'O que é memorial descritivo?', 2, NULL),
  (303, 4, 'Que planta é necessária para registro?', 3, NULL),
  (304, 4, 'Quem deve assinar projetos técnicos?', 4, NULL),
  (305, 4, 'Qual a finalidade de um laudo técnico?', 5, NULL),
  (306, 4, 'O que é planta baixa?', 6, NULL),
  (307, 4, 'Quando é exigido o ART/RC relativo ao projeto?', 7, NULL),
  (308, 4, 'Qual documento facilita o registro imobiliário?', 8, NULL),
  (309, 4, 'Que informação mínima uma planta deve apresentar?', 9, NULL),
  (310, 4, 'Como garantir a validade dos documentos técnicos?', 10, NULL)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), enunciado = VALUES(enunciado), numero_questao = VALUES(numero_questao), fk_alternativa_correta = VALUES(fk_alternativa_correta);

-- Alternatives for evaluation 4 (ids 3001..3040)
INSERT INTO alternativa (id_alternativa, fk_avaliacao, fk_questao, texto, ordem_alternativa) VALUES
  (3001, 4, 301, 'Planta topográfica', 1),
  (3002, 4, 301, 'Relatório socioeconômico', 2),
  (3003, 4, 301, 'Contrato de locação', 3),
  (3004, 4, 301, 'Documento de arrecadação', 4),

  (3005, 4, 302, 'Descrição técnica detalhada do imóvel', 1),
  (3006, 4, 302, 'Contrato de compra', 2),
  (3007, 4, 302, 'Planta de situação', 3),
  (3008, 4, 302, 'Declaração de vizinhos', 4),

  (3009, 4, 303, 'Planta cadastrada e assinada por profissional habilitado', 1),
  (3010, 4, 303, 'Documento de identidade do proprietário', 2),
  (3011, 4, 303, 'Relatório financeiro', 3),
  (3012, 4, 303, 'Protocolo municipal genérico', 4),

  (3013, 4, 304, 'Engenheiro ou arquiteto habilitado', 1),
  (3014, 4, 304, 'Testemunhas do bairro', 2),
  (3015, 4, 304, 'Prefeitura', 3),
  (3016, 4, 304, 'Cartório sem assinatura técnica', 4),

  (3017, 4, 305, 'Fundamentar tecnicamente a situação do imóvel', 1),
  (3018, 4, 305, 'Substituir o registro imobiliário', 2),
  (3019, 4, 305, 'Cobrar IPTU atrasado', 3),
  (3020, 4, 305, 'Emitir nota fiscal', 4),

  (3021, 4, 306, 'Desenho em planta representando o pavimento', 1),
  (3022, 4, 306, 'Relatório socioeconômico', 2),
  (3023, 4, 306, 'Documento de arrecadação', 3),
  (3024, 4, 306, 'Declaração de ocupação', 4),

  (3025, 4, 307, 'Ao assumir responsabilidade técnica pelo projeto', 1),
  (3026, 4, 307, 'Apenas para obras comerciais', 2),
  (3027, 4, 307, 'Somente quando houver financiamento', 3),
  (3028, 4, 307, 'Nunca exigido', 4),

  (3029, 4, 308, 'Planta devidamente assinada e registrada', 1),
  (3030, 4, 308, 'Fotos das fachadas', 2),
  (3031, 4, 308, 'Comprovante de residência', 3),
  (3032, 4, 308, 'Carta de vizinhança', 4),

  (3033, 4, 309, 'Escala, orientação e medidas principais', 1),
  (3034, 4, 309, 'Cor do telhado', 2),
  (3035, 4, 309, 'Número de moradores', 3),
  (3036, 4, 309, 'Marca do mobiliário', 4),

  (3037, 4, 310, 'Assinatura de responsável técnico e carimbo profissional', 1),
  (3038, 4, 310, 'Apenas carimbo da prefeitura', 2),
  (3039, 4, 310, 'Assinatura de vizinhos', 3),
  (3040, 4, 310, 'Declaração de proprietário sem assinatura técnica', 4)
ON DUPLICATE KEY UPDATE fk_avaliacao = VALUES(fk_avaliacao), fk_questao = VALUES(fk_questao), texto = VALUES(texto), ordem_alternativa = VALUES(ordem_alternativa);

-- Set correct alternatives for evaluation 4 questions
UPDATE questao SET fk_alternativa_correta = 3001 WHERE id_questao = 301;
UPDATE questao SET fk_alternativa_correta = 3005 WHERE id_questao = 302;
UPDATE questao SET fk_alternativa_correta = 3009 WHERE id_questao = 303;
UPDATE questao SET fk_alternativa_correta = 3013 WHERE id_questao = 304;
UPDATE questao SET fk_alternativa_correta = 3017 WHERE id_questao = 305;
UPDATE questao SET fk_alternativa_correta = 3021 WHERE id_questao = 306;
UPDATE questao SET fk_alternativa_correta = 3025 WHERE id_questao = 307;
UPDATE questao SET fk_alternativa_correta = 3029 WHERE id_questao = 308;
UPDATE questao SET fk_alternativa_correta = 3033 WHERE id_questao = 309;
UPDATE questao SET fk_alternativa_correta = 3037 WHERE id_questao = 310;


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

-- Garantir que valores de 'estrelas' fiquem no intervalo 1..5 (caso existam dados legados com outra escala)
UPDATE feedback
SET estrelas = LEAST(GREATEST(COALESCE(estrelas, 1), 1), 5)
WHERE estrelas IS NOT NULL;

-- Marcar um feedback como anônimo (usuário 10, curso 1) quando a coluna 'anonimo' existir
-- Fazemos o UPDATE somente se a coluna 'anonimo' estiver presente (JOIN em information_schema)
UPDATE feedback f
JOIN information_schema.COLUMNS c ON c.TABLE_SCHEMA = DATABASE() AND c.TABLE_NAME = 'feedback' AND c.COLUMN_NAME = 'anonimo'
SET f.anonimo = 1
WHERE f.fk_curso = 1 AND f.fk_usuario = 10;

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

-- Tentativas adicionais
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
-- COMMENTED OUT: Conflicting with existing duplicate data
-- ALTER TABLE tentativa ADD UNIQUE INDEX IF NOT EXISTS ux_tentativa_comp (fk_curso, fk_usuario, id_tentativa);

-- Inserir atividades para John Doe (referenciado pelo email john@doe.com)
-- MaterialAluno: apenas insere se o usuário existir
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso)
  SELECT id_usuario, 1, 1, NULL, 1, '2025-02-03 09:30:00' FROM usuario WHERE email = 'john@doe.com'
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);


select * from video;
SELECT id_material_aluno, fk_video, fk_apostila, finalizada, ultimo_acesso, fk_curso, fk_usuario FROM material_aluno WHERE fk_usuario=1 AND fk_curso=1 ORDER BY id_material_aluno;

SELECT id_questao, fk_alternativa_correta FROM questao WHERE fk_avaliacao = 1;
SELECT * FROM avaliacao WHERE fk_curso = 1;

-- Ajuste: garantir pelo menos 4 participantes com ultimo_acesso recente (dentro dos últimos 15 dias)
-- Os valores de ultimo_acesso dos usuários já estão definidos no INSERT inicial

UPDATE matricula SET ultimo_senso = '2025-12-03 09:00:00' WHERE fk_usuario = 1 AND fk_curso = 1;
UPDATE matricula SET ultimo_senso = '2025-12-03 10:00:00' WHERE fk_usuario = 10 AND fk_curso = 1;
UPDATE matricula SET ultimo_senso = '2025-12-02 11:00:00' WHERE fk_usuario = 11 AND fk_curso = 1;
UPDATE matricula SET ultimo_senso = '2025-12-03 09:00:00' WHERE fk_usuario = 12 AND fk_curso = 1;

UPDATE material_aluno SET ultimo_acesso = '2025-12-03 09:00:00' WHERE fk_usuario = 1 AND fk_curso = 1;
UPDATE material_aluno SET ultimo_acesso = '2025-12-03 10:00:00' WHERE fk_usuario = 10 AND fk_curso = 1;
UPDATE material_aluno SET ultimo_acesso = '2025-12-02 11:00:00' WHERE fk_usuario = 11 AND fk_curso = 1;
UPDATE material_aluno SET ultimo_acesso = '2025-12-03 09:00:00' WHERE fk_usuario = 12 AND fk_curso = 1;

-- =============================================================================
-- DADOS PARA MÉTRICAS DE ENGAJAMENTO (NOVEMBRO 2025)
-- Contexto: Data atual simulada = 24/11/2025
-- =============================================================================

-- 1. % Ativos 3x ou mais (7 dias: 17/11 a 24/11)
-- Usuário 20 (John Doe): Acessos em 3 dias distintos (20, 22, 24)
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  (20, 1, 1, NULL, 1, '2025-11-20 10:00:00'),
  (20, 1, NULL, 1, 1, '2025-11-22 14:00:00'),
  (20, 1, 2, NULL, 1, '2025-11-24 09:00:00')
ON DUPLICATE KEY UPDATE ultimo_acesso = VALUES(ultimo_acesso);

-- Usuário 10 (Ana Lima): Tentativas em 3 dias distintos (18, 21, 23)
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  (50, 10, 1, '2025-11-18 15:00:00', 1),
  (51, 10, 1, '2025-11-21 16:00:00', 1),
  (52, 10, 1, '2025-11-23 11:00:00', 1)
ON DUPLICATE KEY UPDATE dt_tentativa = VALUES(dt_tentativa);

-- Usuário 1 (João Silva): Acessos em 3 dias distintos (19, 20, 21)
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  (1, 1, 1, NULL, 1, '2025-11-19 08:00:00'),
  (1, 1, NULL, 1, 1, '2025-11-20 08:30:00'),
  (1, 1, 2, NULL, 1, '2025-11-21 09:00:00')
ON DUPLICATE KEY UPDATE ultimo_acesso = VALUES(ultimo_acesso);


-- 2. % Concluindo +1 Curso (7 dias: 17/11 a 24/11)
-- Usuário 20 (John Doe): Concluiu curso 2 em 22/11
UPDATE matricula SET completo = 1, data_finalizado = '2025-11-22 10:00:00' 
WHERE fk_usuario = 20 AND fk_curso = 2;

-- Usuário 11 (Bruno Costa): Concluiu curso 1 em 20/11
UPDATE matricula SET completo = 1, data_finalizado = '2025-11-20 14:00:00' 
WHERE fk_usuario = 11 AND fk_curso = 1;

-- Usuário 12 (Carla Nunes): Concluiu curso 1 em 23/11
UPDATE matricula SET completo = 1, data_finalizado = '2025-11-23 18:00:00' 
WHERE fk_usuario = 12 AND fk_curso = 1;

-- Usuário 15 (Felipe Rocha): Concluiu curso 1 em 18/11
UPDATE matricula SET completo = 1, data_finalizado = '2025-11-18 11:00:00' 
WHERE fk_usuario = 15 AND fk_curso = 1;

-- =============================================================
-- Adiciona 10 novos colaboradores (fk_cargo = 2) com matrícula,
-- material e uma tentativa/resposta mínima para avaliação 1
-- ids escolhidos: 23..32
-- =============================================================
INSERT INTO usuario (id_usuario, nome, cpf, email, senha, fk_cargo, telefone, foto_url, data_entrada, ultimo_acesso) VALUES
  (23, 'Laura Mendes', '40000000001', 'laura.mendes@example.com', 'senhaLaura1', 2, NULL, NULL, '2025-09-23 08:00:00', '2025-11-28 09:00:00'),
  (24, 'Marcos Pinto', '40000000002', 'marcos.pinto@example.com', 'senhaMarcos2', 2, NULL, NULL, '2025-09-24 08:00:00', '2025-11-29 10:00:00'),
  (25, 'Natalia Rocha', '40000000003', 'natalia.rocha@example.com', 'senhaNatalia3', 2, NULL, NULL, '2025-09-25 08:00:00', '2025-11-27 11:00:00'),
  (26, 'Pedro Alvez', '40000000004', 'pedro.alvez@example.com', 'senhaPedro4', 2, NULL, NULL, '2025-09-26 08:00:00', '2025-12-01 12:00:00'),
  (27, 'Rosa Ferreira', '40000000005', 'rosa.ferreira@example.com', 'senhaRosa5', 2, NULL, NULL, '2025-09-27 08:00:00', '2025-11-30 09:00:00'),
  (28, 'Samuel Costa', '40000000006', 'samuel.costa@example.com', 'senhaSamuel6', 2, NULL, NULL, '2025-09-28 08:00:00', '2025-12-02 14:00:00'),
  (29, 'Tatiana Lopes', '40000000007', 'tatiana.lopes@example.com', 'senhaTatiana7', 2, NULL, NULL, '2025-09-29 08:00:00', '2025-11-26 10:00:00'),
  (30, 'Urbano Silva', '40000000008', 'urbano.silva@example.com', 'senhaUrbano8', 2, NULL, NULL, '2025-09-30 08:00:00', '2025-11-28 08:30:00'),
  (31, 'Vanessa Braga', '40000000009', 'vanessa.braga@example.com', 'senhaVanessa9', 2, NULL, NULL, '2025-09-30 08:00:00', '2025-11-02 11:00:00'),
  (32, 'Rafael Torres', '40000000010', 'rafael.torres@example.com', 'senhaRafael10', 2, NULL, NULL, '2025-09-30 08:00:00', '2025-12-03 15:00:00')
ON DUPLICATE KEY UPDATE nome = VALUES(nome), cpf = VALUES(cpf), email = VALUES(email), senha = VALUES(senha), fk_cargo = VALUES(fk_cargo), telefone = VALUES(telefone), foto_url = VALUES(foto_url), data_entrada = VALUES(data_entrada), ultimo_acesso = VALUES(ultimo_acesso);

-- Matrículas: inscreve cada colaborador no curso 1 (curso base)
INSERT INTO matricula (fk_usuario, fk_curso, fk_inicio, ultimo_senso, completo, data_finalizado) VALUES
  (23, 1, '2025-10-01 08:00:00', '2025-10-10 09:00:00', 0, NULL),
  (24, 1, '2025-10-02 08:00:00', '2025-10-12 09:00:00', 0, NULL),
  (25, 1, '2025-10-03 08:00:00', '2025-10-13 09:00:00', 1, '2025-10-20 10:00:00'),
  (26, 1, '2025-10-04 08:00:00', '2025-10-14 09:00:00', 0, NULL),
  (27, 1, '2025-10-05 08:00:00', '2025-10-15 09:00:00', 1, '2025-10-22 11:00:00'),
  (28, 1, '2025-10-06 08:00:00', '2025-10-16 09:00:00', 0, NULL),
  (29, 1, '2025-10-07 08:00:00', '2025-10-17 09:00:00', 1, '2025-10-24 12:00:00'),
  (30, 1, '2025-10-08 08:00:00', '2025-10-18 09:00:00', 0, NULL),
  (31, 1, '2025-10-09 08:00:00', '2025-10-19 09:00:00', 0, NULL),
  (32, 1, '2025-10-10 08:00:00', '2025-10-20 09:00:00', 1, '2025-10-27 13:00:00')
ON DUPLICATE KEY UPDATE fk_inicio = VALUES(fk_inicio), ultimo_senso = VALUES(ultimo_senso), completo = VALUES(completo), data_finalizado = VALUES(data_finalizado);

-- Material do aluno: cada colaborador tem pelo menos 1 material (video id 1) e alguns têm conclusão
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  (23, 1, 1, NULL, 1, '2025-10-10 09:00:00'),
  (24, 1, 1, NULL, 0, '2025-10-12 09:00:00'),
  (25, 1, 1, NULL, 1, '2025-10-20 10:00:00'),
  (26, 1, 1, NULL, 0, '2025-10-14 09:00:00'),
  (27, 1, 1, NULL, 1, '2025-10-22 11:00:00'),
  (28, 1, 1, NULL, 0, '2025-10-16 09:00:00'),
  (29, 1, 1, NULL, 1, '2025-10-24 12:00:00'),
  (30, 1, 1, NULL, 0, '2025-10-18 09:00:00'),
  (31, 1, 1, NULL, 0, '2025-10-19 09:00:00'),
  (32, 1, 1, NULL, 1, '2025-10-27 13:00:00')
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

-- Tentativas (ids 60..69) e respostas para avaliação 1 (fk_avaliacao = 1)
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  (60, 23, 1, '2025-10-11 10:00:00', 1),
  (61, 24, 1, '2025-10-13 10:30:00', 1),
  (62, 25, 1, '2025-10-21 11:00:00', 1),
  (63, 26, 1, '2025-10-15 09:30:00', 1),
  (64, 27, 1, '2025-10-23 12:00:00', 1),
  (65, 28, 1, '2025-10-17 10:15:00', 1),
  (66, 29, 1, '2025-10-25 12:30:00', 1),
  (67, 30, 1, '2025-10-19 11:45:00', 1),
  (68, 31, 1, '2025-10-20 14:00:00', 1),
  (69, 32, 1, '2025-10-28 15:00:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

-- Respostas: cada tentativa responde ao menos a questão 1 (padrão) e, quando aplicável, questão 2
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (23, 1, 60, 1, 1, 2),
  (24, 1, 61, 1, 1, 3),
  (25, 1, 62, 1, 1, 2),
  (26, 1, 63, 1, 1, 1),
  (27, 1, 64, 1, 1, 2),
  (28, 1, 65, 1, 1, 3),
  (29, 1, 66, 1, 1, 2),
  (30, 1, 67, 1, 1, 1),
  (31, 1, 68, 1, 1, 2),
  (32, 1, 69, 1, 1, 2)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- Fim do bloco de colaboradores adicionais

-- =============================================================================
-- DADOS DE ENGAJAMENTO: NOVEMBRO A 10/12/2025
-- Popula material_aluno e tentativa para preencher gráfico de Materiais Concluídos
-- =============================================================================

-- Materiais concluídos em Novembro/2025 (vários usuários, dias espalhados)
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  -- Semana 1 de Novembro (01-07)
  (10, 1, 2, NULL, 1, '2025-11-01 10:00:00'),
  (11, 1, 2, NULL, 1, '2025-11-01 11:00:00'),
  (12, 1, NULL, 2, 1, '2025-11-02 09:00:00'),
  (13, 1, 1, NULL, 1, '2025-11-03 14:00:00'),
  (14, 1, NULL, 1, 1, '2025-11-04 10:30:00'),
  (15, 1, 2, NULL, 1, '2025-11-05 08:00:00'),
  (16, 1, 1, NULL, 1, '2025-11-06 15:00:00'),
  (17, 1, NULL, 2, 1, '2025-11-07 12:00:00'),
  
  -- Semana 2 de Novembro (08-14)
  (10, 1, 3, NULL, 1, '2025-11-08 09:30:00'),
  (11, 1, NULL, 3, 1, '2025-11-09 10:00:00'),
  (12, 1, 3, NULL, 1, '2025-11-10 11:00:00'),
  (13, 1, NULL, 2, 1, '2025-11-11 14:00:00'),
  (19, 1, 2, NULL, 1, '2025-11-12 08:00:00'),
  (23, 1, 2, NULL, 1, '2025-11-13 09:00:00'),
  (24, 1, 2, NULL, 1, '2025-11-14 10:00:00'),
  
  -- Semana 3 de Novembro (15-21)
  (25, 1, 2, NULL, 1, '2025-11-15 11:00:00'),
  (26, 1, 1, NULL, 1, '2025-11-16 12:00:00'),
  (27, 1, NULL, 2, 1, '2025-11-17 09:00:00'),
  (28, 1, 2, NULL, 1, '2025-11-18 14:00:00'),
  (29, 1, 3, NULL, 1, '2025-11-19 10:00:00'),
  (30, 1, 1, NULL, 1, '2025-11-20 08:30:00'),
  (31, 1, NULL, 1, 1, '2025-11-21 15:00:00'),
  
  -- Semana 4 de Novembro (22-28)
  (10, 1, NULL, 3, 1, '2025-11-22 09:00:00'),
  (11, 1, 3, NULL, 1, '2025-11-23 10:00:00'),
  (12, 1, NULL, 1, 1, '2025-11-24 11:30:00'),
  (15, 1, 3, NULL, 1, '2025-11-25 14:00:00'),
  (17, 1, 3, NULL, 1, '2025-11-26 08:00:00'),
  (32, 1, 2, NULL, 1, '2025-11-27 09:30:00'),
  (20, 1, 3, NULL, 1, '2025-11-28 12:00:00'),
  
  -- Semana 5 de Novembro e Dezembro (29/11 a 05/12)
  (10, 1, NULL, 4, 1, '2025-11-29 10:00:00'),
  (11, 1, NULL, 4, 1, '2025-11-30 11:00:00'),
  (12, 1, 4, NULL, 1, '2025-12-01 09:00:00'),
  (13, 1, 3, NULL, 1, '2025-12-02 14:00:00'),
  (14, 1, 3, NULL, 1, '2025-12-03 10:00:00'),
  (15, 1, NULL, 3, 1, '2025-12-04 08:00:00'),
  (16, 1, 2, NULL, 1, '2025-12-05 15:00:00'),
  
  -- Semana de 06 a 10/12
  (17, 1, 4, NULL, 1, '2025-12-06 09:00:00'),
  (19, 1, 3, NULL, 1, '2025-12-07 10:30:00'),
  (20, 1, 4, NULL, 1, '2025-12-08 11:00:00'),
  (23, 1, 3, NULL, 1, '2025-12-08 14:00:00'),
  (24, 1, 3, NULL, 1, '2025-12-09 08:00:00'),
  (25, 1, 3, NULL, 1, '2025-12-09 09:00:00'),
  (26, 1, 2, NULL, 1, '2025-12-10 10:00:00'),
  (27, 1, 3, NULL, 1, '2025-12-10 11:00:00'),
  (28, 1, 3, NULL, 1, '2025-12-10 12:00:00'),
  (29, 1, NULL, 4, 1, '2025-12-10 14:00:00'),
  (30, 1, 2, NULL, 1, '2025-12-10 15:00:00')
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

-- Tentativas em Novembro e início de Dezembro (ids 70..99)
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  -- Novembro
  (70, 10, 1, '2025-11-01 14:00:00', 1),
  (71, 11, 1, '2025-11-02 10:00:00', 1),
  (72, 12, 1, '2025-11-03 11:00:00', 1),
  (73, 13, 1, '2025-11-05 09:00:00', 1),
  (74, 14, 1, '2025-11-06 14:30:00', 1),
  (75, 15, 1, '2025-11-08 10:00:00', 1),
  (76, 16, 1, '2025-11-10 11:00:00', 1),
  (77, 17, 1, '2025-11-12 08:00:00', 1),
  (78, 19, 1, '2025-11-14 15:00:00', 1),
  (79, 20, 1, '2025-11-15 09:30:00', 1),
  (80, 23, 1, '2025-11-17 10:00:00', 1),
  (81, 24, 1, '2025-11-19 11:00:00', 1),
  (82, 25, 1, '2025-11-20 14:00:00', 1),
  (83, 26, 1, '2025-11-22 08:00:00', 1),
  (84, 27, 1, '2025-11-24 09:00:00', 1),
  (85, 28, 1, '2025-11-25 10:30:00', 1),
  (86, 29, 1, '2025-11-27 11:00:00', 1),
  (87, 30, 1, '2025-11-28 14:00:00', 1),
  (88, 31, 1, '2025-11-29 08:00:00', 1),
  (89, 32, 1, '2025-11-30 09:00:00', 1),
  -- Dezembro (até 10/12)
  (90, 10, 1, '2025-12-01 10:00:00', 1),
  (91, 11, 1, '2025-12-02 11:00:00', 1),
  (92, 12, 1, '2025-12-03 09:00:00', 1),
  (93, 13, 1, '2025-12-04 14:00:00', 1),
  (94, 14, 1, '2025-12-05 10:00:00', 1),
  (95, 15, 1, '2025-12-06 08:00:00', 1),
  (96, 16, 1, '2025-12-07 15:00:00', 1),
  (97, 17, 1, '2025-12-08 09:00:00', 1),
  (98, 19, 1, '2025-12-09 10:30:00', 1),
  (99, 20, 1, '2025-12-10 11:00:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

-- Respostas para as novas tentativas
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (10, 1, 70, 1, 1, 2),
  (11, 1, 71, 1, 1, 2),
  (12, 1, 72, 1, 1, 2),
  (13, 1, 73, 1, 1, 1),
  (14, 1, 74, 1, 1, 2),
  (15, 1, 75, 1, 1, 2),
  (16, 1, 76, 1, 1, 1),
  (17, 1, 77, 1, 1, 2),
  (19, 1, 78, 1, 1, 2),
  (20, 1, 79, 1, 1, 2),
  (23, 1, 80, 1, 1, 2),
  (24, 1, 81, 1, 1, 3),
  (25, 1, 82, 1, 1, 2),
  (26, 1, 83, 1, 1, 1),
  (27, 1, 84, 1, 1, 2),
  (28, 1, 85, 1, 1, 3),
  (29, 1, 86, 1, 1, 2),
  (30, 1, 87, 1, 1, 1),
  (31, 1, 88, 1, 1, 2),
  (32, 1, 89, 1, 1, 2),
  (10, 1, 90, 1, 1, 2),
  (11, 1, 91, 1, 1, 2),
  (12, 1, 92, 1, 1, 2),
  (13, 1, 93, 1, 1, 2),
  (14, 1, 94, 1, 1, 2),
  (15, 1, 95, 1, 1, 2),
  (16, 1, 96, 1, 1, 2),
  (17, 1, 97, 1, 1, 2),
  (19, 1, 98, 1, 1, 2),
  (20, 1, 99, 1, 1, 2)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- =============================================================================
-- DADOS DEZEMBRO 2025 - MAIS VARIADOS PARA KPIs
-- Data atual simulada: 03/12/2025
-- Últimos 7 dias: 26/11 a 03/12
-- =============================================================================

-- PARTICIPANTES INATIVOS (>15 dias sem acesso)
-- Usuários 13, 14, 16, 31, 1, 2, 3 ficam sem acesso recente (mais de 15 dias)
UPDATE usuario SET ultimo_acesso = '2025-11-01 10:00:00' WHERE id_usuario = 13;
UPDATE usuario SET ultimo_acesso = '2025-11-05 14:00:00' WHERE id_usuario = 14;
UPDATE usuario SET ultimo_acesso = '2025-11-08 09:00:00' WHERE id_usuario = 16;
UPDATE usuario SET ultimo_acesso = '2025-11-02 11:00:00' WHERE id_usuario = 31;
UPDATE usuario SET ultimo_acesso = '2025-10-20 08:00:00' WHERE id_usuario = 1;
UPDATE usuario SET ultimo_acesso = '2025-10-15 10:00:00' WHERE id_usuario = 2;
UPDATE usuario SET ultimo_acesso = '2025-10-25 12:00:00' WHERE id_usuario = 3;
UPDATE usuario SET ultimo_acesso = '2025-11-10 09:00:00' WHERE id_usuario = 21;
UPDATE usuario SET ultimo_acesso = '2025-11-12 14:00:00' WHERE id_usuario = 22;

-- ATIVOS NOS ÚLTIMOS 7 DIAS (26/11 a 03/12)
-- Usuários com acesso recente
UPDATE usuario SET ultimo_acesso = '2025-12-03 10:00:00' WHERE id_usuario = 10;
UPDATE usuario SET ultimo_acesso = '2025-12-02 11:00:00' WHERE id_usuario = 11;
UPDATE usuario SET ultimo_acesso = '2025-12-03 09:00:00' WHERE id_usuario = 12;
UPDATE usuario SET ultimo_acesso = '2025-12-01 14:00:00' WHERE id_usuario = 15;
UPDATE usuario SET ultimo_acesso = '2025-11-30 08:00:00' WHERE id_usuario = 17;
UPDATE usuario SET ultimo_acesso = '2025-12-02 10:30:00' WHERE id_usuario = 19;
UPDATE usuario SET ultimo_acesso = '2025-12-03 11:00:00' WHERE id_usuario = 20;
UPDATE usuario SET ultimo_acesso = '2025-11-28 09:00:00' WHERE id_usuario = 23;
UPDATE usuario SET ultimo_acesso = '2025-11-29 10:00:00' WHERE id_usuario = 24;
UPDATE usuario SET ultimo_acesso = '2025-11-27 11:00:00' WHERE id_usuario = 25;
UPDATE usuario SET ultimo_acesso = '2025-12-01 12:00:00' WHERE id_usuario = 26;
UPDATE usuario SET ultimo_acesso = '2025-11-30 09:00:00' WHERE id_usuario = 27;
UPDATE usuario SET ultimo_acesso = '2025-12-02 14:00:00' WHERE id_usuario = 28;
UPDATE usuario SET ultimo_acesso = '2025-11-26 10:00:00' WHERE id_usuario = 29;
UPDATE usuario SET ultimo_acesso = '2025-11-28 08:30:00' WHERE id_usuario = 30;
UPDATE usuario SET ultimo_acesso = '2025-12-03 15:00:00' WHERE id_usuario = 32;

-- ATIVOS 3x OU MAIS (3+ dias distintos nos últimos 7 dias: 26/11 a 03/12)
-- Usuários 10, 11, 12, 20 terão atividade em 3+ dias distintos
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  -- Usuário 10: 3 dias distintos (27, 29, 01, 03)
  (10, 1, 1, NULL, 1, '2025-11-27 10:00:00'),
  (10, 1, NULL, 2, 1, '2025-11-29 11:00:00'),
  (10, 1, 2, NULL, 1, '2025-12-01 09:00:00'),
  (10, 1, NULL, 3, 1, '2025-12-03 14:00:00'),
  
  -- Usuário 11: 4 dias distintos (26, 28, 30, 02)
  (11, 1, 3, NULL, 1, '2025-11-26 08:00:00'),
  (11, 1, NULL, 1, 1, '2025-11-28 09:30:00'),
  (11, 1, 1, NULL, 1, '2025-11-30 10:00:00'),
  (11, 1, NULL, 2, 1, '2025-12-02 11:00:00'),
  
  -- Usuário 12: 3 dias distintos (27, 30, 03)
  (12, 1, NULL, 4, 1, '2025-11-27 14:00:00'),
  (12, 1, 3, NULL, 1, '2025-11-30 08:00:00'),
  (12, 1, NULL, 1, 1, '2025-12-03 09:00:00'),
  
  -- Usuário 20: 5 dias distintos (26, 27, 29, 01, 03)
  (20, 1, 2, NULL, 1, '2025-11-26 10:00:00'),
  (20, 1, NULL, 1, 1, '2025-11-27 11:00:00'),
  (20, 1, 3, NULL, 1, '2025-11-29 09:00:00'),
  (20, 1, NULL, 3, 1, '2025-12-01 14:00:00'),
  (20, 1, 4, NULL, 1, '2025-12-03 08:00:00'),
  
  -- Usuário 19: 3 dias distintos (28, 30, 02)
  (19, 1, 4, NULL, 1, '2025-11-28 09:00:00'),
  (19, 1, NULL, 4, 1, '2025-11-30 10:00:00'),
  (19, 1, 2, NULL, 1, '2025-12-02 11:00:00'),
  
  -- Usuário 32: 3 dias distintos (27, 01, 03)
  (32, 1, 3, NULL, 1, '2025-11-27 15:00:00'),
  (32, 1, NULL, 3, 1, '2025-12-01 10:00:00'),
  (32, 1, 4, NULL, 1, '2025-12-03 14:00:00')
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

-- Tentativas adicionais para os últimos 7 dias (ids 100..120)
INSERT INTO tentativa (id_tentativa, fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
  -- Tentativas variadas nos últimos 7 dias
  (100, 10, 1, '2025-11-27 11:00:00', 1),
  (101, 10, 1, '2025-12-01 10:00:00', 1),
  (102, 10, 1, '2025-12-03 15:00:00', 1),
  (103, 11, 1, '2025-11-26 09:00:00', 1),
  (104, 11, 1, '2025-11-30 11:00:00', 1),
  (105, 11, 1, '2025-12-02 12:00:00', 1),
  (106, 12, 1, '2025-11-27 15:00:00', 1),
  (107, 12, 1, '2025-12-03 10:00:00', 1),
  (108, 20, 1, '2025-11-26 11:00:00', 1),
  (109, 20, 1, '2025-11-29 10:00:00', 1),
  (110, 20, 1, '2025-12-03 09:00:00', 1),
  (111, 19, 1, '2025-11-28 10:00:00', 1),
  (112, 19, 1, '2025-12-02 12:00:00', 1),
  (113, 32, 1, '2025-11-27 16:00:00', 1),
  (114, 32, 1, '2025-12-03 15:00:00', 1),
  (115, 15, 1, '2025-12-01 15:00:00', 1),
  (116, 17, 1, '2025-11-30 09:00:00', 1),
  (117, 26, 1, '2025-12-01 13:00:00', 1),
  (118, 28, 1, '2025-12-02 15:00:00', 1),
  (119, 23, 1, '2025-11-28 10:00:00', 1),
  (120, 24, 1, '2025-11-29 11:00:00', 1)
ON DUPLICATE KEY UPDATE fk_usuario = VALUES(fk_usuario), fk_curso = VALUES(fk_curso), dt_tentativa = VALUES(dt_tentativa), fk_avaliacao = VALUES(fk_avaliacao);

-- Respostas para as novas tentativas
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
  (10, 1, 100, 1, 1, 2),
  (10, 1, 101, 1, 1, 2),
  (10, 1, 102, 1, 1, 2),
  (11, 1, 103, 1, 1, 2),
  (11, 1, 104, 1, 1, 2),
  (11, 1, 105, 1, 1, 2),
  (12, 1, 106, 1, 1, 2),
  (12, 1, 107, 1, 1, 2),
  (20, 1, 108, 1, 1, 2),
  (20, 1, 109, 1, 1, 2),
  (20, 1, 110, 1, 1, 2),
  (19, 1, 111, 1, 1, 2),
  (19, 1, 112, 1, 1, 2),
  (32, 1, 113, 1, 1, 2),
  (32, 1, 114, 1, 1, 2),
  (15, 1, 115, 1, 1, 2),
  (17, 1, 116, 1, 1, 2),
  (26, 1, 117, 1, 1, 2),
  (28, 1, 118, 1, 1, 2),
  (23, 1, 119, 1, 1, 2),
  (24, 1, 120, 1, 1, 2)
ON DUPLICATE KEY UPDATE fk_alternativa = VALUES(fk_alternativa);

-- CONCLUINDO +1 CURSO NOS ÚLTIMOS 7 DIAS (26/11 a 03/12)
-- Usuários 10, 11, 20, 19, 32 concluíram cursos recentemente
UPDATE matricula SET completo = 1, data_finalizado = '2025-11-27 12:00:00' WHERE fk_usuario = 10 AND fk_curso = 1;
UPDATE matricula SET completo = 1, data_finalizado = '2025-11-28 10:00:00' WHERE fk_usuario = 11 AND fk_curso = 1;
UPDATE matricula SET completo = 1, data_finalizado = '2025-12-01 14:00:00' WHERE fk_usuario = 20 AND fk_curso = 1;
UPDATE matricula SET completo = 1, data_finalizado = '2025-11-30 11:00:00' WHERE fk_usuario = 19 AND fk_curso = 1;
UPDATE matricula SET completo = 1, data_finalizado = '2025-12-02 15:00:00' WHERE fk_usuario = 32 AND fk_curso = 1;
UPDATE matricula SET completo = 1, data_finalizado = '2025-11-26 09:00:00' WHERE fk_usuario = 12 AND fk_curso = 1;

-- Matricular alguns usuários no curso 1 que ainda não estão (para aumentar total de alunos)
-- Nota: usuários 1, 2, 3 já tem matrículas anteriores, este INSERT apenas garante que existem
INSERT INTO matricula (fk_usuario, fk_curso, fk_inicio, ultimo_senso, completo, data_finalizado) VALUES
  (1, 1, '2025-01-01 08:00:00', '2025-12-03 09:00:00', 0, NULL),
  (2, 1, '2025-01-02 08:00:00', '2025-10-15 10:00:00', 0, NULL),
  (3, 1, '2025-01-03 08:00:00', '2025-10-25 12:00:00', 0, NULL),
  (18, 1, '2025-01-18 08:00:00', '2025-11-10 09:00:00', 0, NULL)
ON DUPLICATE KEY UPDATE fk_inicio = VALUES(fk_inicio), ultimo_senso = VALUES(ultimo_senso), completo = VALUES(completo), data_finalizado = VALUES(data_finalizado);

-- Dados variados para o gráfico de dezembro (valores diferentes por dia)
INSERT INTO material_aluno (fk_usuario, fk_curso, fk_video, fk_apostila, finalizada, ultimo_acesso) VALUES
  -- 26/11 - 3 materiais
  (23, 1, 4, NULL, 1, '2025-11-26 10:00:00'),
  (24, 1, NULL, 4, 1, '2025-11-26 11:00:00'),
  (25, 1, 4, NULL, 1, '2025-11-26 14:00:00'),
  
  -- 27/11 - 5 materiais
  (26, 1, NULL, 4, 1, '2025-11-27 09:00:00'),
  (27, 1, 4, NULL, 1, '2025-11-27 10:00:00'),
  (28, 1, NULL, 3, 1, '2025-11-27 11:00:00'),
  (29, 1, 4, NULL, 1, '2025-11-27 14:00:00'),
  (30, 1, NULL, 4, 1, '2025-11-27 15:00:00'),
  
  -- 28/11 - 4 materiais
  (15, 1, 4, NULL, 1, '2025-11-28 08:00:00'),
  (17, 1, NULL, 4, 1, '2025-11-28 10:00:00'),
  (26, 1, 3, NULL, 1, '2025-11-28 14:00:00'),
  (27, 1, NULL, 3, 1, '2025-11-28 16:00:00'),
  
  -- 29/11 - 6 materiais
  (23, 1, NULL, 3, 1, '2025-11-29 08:00:00'),
  (24, 1, 4, NULL, 1, '2025-11-29 09:00:00'),
  (25, 1, NULL, 3, 1, '2025-11-29 10:00:00'),
  (28, 1, 4, NULL, 1, '2025-11-29 11:00:00'),
  (29, 1, NULL, 3, 1, '2025-11-29 14:00:00'),
  (30, 1, 3, NULL, 1, '2025-11-29 15:00:00'),
  
  -- 30/11 - 7 materiais
  (15, 1, NULL, 3, 1, '2025-11-30 08:00:00'),
  (17, 1, 3, NULL, 1, '2025-11-30 09:00:00'),
  (23, 1, 3, NULL, 1, '2025-11-30 10:00:00'),
  (24, 1, NULL, 3, 1, '2025-11-30 11:00:00'),
  (26, 1, 4, NULL, 1, '2025-11-30 12:00:00'),
  (27, 1, 3, NULL, 1, '2025-11-30 14:00:00'),
  (28, 1, NULL, 4, 1, '2025-11-30 15:00:00'),
  
  -- 01/12 - 8 materiais
  (15, 1, 3, NULL, 1, '2025-12-01 08:00:00'),
  (17, 1, NULL, 3, 1, '2025-12-01 09:00:00'),
  (19, 1, 3, NULL, 1, '2025-12-01 10:00:00'),
  (23, 1, NULL, 4, 1, '2025-12-01 11:00:00'),
  (25, 1, 3, NULL, 1, '2025-12-01 12:00:00'),
  (27, 1, NULL, 4, 1, '2025-12-01 13:00:00'),
  (29, 1, 3, NULL, 1, '2025-12-01 14:00:00'),
  (30, 1, NULL, 3, 1, '2025-12-01 15:00:00'),
  
  -- 02/12 - 5 materiais
  (15, 1, NULL, 4, 1, '2025-12-02 08:00:00'),
  (17, 1, 4, NULL, 1, '2025-12-02 10:00:00'),
  (26, 1, NULL, 3, 1, '2025-12-02 12:00:00'),
  (28, 1, 3, NULL, 1, '2025-12-02 14:00:00'),
  (30, 1, 4, NULL, 1, '2025-12-02 16:00:00'),
  
  -- 03/12 - 9 materiais
  (15, 1, 4, NULL, 1, '2025-12-03 08:00:00'),
  (17, 1, NULL, 4, 1, '2025-12-03 09:00:00'),
  (19, 1, NULL, 3, 1, '2025-12-03 10:00:00'),
  (23, 1, 4, NULL, 1, '2025-12-03 11:00:00'),
  (24, 1, 3, NULL, 1, '2025-12-03 12:00:00'),
  (25, 1, NULL, 4, 1, '2025-12-03 13:00:00'),
  (26, 1, 3, NULL, 1, '2025-12-03 14:00:00'),
  (28, 1, NULL, 3, 1, '2025-12-03 15:00:00'),
  (29, 1, 4, NULL, 1, '2025-12-03 16:00:00')
ON DUPLICATE KEY UPDATE fk_video = VALUES(fk_video), fk_apostila = VALUES(fk_apostila), finalizada = VALUES(finalizada), ultimo_acesso = VALUES(ultimo_acesso);

