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
   budget_id     int REFERENCES budgets(id),
   PRIMARY KEY(user_id, budget_id)
);

CREATE TABLE settlements(
   id             serial PRIMARY KEY,
   budget_id      int REFERENCES budgets(id) NOT NULL,
   term           date NOT NULL DEFAULT now()
);

CREATE TABLE payments(
   id            serial PRIMARY KEY,
   term          date NOT NULL DEFAULT now(),
   budget_id     int REFERENCES budgets(id) NOT NULL,
   user_id       int REFERENCES users(id),
   description   varchar(200),
   amount        numeric(6,2) NOT NULL,
   settlement_id int REFERENCES settlements(id) DEFAULT NULL,
   accounted     boolean DEFAULT FALSE
);

CREATE TABLE bank_transfers (
   id            serial PRIMARY KEY,
   settle_id     int REFERENCES settlements(id) NOT NULL,
   who           int REFERENCES users(id) NOT NULL,
   whom          int REFERENCES users(id) NOT NULL,
   amount        numeric(6, 2) NOT NULL,
   paid          boolean DEFAULT FALSE
);