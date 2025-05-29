CREATE TABLE IF NOT EXISTS tb_process (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(36) NOT NULL UNIQUE,
    opening_date TIMESTAMPTZ NOT NULL,
    status VARCHAR(10) NOT NULL,
    description TEXT,
    version BIGINT
);

CREATE TABLE IF NOT EXISTS tb_action (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(10) NOT NULL,
    registration_date TIMESTAMPTZ NOT NULL,
    description TEXT,
    process_id BIGINT NOT NULL,
    version BIGINT,
    FOREIGN KEY (process_id) REFERENCES tb_process (id)
);

CREATE TABLE IF NOT EXISTS tb_party (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    legal_entity_id VARCHAR(20),
    type VARCHAR(10) NOT NULL,
    process_id BIGINT NOT NULL,
    version BIGINT,
    FOREIGN KEY (process_id) REFERENCES tb_process (id)
);

CREATE TABLE IF NOT EXISTS tb_contact (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    party_id BIGINT NOT NULL,
    version BIGINT,
    FOREIGN KEY (party_id) REFERENCES tb_party (id)
);

CREATE INDEX idx_process_number ON tb_process(number);
CREATE INDEX idx_status ON tb_process(status);
CREATE INDEX idx_opening_date ON tb_process(opening_date);
CREATE INDEX idx_legal_entity_id ON tb_party(legal_entity_id);
CREATE INDEX idx_email ON tb_contact(email);
CREATE INDEX idx_phone ON tb_contact(phone);
