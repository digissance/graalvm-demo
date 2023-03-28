create table jpa_address
(
    address_type     varchar(31)                 not null,
    pk               bigint                      not null,
    created_by       varchar(255),
    created_date     timestamp(6) with time zone not null,
    modified_by      varchar(255),
    modified_date    timestamp(6) with time zone,
    version          bigint,
    address          varchar(255)                not null,
    email_address    varchar(255),
    city             varchar(255),
    country          varchar(255),
    region_or_state  varchar(255),
    zip_or_post_code varchar(255),
    primary key (pk)
);

create table jpa_address_property
(
    pk               bigint                      not null,
    created_by       varchar(255),
    created_date     timestamp(6) with time zone not null,
    modified_by      varchar(255),
    modified_date    timestamp(6) with time zone,
    version          bigint,
    address_pk       bigint                      not null,
    party_pk         bigint                      not null,
    party_identifier varchar(255)                not null,
    primary key (pk)
);

create table jpa_address_property_use
(
    jpa_address_property_pk bigint not null,
    use                     varchar(255)
);

create table jpa_email_password_party_authentication
(
    pk               bigint                      not null,
    created_by       varchar(255),
    created_date     timestamp(6) with time zone not null,
    modified_by      varchar(255),
    modified_date    timestamp(6) with time zone,
    version          bigint,
    party_pk         bigint                      not null,
    party_identifier varchar(255)                not null,
    email_address    varchar(255)                not null,
    password         varchar(255)                not null,
    primary key (pk)
);

create table jpa_geographic_address_address_line
(
    jpa_geographic_address_pk bigint not null,
    address_line              varchar(255)
);

create table jpa_party
(
    party_type        varchar(31)                 not null,
    pk                bigint                      not null,
    identifier        varchar(255)                not null,
    created_by        varchar(255),
    created_date      timestamp(6) with time zone not null,
    modified_by       varchar(255),
    modified_date     timestamp(6) with time zone,
    version           bigint,
    name              varchar(255),
    organization_name varchar(255),
    use               varchar(255),
    valid_from        timestamp(6) with time zone,
    valid_to          timestamp(6) with time zone,
    family_name       varchar(255),
    given_name        varchar(255),
    primary key (pk, identifier)
);

create table jpa_party_role
(
    pk               bigint                      not null,
    identifier       varchar(255)                not null,
    created_by       varchar(255),
    created_date     timestamp(6) with time zone not null,
    modified_by      varchar(255),
    modified_date    timestamp(6) with time zone,
    version          bigint,
    party_pk         bigint                      not null,
    party_identifier varchar(255)                not null,
    type_pk          bigint                      not null,
    primary key (pk, identifier)
);

create table jpa_party_role_type
(
    pk            bigint                      not null,
    created_by    varchar(255),
    created_date  timestamp(6) with time zone not null,
    modified_by   varchar(255),
    modified_date timestamp(6) with time zone,
    version       bigint,
    description   varchar(255),
    name          varchar(255)                not null,
    primary key (pk)
);

alter table if exists jpa_address
drop
constraint if exists UK_hl071froy86b1bg8netxmm2h2;

alter table if exists jpa_address
    add constraint UK_hl071froy86b1bg8netxmm2h2 unique (address);


alter table if exists jpa_address
drop
constraint if exists UK_462pjfbf31uagsnvp1kchmfj3;

alter table if exists jpa_address
    add constraint UK_462pjfbf31uagsnvp1kchmfj3 unique (email_address);
alter table if exists jpa_email_password_party_authentication
drop
constraint if exists UK_3g66ougk633bk10mn05tm1utq;


alter table if exists jpa_email_password_party_authentication
    add constraint UK_3g66ougk633bk10mn05tm1utq unique (email_address);

alter table if exists jpa_party
drop
constraint if exists UK_xw2vw6xhn5pavfk44lgsilh5;

alter table if exists jpa_party
    add constraint UK_xw2vw6xhn5pavfk44lgsilh5 unique (identifier);

alter table if exists jpa_party_role
drop
constraint if exists UK_5wtyu94x6lxshv082nglud67y;

alter table if exists jpa_party_role
    add constraint UK_5wtyu94x6lxshv082nglud67y unique (identifier);

alter table if exists jpa_party_role_type
drop
constraint if exists UK_lrgb8daupy07uaiqo72wlav7l;

alter table if exists jpa_party_role_type
    add constraint UK_lrgb8daupy07uaiqo72wlav7l unique (name);
create sequence seq start with 1 increment by 50;

alter table if exists jpa_address_property
    add constraint FKl7jdwhu2kxfsmk5fygc143k45
    foreign key (address_pk)
    references jpa_address;

alter table if exists jpa_address_property
    add constraint FKd6wh00okpc6n4k3chy6eo9jk1
    foreign key (party_pk, party_identifier)
    references jpa_party;

alter table if exists jpa_address_property_use
    add constraint FKab38kff99icudnvxdfrotgsx0
    foreign key (jpa_address_property_pk)
    references jpa_address_property;

alter table if exists jpa_email_password_party_authentication
    add constraint FK_jhjckc9c5i7f2southm5gv4rs
    foreign key (party_pk, party_identifier)
    references jpa_party;

alter table if exists jpa_geographic_address_address_line
    add constraint FK3dfmg8a1np2ybf6b2bd5egn43
    foreign key (jpa_geographic_address_pk)
    references jpa_address;

alter table if exists jpa_party_role
    add constraint FKtdonafx7aqs6g2xm07p1fg37e
    foreign key (party_pk, party_identifier)
    references jpa_party;

alter table if exists jpa_party_role
    add constraint FK6lqn66bgj9gxx52ur18s5xa9i
    foreign key (type_pk)
    references jpa_party_role_type;



INSERT INTO public.jpa_party_role_type (pk, created_by, created_date, modified_by, modified_date, version, description,
                                        name)
VALUES (202, 'anonymousUser', '2023-03-28 08:05:35.595764 +00:00', 'anonymousUser', '2023-03-28 08:05:35.595764 +00:00',
        0, 'Basic user', 'ADMIN');
INSERT INTO public.jpa_address (address_type, pk, created_by, created_date, modified_by, modified_date, version,
                                address, email_address, city, country, region_or_state, zip_or_post_code)
VALUES ('email', 52, 'anonymousUser', '2023-03-28 08:05:35.577346 +00:00', 'anonymousUser',
        '2023-03-28 08:05:35.577346 +00:00', 0, 'liccioni@gmail.com', 'liccioni@gmail.com', null, null, null, null);
INSERT INTO public.jpa_party (party_type, pk, identifier, created_by, created_date, modified_by, modified_date, version,
                              name, organization_name, use, valid_from, valid_to, family_name, given_name)
VALUES ('person', 1, 'cc083793-533b-45ab-869a-1191251d85ac', 'anonymousUser', '2023-03-28 08:05:35.536029 +00:00',
        'anonymousUser', '2023-03-28 08:05:35.536029 +00:00', 0, 'Gustavo,Rodriguez Liccioni', null, null, null, null,
        'Rodriguez Liccioni', 'Gustavo');
INSERT INTO public.jpa_address_property (pk, created_by, created_date, modified_by, modified_date, version, address_pk,
                                         party_pk, party_identifier)
VALUES (2, 'anonymousUser', '2023-03-28 08:05:35.570729 +00:00', 'anonymousUser', '2023-03-28 08:05:35.570729 +00:00',
        0, 52, 1, 'cc083793-533b-45ab-869a-1191251d85ac');
INSERT INTO public.jpa_address_property_use (jpa_address_property_pk, use)
VALUES (2, 'authentication');
INSERT INTO public.jpa_email_password_party_authentication (pk, created_by, created_date, modified_by, modified_date,
                                                            version, party_pk, party_identifier, email_address,
                                                            password)
VALUES (102, 'anonymousUser', '2023-03-28 08:05:35.586030 +00:00', 'anonymousUser', '2023-03-28 08:05:35.586030 +00:00',
        0, 1, 'cc083793-533b-45ab-869a-1191251d85ac', 'liccioni@gmail.com',
        '{bcrypt}$2a$10$yJic2EXDEYoooTwEhSmCRO.WXq2PHLjLFjrsld1Qpu9fvSZ3vN0ky');
INSERT INTO public.jpa_party_role (pk, identifier, created_by, created_date, modified_by, modified_date, version,
                                   party_pk, party_identifier, type_pk)
VALUES (152, '3396b5a0-c021-45b1-909a-516a0d307978', 'anonymousUser', '2023-03-28 08:05:35.591381 +00:00',
        'anonymousUser', '2023-03-28 08:05:35.591381 +00:00', 0, 1, 'cc083793-533b-45ab-869a-1191251d85ac', 202);