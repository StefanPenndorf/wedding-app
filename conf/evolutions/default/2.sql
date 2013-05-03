# TODO <comment me>

# --- !Ups
ALTER TABLE users ADD COLUMN passwort CHAR(60)

# --- !Downs
ALTER TABLE users DROP COLUMN passwort
