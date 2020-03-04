CREATE TABLE card (
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    number VARCHAR(25),
    valid VARCHAR(5),
	owner VARCHAR(100)
);
CREATE TABLE invoice (
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sum DOUBLE
);

CREATE TABLE transaction (
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_code INT,
    status_date TIMESTAMP,
    card_id INT,
    invoice_id INT,
    CONSTRAINT card_id FOREIGN KEY (id)
    REFERENCES card(id),
    CONSTRAINT invoice_id FOREIGN KEY (id)
    REFERENCES invoice(id)
);