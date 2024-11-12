CREATE SCHEMA IF NOT EXISTS users;

CREATE TABLE users.monday_tenant_users (
	monday_id INTEGER NOT NULL,
	tenant_id INTEGER NOT NULL,
	docspace_id VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	hash VARCHAR(255) NOT NULL,
	created_at BIGINT NOT NULL,
	updated_at BIGINT,
	version BIGINT NOT NULL default 0,
	PRIMARY KEY (monday_id, tenant_id)
);

CREATE INDEX monday_tenant_users_idx on users.monday_tenant_users (monday_id, tenant_id);
CREATE index monday_tenant_docspace_users_idx on users.monday_tenant_users (docspace_id);