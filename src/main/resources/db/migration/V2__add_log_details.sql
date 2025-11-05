-- V2__add_log_details.sql

-- Adiciona as novas colunas à tabela tbl_logs
-- Usamos 'NOT NULL' com um 'DEFAULT' temporário para compatibilidade com dados existentes,
-- caso a tabela já tenha sido criada e populada sem essas colunas.

ALTER TABLE tbl_logs
ADD COLUMN report_id VARCHAR(255) NOT NULL DEFAULT 'N/A';

ALTER TABLE tbl_logs
ADD COLUMN level VARCHAR(50) NOT NULL DEFAULT 'INFO';

-- Para 'details', usando JSONB para PostgreSQL.
-- 'DEFAULT '{}'::jsonb' define um objeto JSON vazio como padrão.
ALTER TABLE tbl_logs
ADD COLUMN details JSONB NOT NULL DEFAULT '{}'::jsonb;

-- IMPORTANTE:
-- Se você já tem dados na tabela tbl_logs e as colunas foram adicionadas com NOT NULL,
-- o Flyway tentará preencher esses campos para as linhas existentes.
-- Os 'DEFAULT's acima farão isso automaticamente.

-- Após a conclusão da migração e a garantia de que os dados existentes estão corretos,
-- podemos remover os valores DEFAULT para que novas inserções exijam que esses campos sejam fornecidos.
-- Se você pretende sempre fornecer esses campos na sua aplicação, é uma boa prática remover o DEFAULT.
ALTER TABLE tbl_logs ALTER COLUMN report_id DROP DEFAULT;
ALTER TABLE tbl_logs ALTER COLUMN level DROP DEFAULT;
ALTER TABLE tbl_logs ALTER COLUMN details DROP DEFAULT;

-- Se você precisar adicionar uma restrição UNIQUE para report_id (baseado na sua necessidade específica):
-- ALTER TABLE tbl_logs ADD CONSTRAINT uq_tbl_logs_report_id UNIQUE (report_id);
-- Cuidado ao adicionar UNIQUE se 'N/A' for o padrão para múltiplas linhas existentes.
-- Se report_id deve ser único, garanta que seu sistema Python sempre gera IDs únicos.