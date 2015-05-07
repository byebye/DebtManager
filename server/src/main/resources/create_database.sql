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
   owner_id      int REFERENCES users(id) NOT NULL,
   name          varchar(50) NOT NULL,
   description   varchar(200)
);

CREATE TABLE user_budget (
   user_id       int REFERENCES users(id),
   budget_id     int REFERENCES budgets(id)
);

CREATE TABLE payments(
   id            int PRIMARY KEY,
   term          date NOT NULL,
   budget_id     int REFERENCES budgets(id) NOT NULL,
   user_id       int REFERENCES users(id),
   description   varchar(200),
   amount        numeric(6,2) NOT NULL,
   accounted     boolean DEFAULT FALSE
);