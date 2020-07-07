CREATE TABLE card (
    id BIGINT AUTO_INCREMENT NOT NULL,
    PRIMARY KEY (id),
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    number VARCHAR(25),
    valid VARCHAR(5),
    owner VARCHAR(100)
);
CREATE TABLE invoice (
    id BIGINT AUTO_INCREMENT NOT NULL,
    PRIMARY KEY (id),
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sum DOUBLE
);

CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT NOT NULL,
    PRIMARY KEY (id),
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_code VARCHAR(100),
    status_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    card_id BIGINT,
    invoice_id BIGINT,
    CONSTRAINT card_id FOREIGN KEY (card_id)
    REFERENCES card(id),
    CONSTRAINT invoice_id FOREIGN KEY (invoice_id)
    REFERENCES invoice(id)
);
