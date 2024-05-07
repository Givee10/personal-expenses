-- Create the user_exchange table
CREATE TABLE user_exchange (
    id SERIAL PRIMARY KEY,
    from_currency_id INT NOT NULL,
    to_currency_id INT NOT NULL,
    from_amount NUMERIC(10, 6) NOT NULL,
    to_amount NUMERIC(10, 6) NOT NULL,
    rate NUMERIC(10, 6) NOT NULL,
    exchange_date DATE NOT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    user_id INT NOT NULL,
    FOREIGN KEY (from_currency_id) REFERENCES currency(id),
    FOREIGN KEY (to_currency_id) REFERENCES currency(id),
    FOREIGN KEY (user_id) REFERENCES user_info(id)
);

-- Create indexes for user_exchange table
CREATE INDEX exchange_from_currency_id_idx ON user_exchange (from_currency_id);
CREATE INDEX exchange_to_currency_id_idx ON user_exchange (to_currency_id);
CREATE INDEX exchange_user_date_idx ON user_exchange (exchange_date);
CREATE INDEX exchange_user_id_idx ON user_exchange (user_id);