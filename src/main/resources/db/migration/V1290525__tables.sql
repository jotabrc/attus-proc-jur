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
    legal_entity_id VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    type VARCHAR(10) NOT NULL,
    version BIGINT
);

CREATE TABLE IF NOT EXISTS tb_party_process (
    party_id BIGINT NOT NULL,
    process_id BIGINT NOT NULL,
    PRIMARY KEY (party_id, process_id),
    FOREIGN KEY (party_id) REFERENCES tb_party (id) ON DELETE CASCADE,
    FOREIGN KEY (process_id) REFERENCES tb_process (id) ON DELETE CASCADE
);

CREATE INDEX idx_process_number ON tb_process(number);
CREATE INDEX idx_status ON tb_process(status);
CREATE INDEX idx_opening_date ON tb_process(opening_date);
CREATE INDEX idx_legal_entity_id ON tb_party(legal_entity_id);
