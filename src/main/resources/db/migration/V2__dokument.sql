CREATE TABLE dokument
(
    dokument_uuid       uuid primary key,
    rinasak_id          integer   not null,
    sed_id              uuid      not null,
    sed_versjon         int       not null,
    sed_type            text      not null,
    status              text      not null,
    opprettet_bruker    text      not null,
    opprettet_tidspunkt timestamp not null,
    endret_bruker       text      not null,
    endret_tidspunkt    timestamp not null
);
ALTER TABLE dokument
    ADD UNIQUE (sed_id, sed_versjon);
