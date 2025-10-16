-- Script para verificar dados de resposta_do_usuario
USE capacita;

-- Verificar se existem respostas do usuário 20 na tentativa 40
SELECT 'Respostas usuario 20 tentativa 40:' AS info;
SELECT * FROM resposta_do_usuario 
WHERE fk_usuario = 20 AND fk_tentativa = 40 AND fk_avaliacao = 1;

-- Contar total de respostas
SELECT 'Total de respostas no banco:' AS info;
SELECT COUNT(*) AS total FROM resposta_do_usuario;

-- Verificar tentativas do usuário 20
SELECT 'Tentativas do usuario 20:' AS info;
SELECT * FROM tentativa WHERE fk_usuario = 20;

-- Verificar matricula do usuário 20
SELECT 'Matriculas do usuario 20:' AS info;
SELECT * FROM matricula WHERE fk_usuario = 20;

-- Verificar estrutura completa para debugging
SELECT 'Dados completos usuario 20, curso 1, tentativa 40:' AS info;
SELECT 
  r.fk_usuario, r.fk_curso, r.fk_tentativa, 
  r.fk_avaliacao, r.fk_questao, r.fk_alternativa,
  q.numero_questao, q.fk_alternativa_correta,
  CASE WHEN r.fk_alternativa = q.fk_alternativa_correta THEN 'CORRETO' ELSE 'ERRADO' END AS resultado
FROM resposta_do_usuario r
JOIN questao q ON r.fk_questao = q.id_questao AND r.fk_avaliacao = q.fk_avaliacao
WHERE r.fk_usuario = 20 AND r.fk_curso = 1 AND r.fk_tentativa = 40
ORDER BY q.numero_questao;
