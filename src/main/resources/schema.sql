CREATE TABLE IF NOT EXISTS mpa_rating (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(5) NOT NULL
    );

CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa INTEGER NOT NULL,
    CONSTRAINT mpa_rating_fk FOREIGN KEY (mpa)
    REFERENCES mpa_rating (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS films_genres (
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    CONSTRAINT film_id_fk FOREIGN KEY (film_id)
    REFERENCES films (id) ON DELETE CASCADE,
    CONSTRAINT genre_id_fk FOREIGN KEY (genre_id)
    REFERENCES genres (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    login varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    birthday DATE NOT NULL
    );

CREATE TABLE IF NOT EXISTS likes (
    user_id INTEGER NOT NULL,
    film_id INTEGER NOT NULL,
    CONSTRAINT user_likes_fk FOREIGN KEY (user_id)
    REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT film_likes_fk FOREIGN KEY (film_id)
    REFERENCES films (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS relationships (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS friends (
    user_id INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    relationship_id INTEGER,
    CONSTRAINT user_from_fk FOREIGN KEY (user_id)
    REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT user_to_fk FOREIGN KEY (friend_id)
    REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT relationship_id_fk FOREIGN KEY (relationship_id)
    REFERENCES relationships (id) ON DELETE RESTRICT
    );



