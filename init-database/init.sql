-- Tạo databases
CREATE DATABASE "TaskRacer_test";

-- Kết nối vào database taskracer
\c "TaskRacer_test";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- Tạo schema cần thiết
CREATE SCHEMA liquibase;
CREATE SCHEMA social;

-- Kiểm tra danh sách databases và schemas
SELECT datname FROM pg_database;
SELECT schema_name FROM information_schema.schemata;
