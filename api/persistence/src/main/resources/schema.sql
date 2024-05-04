CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN      NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT current_timestamp,
    unique (username)
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT NOT NULL,
    role    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS detectors
(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    owner_id INT,
    is_online bool default false not null,
    version varchar(10) default '1' not null,
    name varchar(30) not null,
    description varchar(255),
    FOREIGN KEY (user_id) REFERENCES users (id) on DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES users (id),
    unique (user_id)
);

CREATE TABLE IF NOT EXISTS signals
(
    id SERIAL PRIMARY KEY,
    detector_id INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    is_heartbeat boolean NOT NULL,
    FOREIGN KEY (detector_id) REFERENCES detectors (id) on DELETE CASCADE
);
