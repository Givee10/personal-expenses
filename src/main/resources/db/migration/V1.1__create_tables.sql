-- Create the currency table
CREATE TABLE currency (
    id SERIAL PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create the currency_exchange_rate table
CREATE TABLE currency_exchange_rate (
    id SERIAL PRIMARY KEY,
    from_currency_id INT NOT NULL,
    to_currency_id INT NOT NULL,
    rate NUMERIC(10, 6) NOT NULL,
    exchange_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (from_currency_id) REFERENCES currency(id),
    FOREIGN KEY (to_currency_id) REFERENCES currency(id)
);

-- Create the user_info table
CREATE TABLE user_info (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    last_login TIMESTAMP
);

-- Create the receipts table
CREATE TABLE receipts (
    id SERIAL PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL,
    receipt_date DATE NOT NULL,
    store_name VARCHAR(255) NOT NULL,
    total_amount NUMERIC(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT current_timestamp,
    FOREIGN KEY (user_id) REFERENCES user_info(id)
);

-- Create the items table
CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    receipt_id INT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (receipt_id) REFERENCES receipts(id)
);