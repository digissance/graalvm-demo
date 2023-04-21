ALTER TABLE jpa_party_authentication
    ALTER COLUMN provider set default 'system',
    ALTER COLUMN provider SET NOT NULL;

ALTER TABLE jpa_party_authentication
    ADD CONSTRAINT uk_jpa_party_authentication_provider_username UNIQUE (provider, username);

