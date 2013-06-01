# TODO <comment me>

# --- !Ups
ALTER TABLE users ADD COLUMN istAdmin BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE users SET istAdmin = TRUE WHERE email = 'stefan@cyphoria.net';

# --- !Downs
ALTER TABLE users DROP COLUMN istAdmin;
