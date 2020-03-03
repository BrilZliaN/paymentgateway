CREATE TABLE card (
    id INT NOT NULL,
    PRIMARY KEY (id),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    number VARCHAR(25),
    valid VARCHAR(5),
	owner VARCHAR(100)
);
CREATE TABLE invoice (
    id INT NOT NULL,
    PRIMARY KEY (id),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sum DOUBLE
);

CREATE TABLE transaction (
    id INT NOT NULL,
    PRIMARY KEY (id),
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_code INT,
    FK_card_id INT,
    FK_invoice_id INT,
    CONSTRAINT FK_card_id FOREIGN KEY (id)
    REFERENCES card(id),
    CONSTRAINT FK_invoice_id FOREIGN KEY (id)
    REFERENCES invoice(id)
);