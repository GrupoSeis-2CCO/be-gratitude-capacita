 --Drop database Capacita;
create database capacita;
use Capacita;
-- Tabela cargo
CREATE TABLE cargo (
    id_cargo INT PRIMARY KEY AUTO_INCREMENT,
    nome_cargo VARCHAR(50) NOT NULL
);

-- INSERT INTO cargo (nome_cargo) VALUES 
-- ('Colaborador'), ('Funcionário');

-- select * from cargo;

-- Tabela usuario
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    email VARCHAR(256) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,  -- Corrigido de 'senda'
    data_entrada DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso DATETIME,
    fk_cargo INT NOT NULL,
    primary key(id_usuario),	
    FOREIGN KEY (fk_cargo) REFERENCES cargo(id_cargo)
);


--INSERT INTO usuario (nome, cpf, email, senha, fk_cargo) VALUES 
--('João Silva', '12345678901', 'joao@email.com', 'senha123', 1),
--('Maria Souza', '98765432109', 'maria@email.com', 'senha456', 2),
--('Carlos Oliveira', '11122233344', 'carlos@email.com', 'senha789', 3);

--Select * from Usuario;

CREATE TABLE curso (
    id_curso INT AUTO_INCREMENT ,
    titulo_curso VARCHAR(50),
    descricao VARCHAR(100),
    imagem VARCHAR(255),
    ocultado TINYINT,
    duracao_estimada INT,
    PRIMARY KEY(id_curso)
);

INSERT INTO curso (titulo_curso, descricao, imagem, ocultado, duracao_estimada) VALUES
('Java Básico', 'Curso introdutório de Java', 'java.jpg', 0, 20),
('Spring Boot', 'API REST com Spring Boot', 'spring.jpg', 0, 40),
('MySQL Essencial', 'Fundamentos do banco de dados MySQL', 'mysql.jpg', 0, 30);

--select * from curso;

-- Tabela matricula
CREATE TABLE matricula (
    FK_usuario INT,
    FK_curso INT,
    FK_inicio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,  
    ultimo_senso DATETIME,
    completo TINYINT(1) DEFAULT 0,  -- BOOLEAN
    data_finalizado DATETIME,
    PRIMARY KEY (fk_usuario, fk_curso),
    FOREIGN KEY (FK_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso)
);

-- INSERT INTO matricula (fk_usuario, fk_curso) VALUES 
-- (2, 2), (1, 1);

-- select * from matricula;

-- Tabela video
CREATE TABLE video (
    id_video INT PRIMARY KEY AUTO_INCREMENT,
    nome_video VARCHAR(100) NOT NULL,
    descricao_video VARCHAR(256),  
    url_video VARCHAR(256) NOT NULL,
    data_postado_video DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizado_video DATETIME,  
    ordem_video INT,
    is_video_oculto TINYINT(1) DEFAULT 0,  
    FK_curso INT NOT NULL,
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso)
);
INSERT INTO video (nome_video,descricao_video, url_video,ordem_video, fk_curso) VALUES 
('Introdução','video1', 'http://exemplo.com/video1', 1,1),
('Aula 1', 'video2','http://exemplo.com/video2', 2,2),
('Aula 2', 'video3','http://exemplo.com/video3', 3,3);

select * from video;

-- Tabela apostila
CREATE TABLE apostila (
    id_apostila INT PRIMARY KEY AUTO_INCREMENT,  
    nome_apostila_original VARCHAR(100) NOT NULL,
    nome_apostila_armazenamento VARCHAR(100) NOT NULL,  
    descricao_apostila VARCHAR(256),  
    tamanho_bytes INT,  
    data_postado_apostila DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao_apostila DATETIME,  
    is_apostila_oculto TINYINT(1) DEFAULT 0,  
    ordem_apostila INT,  
    FK_curso INT NOT NULL,
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso)
);

INSERT INTO apostila (nome_apostila_original, nome_apostila_armazenamento,descricao_apostila,tamanho_bytes,is_apostila_oculto,ordem_apostila, fk_curso) VALUES 
('Apostila 1.pdf', 'apostila1_armazenada.pdf','apostila 1',225252, 1 ,1, 1),
('Apostila 2.pdf', 'apostila2_armazenada.pdf','apostila 2',25425, 0 ,2, 2),
('Apostila 3.pdf', 'apostila3_armazenada.pdf','apostila 3',2542,1,3, 3);

select * from apostila;


-- Tabela materia_aluno
CREATE TABLE material_aluno (
    FK_usuario INT,
    FK_cargo INT,
    finalizada TINYINT(1) DEFAULT 0,
    ultimo_acesso DATETIME,
    FK_video INT,
    FK_apostila INT,
    PRIMARY KEY (FK_usuario, FK_cargo),
    FOREIGN KEY (FK_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (FK_cargo) REFERENCES cargo(id_cargo),
    FOREIGN KEY (FK_video) REFERENCES video(id_video),
    FOREIGN KEY (FK_apostila) REFERENCES apostila(id_apostila)
);

INSERT INTO material_aluno (fk_usuario, fk_cargo, fk_video, fk_apostila) VALUES 
(1, 1, 1, 1), (2, 2, 2, 2), (3, 2, 3, 3);

select * from material_aluno;

-- Tabela avaliacao
CREATE TABLE avaliacao (  
    id_avaliacao INT PRIMARY KEY AUTO_INCREMENT,  
    nota_minima DECIMAL(5,2) NOT NULL,  
    FK_curso INT NOT NULL,
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso)
);

INSERT INTO avaliacao (nota_minima, fk_curso) VALUES 
(7.5, 1), (6.0, 2), (8.0, 3);

select * from avaliacao;

-- Tabela alternativa
CREATE TABLE alternativa (
    id_alternativa INT  AUTO_INCREMENT,
    FK_questao INT,  
    FK_avaliacao INT NOT NULL,
    texto VARCHAR(50) NOT NULL,
    ordem_alternativa INT NOT NULL,  
    primary key(id_alternativa),
    FOREIGN KEY (FK_avaliacao) REFERENCES avaliacao(id_avaliacao)
);

INSERT INTO alternativa (fk_avaliacao, texto, ordem_alternativa) VALUES 
(1, 'Alternativa A', 1), 
(1, 'Alternativa B', 2), 
(2, 'Alternativa C', 1);

select * from alternativa;

CREATE TABLE tentativa (
    id_tentativa INT AUTO_INCREMENT ,
    fk_usuario INT,
    fk_curso INT,
    dt_tentativa DATETIME,
    fk_avaliacao INT,
    PRIMARY KEY(id_tentativa),
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (fk_curso) REFERENCES curso(id_curso),
    FOREIGN KEY (fk_avaliacao) REFERENCES avaliacao(id_avaliacao)
);

-- Inserts para tentativa
INSERT INTO tentativa (fk_usuario, fk_curso, dt_tentativa, fk_avaliacao) VALUES
(1, 1, '2025-06-01 10:00:00', 1),
(2, 2, '2025-06-02 14:30:00', 2),
(3, 1, '2025-06-03 09:15:00', 1);

CREATE TABLE questao (
    id_questao INT AUTO_INCREMENT PRIMARY KEY,
    fk_avaliacao INT,
    enunciado VARCHAR(100),
    numero_questao INT,
    fk_alternativa_correta INT,
	FOREIGN KEY (fk_avaliacao) REFERENCES avaliacao(id_avaliacao)
);

-- Inserts para questao
INSERT INTO questao (fk_avaliacao, enunciado, numero_questao, fk_alternativa_correta) VALUES
(1, 'O que é JVM?', 1, 2),
(1, 'Qual comando compila um arquivo Java?', 2, 3),
(2, 'Para que serve o @RestController no Spring?', 1, 4);

-- Tabela resposta_usuario
CREATE TABLE resposta_do_usuario ( 
    FK_usuario INT NOT NULL,
    FK_curso INT NOT NULL,
    FK_tentativa INT,  
    FK_avaliacao INT NOT NULL,
    FK_questao INT,  
    FK_alternativa INT NOT NULL,
    PRIMARY KEY (FK_usuario, FK_curso, FK_tentativa, FK_avaliacao, FK_questao, FK_alternativa),
    FOREIGN KEY (FK_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso),
    FOREIGN KEY (FK_tentativa) REFERENCES tentativa(id_tentativa),
    FOREIGN KEY (FK_avaliacao) REFERENCES avaliacao(id_avaliacao),
    FOREIGN KEY (FK_questao) REFERENCES questao(id_questao),
    FOREIGN KEY (FK_alternativa) REFERENCES alternativa(id_alternativa)
);

INSERT INTO resposta_do_usuario (FK_usuario, FK_curso, FK_tentativa, FK_avaliacao, FK_questao, FK_alternativa) VALUES 
(2, 2, 1, 1,3,2), (2, 2, 1, 2,2,1), (3, 3, 2, 3,1,2);

select * from resposta_do_usuario;

-- Tabela feedback
CREATE TABLE feedback (
    FK_curso INT NOT NULL,
    estrelas INT NOT NULL CHECK (estrelas BETWEEN 1 AND 5),
    motivo VARCHAR(250),
    FK_usuario INT NOT NULL,
    PRIMARY KEY (FK_usuario, FK_curso),
    FOREIGN KEY (FK_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso)
);

INSERT INTO feedback (fk_curso, estrelas, motivo, fk_usuario) VALUES 
(1, 5, 'Excelente!', 1),
(2, 4, 'Muito bom', 2),
(3, 3, 'Regular', 3);

select * from feedback;
