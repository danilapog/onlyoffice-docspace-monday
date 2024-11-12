CREATE SCHEMA IF NOT EXISTS tenants;

CREATE TABLE tenants.monday_outbox (
	id VARCHAR(255) NOT NULL,
	payload JSONB,
	type VARCHAR(255) CHECK (type IN ('REFRESH','INVITE','CREATE_USER_ON_INITIALIZATION','REMOVE_TENANT_USERS')),
	created_at bigint NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE tenants.monday_tenant_docspace (
	id VARCHAR(255) NOT NULL,
	hash VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	url VARCHAR(255) NOT NULL,
	tenant_id INTEGER,
	created_at BIGINT NOT NULL,
	updated_at BIGINT,
	PRIMARY KEY (id)
);

CREATE TABLE tenants.monday_tenant_registered_boards (
	id INTEGER NOT NULL,
	access_key VARCHAR(255),
	room_id INTEGER NOT NULL,
	tenant_id INTEGER NOT NULL,
	created_at BIGINT NOT NULL,
	updated_at BIGINT,
	PRIMARY KEY (id)
);

CREATE TABLE tenants.monday_tenants (
	id INTEGER NOT NULL,
	created_at BIGINT NOT NULL,
	updated_at BIGINT,
	PRIMARY KEY (id)
);

CREATE INDEX monday_tenant_docspace_idx on tenants.monday_tenant_docspace (url, tenant_id);
ALTER TABLE IF EXISTS tenants.monday_tenant_docspace ADD CONSTRAINT UK_tenant_id UNIQUE (tenant_id);
CREATE INDEX monday_tenant_boards_idx on tenants.monday_tenant_registered_boards (room_id, tenant_id);
ALTER TABLE IF EXISTS tenants.monday_tenant_docspace ADD CONSTRAINT FK_docspace_monday_tenant FOREIGN KEY (tenant_id) REFERENCES tenants.monday_tenants;
ALTER TABLE IF EXISTS tenants.monday_tenant_registered_boards ADD CONSTRAINT FK_board_monday_tenant FOREIGN KEY (tenant_id) REFERENCES tenants.monday_tenants;