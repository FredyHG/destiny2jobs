CREATE TABLE tb_token (
    id UUID PRIMARY KEY,
    token VARCHAR(255),
    token_type VARCHAR(20),
    revoked BOOLEAN DEFAULT FALSE,
    expired BOOLEAN DEFAULT FALSE,
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES user_tb(id)
);