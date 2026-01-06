-- V1__Create_users_table.sql
-- Initial schema for user management

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    email VARCHAR(100),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Create refresh tokens table for JWT
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);

-- Insert default users (password is BCrypt hash of "1234")
-- BCrypt hash for "1234": $2a$10$LfHRNn3XLDnjmeAXUIn8fO97R2PEzkNnTm4brf5hckONlebZ0HpHa
INSERT INTO users (username, password, role, email, first_name, last_name, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_by)
VALUES 
    ('user', '$2a$10$LfHRNn3XLDnjmeAXUIn8fO97R2PEzkNnTm4brf5hckONlebZ0HpHa', 'USER', 'user@example.com', 'Regular', 'User', true, true, true, true, 'SYSTEM'),
    ('admin', '$2a$10$LfHRNn3XLDnjmeAXUIn8fO97R2PEzkNnTm4brf5hckONlebZ0HpHa', 'ADMIN', 'admin@example.com', 'Admin', 'User', true, true, true, true, 'SYSTEM');
