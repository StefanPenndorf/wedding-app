# Neue Tabelle Fotoalben für die Fotos

# --- !Ups
CREATE TABLE fotos (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    besitzer bigint(20) NOT NULL,
    foto LONGBLOB NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (besitzer) REFERENCES users (id) ON DELETE CASCADE
)


# --- !Downs
DROP TABLE fotos
