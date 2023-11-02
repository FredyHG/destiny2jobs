CREATE TABLE user_tb (
    id UUID PRIMARY KEY,
    name VARCHAR(20),
    last_name VARCHAR(20),
    email VARCHAR(255),
    password VARCHAR(255),
    discord_name VARCHAR(255),
    balance DOUBLE PRECISION,
    create_at TIMESTAMP,
    role VARCHAR(255)
);