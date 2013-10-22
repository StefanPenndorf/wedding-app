# Neue Spalte für Sortierung der Fotos im Album

# --- !Ups
ALTER TABLE fotos
   ADD COLUMN position INT NOT NULL DEFAULT '0';

UPDATE `fotos` as f1 SET
 f1.position = (Select count(*) from (SELECT * FROM fotos) as f2 where f1.besitzer=f2.besitzer AND f2.id<=f1.id );

ALTER TABLE fotos
   ALTER COLUMN position DROP DEFAULT;

# --- !Downs
ALTER TABLE fotos
   DROP COLUMN position;
