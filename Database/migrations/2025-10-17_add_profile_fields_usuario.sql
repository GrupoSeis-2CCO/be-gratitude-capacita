-- Safe migration to add profile-related columns if they don't exist
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS telefone VARCHAR(20) NULL;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS departamento VARCHAR(100) NULL;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS foto_url VARCHAR(255) NULL;
