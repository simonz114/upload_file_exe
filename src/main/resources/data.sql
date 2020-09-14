DROP TABLE IF EXISTS meta_info;

CREATE TABLE meta_info (
  id UUID PRIMARY KEY,
  namespace VARCHAR(250) NOT NULL,
  path VARCHAR(250)
--  created VARCHAR(250) NOT NULL,
--  last_update VARCHAR(250) DEFAULT NULL
);