-- Safe migration to add 'anonimo' column to feedback table if missing
-- Run this once against your MySQL database 'capacita'

ALTER TABLE feedback
ADD COLUMN IF NOT EXISTS anonimo TINYINT(1) DEFAULT 0;

-- Optional: backfill existing rows as non-anonymous
UPDATE feedback SET anonimo = 0 WHERE anonimo IS NULL;
