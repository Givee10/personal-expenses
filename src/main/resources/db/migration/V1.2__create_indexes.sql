CREATE UNIQUE INDEX code_idx ON currency (code);

CREATE INDEX from_currency_id_idx ON currency_exchange_rate (from_currency_id);
CREATE INDEX to_currency_id_idx ON currency_exchange_rate (to_currency_id);
CREATE INDEX exchange_date_idx ON currency_exchange_rate (exchange_date);

CREATE UNIQUE INDEX username_idx ON user_info (username);
CREATE UNIQUE INDEX email_idx ON user_info (email);

CREATE INDEX receipt_number_idx ON receipts (receipt_number);
CREATE INDEX receipt_date_idx ON receipts (receipt_date);
CREATE INDEX store_name_idx ON receipts (store_name);
CREATE INDEX user_id_idx ON receipts (user_id);

CREATE INDEX receipt_id_idx ON items (receipt_id);
