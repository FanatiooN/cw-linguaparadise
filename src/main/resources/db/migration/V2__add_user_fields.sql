-- Добавление новых полей в таблицу users
ALTER TABLE users 
ADD COLUMN birth_date DATE,
ADD COLUMN balance DECIMAL(10,2) DEFAULT 0.00; 