CREATE TABLE players (
    
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    correct_hits INT NOT NULL DEFAULT 0,
    wrong_hits INT NOT NULL DEFAULT 0
);

CREATE TABLE matches (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    player_one_id BIGINT NOT NULL REFERENCES players(id),
    player_two_id BIGINT NOT NULL REFERENCES players(id),
    winner_id BIGINT NOT NULL REFERENCES players(id),
    started_at TIMESTAMP NOT NULL DEFAULT now(),
    ended_at TIMESTAMP NOT NULL,

    CHECK (player_one_id <> player_two_id),
    CHECK (winner_id IN (player_one_id, player_two_id))
);
