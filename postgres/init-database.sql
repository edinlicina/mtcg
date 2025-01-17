CREATE TABLE user_data
(
    username VARCHAR UNIQUE,
    password VARCHAR NOT NULL,
    coins    INT     NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE token
(
    token_id SERIAL,
    token    VARCHAR NOT NULL,
    username VARCHAR NOT NULL,
    PRIMARY KEY (token_id),
    FOREIGN KEY (username) REFERENCES user_data (username)
);



CREATE TABLE card
(
    name     VARCHAR        NOT NULL,
    damage   FLOAT          NOT NULL,
    id       VARCHAR UNIQUE NOT NULL,
    username VARCHAR NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (username) REFERENCES user_data (username)

);

CREATE TABLE package
(
    id    SERIAL,
    card1 VARCHAR NOT NULL,
    card2 VARCHAR NOT NULL,
    card3 VARCHAR NOT NULL,
    card4 VARCHAR NOT NULL,
    card5 VARCHAR NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (card1) REFERENCES card (id),
    FOREIGN KEY (card2) REFERENCES card (id),
    FOREIGN KEY (card3) REFERENCES card (id),
    FOREIGN KEY (card4) REFERENCES card (id),
    FOREIGN KEY (card5) REFERENCES card (id)

);

CREATE TABLE deck
(
    id       SERIAL,
    card1    VARCHAR NOT NULL, <
    card2
    VARCHAR
    NOT
    NULL,
    card3    VARCHAR NOT NULL,
    card4    VARCHAR NOT NULL,
    username VARCHAR UNIQUE,
    PRIMARY KEY (id),
    FOREIGN KEY (card1) REFERENCES card (id),
    FOREIGN KEY (card2) REFERENCES card (id),
    FOREIGN KEY (card3) REFERENCES card (id),
    FOREIGN KEY (card4) REFERENCES card (id),
    FOREIGN KEY (username) REFERENCES user_data (username)
);
