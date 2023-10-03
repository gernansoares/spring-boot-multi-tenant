CREATE TABLE tenantconnection
(
  id bigint NOT NULL,
  database character varying(256),
  password character varying(256),
  url character varying(256),
  username character varying(256),
  CONSTRAINT tenantconnection_pkey PRIMARY KEY (id),
  CONSTRAINT uk_5rpuueiviayai1hpxioswmm3o UNIQUE (url),
  CONSTRAINT uk_fnuyuvvbwoj9x2usprecd45fc UNIQUE (database)
);

CREATE TABLE tenant
(
  id bigint NOT NULL,
  domain character varying(256),
  name character varying(256),
  status boolean,
  connection_id bigint,
  CONSTRAINT tenant_pkey PRIMARY KEY (id),
  CONSTRAINT fknjtuay5w59ldiw61vujgoqccp FOREIGN KEY (connection_id)
      REFERENCES tenantconnection (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_10wd388mxsf0uyxs14iiu94gu UNIQUE (domain),
  CONSTRAINT uk_5q3rovgf406drx1uxl6yacksm UNIQUE (connection_id)
);

INSERT INTO tenantconnection (id, database, url, username, password) VALUES (0, 'tenant', 'jdbc:postgresql://localhost:5432/tenant', 'postgres', 'root');
INSERT INTO tenant (id, domain, name, status, connection_id) VALUES (0, 'tenant', 'Default tenant', true, 0);

