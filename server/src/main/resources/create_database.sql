CREATE DATABASE debtmanager OWNER debtmanager;
USE debtmanager;

CREATE SCHEMA debtmanager;
SET search_path TO debtmanager;

CREATE TABLE users (
   id            serial PRIMARY KEY,
   email         varchar(120) UNIQUE,
   name          varchar(60) NOT NULL,
   bank_account  numeric(22,0) NOT NULL,
   password_hash char(64) NOT NULL
);

CREATE TABLE budgets (
   id            serial PRIMARY KEY,
   name          varchar(50) NOT NULL,
   description   varchar(200)
);

CREATE TABLE user_budget (
   user_id       int REFERENCES users(id),
   budget_id     int REFERENCES budgets(id)
);