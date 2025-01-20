CREATE TABLE user_data
(
    username VARCHAR UNIQUE,
    password VARCHAR NOT NULL,
    coins    INT     NOT NULL,
    name  VARCHAR NULL,
    bio   VARCHAR NULL,
    image VARCHAR NULL,
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
    trade_id VARCHAR UNIQUE NULL,
    username VARCHAR NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (username) REFERENCES user_data (username)


);

CREATE TABLE trade
(
    id      VARCHAR UNIQUE NOT NULL,
    type    VARCHAR        NOT NULL,
    min_dmg FLOAT          NOT NULL,
    card_id VARCHAR UNIQUE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (card_id) REFERENCES card (id)
);

ALTER TABLE card
    ADD FOREIGN KEY (trade_id) REFERENCES trade (id);

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
    card1 VARCHAR NOT NULL,
    card2 VARCHAR NOT NULL,
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
