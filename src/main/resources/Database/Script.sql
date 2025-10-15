DROP DATABASE IF EXISTS capacita;
create database capacita;
use capacita;

-- Tabela cargo
CREATE TABLE cargo (
    id_cargo INT PRIMARY KEY AUTO_INCREMENT,
    nome_cargo VARCHAR(50) NOT NULL
);

select * from cargo;

-- Tabela usuario
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    email VARCHAR(256) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    data_entrada DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso DATETIME,
    fk_cargo INT NOT NULL,
    primary key(id_usuario),	
    FOREIGN KEY (fk_cargo) REFERENCES cargo(id_cargo)
);

Select * from usuario;

CREATE TABLE curso (
    id_curso INT AUTO_INCREMENT ,
    titulo_curso VARCHAR(50),
    descricao VARCHAR(100),
    imagem VARCHAR(255),
    ocultado TINYINT,
    duracao_estimada INT,
    PRIMARY KEY(id_curso)
);
-- select * from curso;

-- Tabela matricula
CREATE TABLE matricula (
    FK_usuario INT,
    FK_curso INT,
    FK_inicio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,  
    ultimo_senso DATETIME,
    completo TINYINT(1) DEFAULT 0,
    data_finalizado DATETIME,
    PRIMARY KEY (fk_usuario, fk_curso),
    FOREIGN KEY (FK_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso)
);

select * from matricula;

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

-- select * from video;

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
    url_arquivo VARCHAR(255) DEFAULT NULL,
    FK_curso INT NOT NULL,
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso)
);

-- select * from apostila;


-- Tabela material_aluno (um registro por material visualizado/registrado por matrícula)
CREATE TABLE material_aluno (
    id_material_aluno INT PRIMARY KEY AUTO_INCREMENT,
    FK_usuario INT NOT NULL,
    FK_curso INT NOT NULL,
    FK_video INT DEFAULT NULL,
    FK_apostila INT DEFAULT NULL,
    finalizada TINYINT(1) DEFAULT 0,
    ultimo_acesso DATETIME,
    FOREIGN KEY (FK_usuario, FK_curso) REFERENCES matricula(FK_usuario, FK_curso),
    FOREIGN KEY (FK_video) REFERENCES video(id_video),
    FOREIGN KEY (FK_apostila) REFERENCES apostila(id_apostila)
);

select * from material_aluno;

-- Tabela avaliacao
CREATE TABLE avaliacao (  
    id_avaliacao INT PRIMARY KEY AUTO_INCREMENT,  
    nota_minima DECIMAL(5,2) NOT NULL,  
    FK_curso INT NOT NULL,
    FOREIGN KEY (FK_curso) REFERENCES curso(id_curso)
);

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
select * from tentativa;


CREATE TABLE questao (
    id_questao INT AUTO_INCREMENT PRIMARY KEY,
    fk_avaliacao INT,
    enunciado VARCHAR(100),
    numero_questao INT,
    fk_alternativa_correta INT,
    FOREIGN KEY (fk_avaliacao) REFERENCES avaliacao(id_avaliacao)
);

-- Inserts para questao
SELECT * FROM questao;

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

-- Ajuste os valores de FK_tentativa para corresponder aos id_tentativa existentes na tabela tentativa
-- Supondo que os três inserts anteriores em tentativa geraram id_tentativa 1, 2 e 3, os valores abaixo estão corretos.
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

use capacita;
select * from feedback
join usuario on feedback.fk_usuario = usuario.id_usuario;

SELECT COUNT(*) FROM feedback;
SELECT * FROM feedback WHERE fk_curso = 1;


SELECT * FROM cargo;
SELECT * FROM usuario;
SELECT * FROM curso;
SELECT * FROM matricula;
SELECT * FROM video;
SELECT * FROM apostila;
SELECT * FROM material_aluno;
SELECT * FROM avaliacao;
SELECT * FROM alternativa;
SELECT * FROM tentativa;
SELECT * FROM questao;
SELECT * FROM resposta_do_usuario;
SELECT * FROM feedback;