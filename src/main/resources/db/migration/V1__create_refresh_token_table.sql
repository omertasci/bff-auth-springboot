CREATE TABLE refresh_tokens (
    id NVARCHAR(36) PRIMARY KEY,
    created_date DATETIME2 NOT NULL,
    created_by NVARCHAR(255) NULL,
    last_modified_date DATETIME2 NULL,
    last_modified_by NVARCHAR(255) NULL,
    username NVARCHAR(255) NOT NULL,
    domain NVARCHAR(255) NULL,
    device_id NVARCHAR(255) NULL,
    token_hash NVARCHAR(512) NULL,
    expires_at DATETIME2 NULL,
    revoked BIT DEFAULT 0,
    family_id NVARCHAR(255) NULL
);

