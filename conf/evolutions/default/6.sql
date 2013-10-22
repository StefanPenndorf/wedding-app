# Index für Sortierung der Fotos im Album

# --- !Ups
CREATE UNIQUE INDEX fotoalbum_reihenfolge
    ON fotos (besitzer, position);


# --- !Downs
DROP INDEX fotoalbum_reihenfolge ON fotos;