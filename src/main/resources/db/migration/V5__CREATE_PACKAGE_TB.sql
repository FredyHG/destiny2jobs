CREATE TABLE package_tb (
    id UUID PRIMARY KEY,
    final_price DOUBLE PRECISION,
    estimated_time INTEGER,
    user_id UUID,
    worker_id UUID,
    FOREIGN KEY (user_id) REFERENCES user_tb(id),
    FOREIGN KEY (worker_id) REFERENCES user_tb(id),
    status VARCHAR(255)
);