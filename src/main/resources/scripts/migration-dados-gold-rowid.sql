-- Migration: Add auto-increment primary key to dados_gold table
-- This is needed for JPA entity mapping since the original 'id' column is not unique
-- Run this ONCE on your MySQL database before starting the backend

USE WeGo;

-- Check if row_id column already exists before adding it
SET @column_exists = (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'WeGo' 
    AND TABLE_NAME = 'dados_gold' 
    AND COLUMN_NAME = 'row_id'
);

-- Only add if it doesn't exist
-- If you get an error, run these manually:
-- ALTER TABLE dados_gold ADD COLUMN row_id BIGINT AUTO_INCREMENT PRIMARY KEY FIRST;

-- Alternative: if the table has no primary key, add row_id
ALTER TABLE dados_gold ADD COLUMN row_id BIGINT AUTO_INCREMENT PRIMARY KEY FIRST;

-- Verify
SELECT COUNT(*) as total_registros FROM dados_gold;
SELECT * FROM dados_gold LIMIT 5;
