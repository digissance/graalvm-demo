drop table if exists jpa_party_authentication;
create table jpa_party_authentication
(
    auth_type        varchar(31)  not null,
    pk               bigserial       not null primary key,
    created_by       varchar(255),
    created_date     timestamp(6) with time zone,
    modified_by      varchar(255),
    modified_date    timestamp(6) with time zone,
    version          bigint,
    username         varchar(255) not null,
    password         varchar(255),
    provider         varchar(255),
    party_pk         bigint       not null,
    party_identifier varchar(255) not null,
    constraint fk_jpa_party_authentication_party
        foreign key (party_pk, party_identifier) references jpa_party
);

insert into jpa_party_authentication (auth_type, created_by, created_date, modified_by, modified_date, version,
                                      username, provider, party_pk, party_identifier)
select 'oidc',
       created_by,
       created_date,
       modified_by,
       modified_date,
       0,
       username,
       provider,
       party_pk,
       party_identifier
from jpa_oidc_party_authentication;

insert into jpa_party_authentication (auth_type, created_by, created_date, modified_by, modified_date, version,
                                      username, password, party_pk, party_identifier)
select 'email_password',
       created_by,
       created_date,
       modified_by,
       modified_date,
       0,
       username,
       password,
       party_pk,
       party_identifier
from jpa_email_password_party_authentication;

drop table jpa_oidc_party_authentication;
drop table jpa_email_password_party_authentication;