-- create webauthn credential table
create table if not exists webauthn_credential (
    credential_id text primary key,
    public_key bytea,
    public_key_algorithm bigint not null,
    counter bigint not null,
    aaguid uuid,
    user_id bigint not null,
    constraint fk_webauthn_user foreign key (user_id)
        references auth_user (id) on delete cascade
);
