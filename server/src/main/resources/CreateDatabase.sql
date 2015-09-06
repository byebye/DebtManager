CREATE DATABASE debtmanager OWNER debtmanager;

CREATE SCHEMA debtmanager;
SET search_path TO debtmanager;

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS budgets CASCADE;
DROP TABLE IF EXISTS user_budget CASCADE;
DROP TABLE IF EXISTS settlements CASCADE;
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS bank_transfers CASCADE;

CREATE TABLE users (
  id            SERIAL PRIMARY KEY,
  email         VARCHAR(120) UNIQUE,
  name          VARCHAR(60) NOT NULL,
  bank_account  CHAR(22)    NOT NULL,
  password_hash CHAR(64)    NOT NULL
);

CREATE TABLE budgets (
  id          SERIAL PRIMARY KEY,
  owner_id    INT REFERENCES users (id) NOT NULL,
  name        VARCHAR(30)               NOT NULL,
  description VARCHAR(60)
);

CREATE TABLE user_budget (
  user_id   INT REFERENCES users (id),
  budget_id INT REFERENCES budgets (id),
  PRIMARY KEY (user_id, budget_id)
);

CREATE TABLE settlements (
  id          SERIAL PRIMARY KEY,
  budget_id   INT REFERENCES budgets (id) NOT NULL,
  settle_date DATE                        NOT NULL DEFAULT now()
);

CREATE TABLE payments (
  id            SERIAL PRIMARY KEY,
  budget_id     INT REFERENCES budgets (id) NOT NULL,
  payer_id      INT REFERENCES users (id),
  description   VARCHAR(200),
  amount        NUMERIC(12, 2)              NOT NULL,
  settlement_id INT REFERENCES settlements (id)      DEFAULT NULL,
  settled       BOOLEAN                              DEFAULT FALSE
);

CREATE TABLE bank_transfers (
  id            SERIAL PRIMARY KEY,
  settlement_id INT REFERENCES settlements (id) NOT NULL,
  sender        INT REFERENCES users (id)       NOT NULL,
  recipient     INT REFERENCES users (id)       NOT NULL,
  amount        NUMERIC(12, 2)                  NOT NULL,
  paid          INT DEFAULT 0
);
