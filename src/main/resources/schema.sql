create TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

create TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  requestor_id BIGINT,
  CONSTRAINT fk_request_id FOREIGN KEY (requestor_id) REFERENCES users (id),
  CONSTRAINT pk_requests PRIMARY KEY (id)

);

create TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  available BOOLEAN,
  owner_id BIGINT,
  requests_id BIGINT,
  CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users(id),
  CONSTRAINT fk_requests_id FOREIGN KEY (requests_id) REFERENCES requests(id),
  CONSTRAINT pk_items PRIMARY KEY (id)
);

--create TABLE IF NOT EXISTS bookings (
--  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--  start TIMESTAMP WITHOUT TIME ZONE,
--  end_time TIMESTAMP WITHOUT TIME ZONE,
--  item_id BIGINT REFERENCES items(id),
--  booker_id BIGINT REFERENCES users(id),
--  status VARCHAR,
--  CONSTRAINT pk_bookings PRIMARY KEY (id)
--);
--
--create TABLE IF NOT EXISTS comments (
--  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--  text VARCHAR(255) NOT NULL,
--  item_id BIGINT REFERENCES items(id),
--  authot_id BIGINT REFERENCES users(id),
--  CONSTRAINT pk_comments PRIMARY KEY (id)
--);