-- CREATE DATABASE debtmanager OWNER debtmanager;

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

CREATE INDEX users_email_index ON users (email);

CREATE TABLE budgets (
  id          SERIAL PRIMARY KEY,
  owner_id    INT REFERENCES users (id) NOT NULL,
  name        VARCHAR(30)               NOT NULL,
  description VARCHAR(60)
);

CREATE TABLE user_budget (
  user_id   INT REFERENCES users (id) ON DELETE CASCADE,
  budget_id INT REFERENCES budgets (id) ON DELETE CASCADE NOT NULL,
  PRIMARY KEY (user_id, budget_id)
);

CREATE TABLE settlements (
  id          SERIAL PRIMARY KEY,
  budget_id   INT REFERENCES budgets (id) ON DELETE CASCADE NOT NULL,
  settle_date DATE                                          NOT NULL DEFAULT now()
);

CREATE TABLE payments (
  id            SERIAL PRIMARY KEY,
  budget_id     INT REFERENCES budgets (id) ON DELETE CASCADE NOT NULL,
  payer_id      INT REFERENCES users (id) ON DELETE SET NULL,
  description   VARCHAR(200),
  amount        NUMERIC(12, 2)                                NOT NULL,
  settlement_id INT REFERENCES settlements (id) ON DELETE NO ACTION DEFAULT NULL,
  settled       BOOLEAN                                             DEFAULT FALSE
);

CREATE INDEX payments_budget_index ON payments (budget_id);
CREATE INDEX payments_settlement_index ON payments (settlement_id);

CREATE TABLE bank_transfers (
  id            SERIAL PRIMARY KEY,
  settlement_id INT REFERENCES settlements (id) ON DELETE NO ACTION NOT NULL,
  sender        INT REFERENCES users (id) ON DELETE SET NULL,
  recipient     INT REFERENCES users (id) ON DELETE SET NULL,
  amount        NUMERIC(12, 2)                                      NOT NULL,
  status        INT DEFAULT 0 CHECK (status IN (0, 1, 2))
);

CREATE INDEX bank_transfers_settlement_index ON bank_transfers (settlement_id);
CREATE INDEX bank_transfers_sender_index ON bank_transfers (sender);
CREATE INDEX bank_transfers_recipient_index ON bank_transfers (recipient);
