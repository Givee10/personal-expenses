ALTER TABLE currency_exchange_rate ALTER COLUMN rate TYPE NUMERIC(16, 6);

ALTER TABLE receipts ADD COLUMN currency_id INT NOT NULL;
ALTER TABLE receipts ALTER COLUMN total_amount TYPE NUMERIC(22, 2);
ALTER TABLE receipts ADD CONSTRAINT receipts_currency_id_fkey FOREIGN KEY (currency_id) REFERENCES currency(id);

ALTER TABLE items ALTER COLUMN unit_price TYPE NUMERIC(22, 2);
ALTER TABLE items ALTER COLUMN total_price TYPE NUMERIC(22, 2);