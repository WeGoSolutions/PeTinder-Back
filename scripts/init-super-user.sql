INSERT INTO usuario (id, nome, email, senha)
SELECT UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')), 'super', 'super@admin.com', 'senhaSuperHash'
    WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE id = UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', ''))
);