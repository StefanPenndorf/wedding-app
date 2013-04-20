# <comment me>

# --- !Ups
CREATE TABLE users (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    email varchar(255) NOT NULL,
    passwort varchar(255) NOT NULL,
    vorname varchar(50) NOT NULL,
    nachname varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY email (email),
    UNIQUE KEY name (vorname,nachname)
)


# --- !Downs
DROP TABLE users
