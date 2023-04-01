create table jpa_address
(
    address_type varchar(31) not null,
    pk bigint not null
        constraint jpa_address_pkey
            primary key,
    created_by varchar(255),
    created_date timestamp(6) with time zone,
    modified_by varchar(255),
    modified_date timestamp(6) with time zone,
    version bigint,
    address varchar(255) not null
        constraint uk_hl071froy86b1bg8netxmm2h2
            unique,
    email_address varchar(255)
        constraint uk_462pjfbf31uagsnvp1kchmfj3
            unique,
    city varchar(255),
    country varchar(255),
    region_or_state varchar(255),
    zip_or_post_code varchar(255)
);

create table jpa_geographic_address_address_line
(
    jpa_geographic_address_pk bigint not null
        constraint fk3dfmg8a1np2ybf6b2bd5egn43
            references jpa_address,
    address_line varchar(255)
);

create table jpa_party
(
    party_type varchar(31) not null,
    pk bigint not null,
    identifier varchar(255) not null
        constraint uk_xw2vw6xhn5pavfk44lgsilh5
            unique,
    created_by varchar(255),
    created_date timestamp(6) with time zone,
    modified_by varchar(255),
    modified_date timestamp(6) with time zone,
    version bigint,
    name varchar(255),
    organization_name varchar(255),
    use varchar(255),
    valid_from timestamp(6) with time zone,
    valid_to timestamp(6) with time zone,
    family_name varchar(255),
    given_name varchar(255),
    constraint jpa_party_pkey
        primary key (pk, identifier)
);

create table jpa_address_property
(
    pk bigint not null
        constraint jpa_address_property_pkey
            primary key,
    created_by varchar(255),
    created_date timestamp(6) with time zone,
    modified_by varchar(255),
    modified_date timestamp(6) with time zone,
    version bigint,
    address_pk bigint not null
        constraint fkl7jdwhu2kxfsmk5fygc143k45
            references jpa_address,
    party_pk bigint not null,
    party_identifier varchar(255) not null,
    constraint fkd6wh00okpc6n4k3chy6eo9jk1
        foreign key (party_pk, party_identifier) references jpa_party
);

create table jpa_address_property_use
(
    jpa_address_property_pk bigint not null
        constraint fkab38kff99icudnvxdfrotgsx0
            references jpa_address_property,
    use varchar(255)
);

create table jpa_email_password_party_authentication
(
    pk bigint not null
        constraint jpa_email_password_party_authentication_pkey
            primary key,
    created_by varchar(255),
    created_date timestamp(6) with time zone,
    modified_by varchar(255),
    modified_date timestamp(6) with time zone,
    version bigint,
    username varchar(255) not null
        constraint uk_adfmqpbqmq6fs9koswx9vixaa
            unique,
    party_pk bigint not null,
    party_identifier varchar(255) not null,
    password varchar(255) not null,
    constraint fk_jhjckc9c5i7f2southm5gv4rs
        foreign key (party_pk, party_identifier) references jpa_party
);

create table jpa_oidc_party_authentication
(
    pk bigint not null
        constraint jpa_oidc_party_authentication_pkey
            primary key,
    created_by varchar(255),
    created_date timestamp(6) with time zone,
    modified_by varchar(255),
    modified_date timestamp(6) with time zone,
    version bigint,
    username varchar(255) not null
        constraint uk_6yt85yiy3yn21lfe95nxvoy1c
            unique,
    party_pk bigint not null,
    party_identifier varchar(255) not null,
    provider varchar(255) not null,
    constraint fk_ici6eo0f66tes9f73ygo24qgr
        foreign key (party_pk, party_identifier) references jpa_party
);

create table jpa_party_role_type
(
    pk bigint not null
        constraint jpa_party_role_type_pkey
            primary key,
    created_by varchar(255),
    created_date timestamp(6) with time zone,
    modified_by varchar(255),
    modified_date timestamp(6) with time zone,
    version bigint,
    description varchar(255),
    name varchar(255) not null
        constraint uk_lrgb8daupy07uaiqo72wlav7l
            unique
);

create table jpa_party_role
(
    pk bigint not null,
    identifier varchar(255) not null
        constraint uk_5wtyu94x6lxshv082nglud67y
            unique,
    created_by varchar(255),
    created_date timestamp(6) with time zone,
    modified_by varchar(255),
    modified_date timestamp(6) with time zone,
    version bigint,
    party_pk bigint not null,
    party_identifier varchar(255) not null,
    type_pk bigint not null
        constraint fk6lqn66bgj9gxx52ur18s5xa9i
            references jpa_party_role_type,
    constraint jpa_party_role_pkey
        primary key (pk, identifier),
    constraint fktdonafx7aqs6g2xm07p1fg37e
        foreign key (party_pk, party_identifier) references jpa_party
);

create table session
(
    pk bigint not null
        constraint session_pkey
            primary key,
    deleted boolean not null,
    expires_at timestamp(6) with time zone,
    issued_at timestamp(6) with time zone,
    last_used_at timestamp(6) with time zone,
    removed_at timestamp(6) with time zone,
    session_id varchar(255),
    token varchar(255),
    user_id varchar(255)
);
