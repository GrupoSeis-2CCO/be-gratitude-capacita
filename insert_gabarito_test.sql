-- Script simplificado para inserir dados de teste do gabarito
-- Execute este script manualmente no MySQL

USE capacita;

-- Deletar respostas antigas do usuario 20 se existirem
DELETE FROM resposta_do_usuario WHERE fk_usuario = 20;

-- Inserir as 10 respostas do usuario 20, tentativa 40, avaliação 1
-- 6 corretas (60%) e 4 erradas (40%)
INSERT INTO resposta_do_usuario (fk_usuario, fk_curso, fk_tentativa, fk_avaliacao, fk_questao, fk_alternativa) VALUES
-- Corretas (6):
(20, 1, 40, 1, 1, 2),   -- Q1: alternativa correta
(20, 1, 40, 1, 2, 3),   -- Q2: alternativa correta
(20, 1, 40, 1, 6, 15),  -- Q6: alternativa correta
(20, 1, 40, 1, 7, 19),  -- Q7: alternativa correta
(20, 1, 40, 1, 8, 23),  -- Q8: alternativa correta
(20, 1, 40, 1, 9, 27),  -- Q9: alternativa correta

-- Erradas (4):
(20, 1, 40, 1, 10, 32), -- Q10: alternativa errada (correta seria 31)
(20, 1, 40, 1, 11, 36), -- Q11: alternativa errada (correta seria 35)
(20, 1, 40, 1, 12, 40), -- Q12: alternativa errada (correta seria 39)
(20, 1, 40, 1, 13, 43); -- Q13: alternativa errada (correta seria 44)

-- Verificar se os dados foram inseridos
SELECT 'Dados inseridos com sucesso!' AS mensagem;
SELECT COUNT(*) AS total_respostas FROM resposta_do_usuario WHERE fk_usuario = 20 AND fk_tentativa = 40;
