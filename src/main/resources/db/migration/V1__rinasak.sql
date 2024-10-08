CREATE TABLE rinasak
(
    rinasak_status_uuid uuid primary key,
    rinasak_id          integer   not null,
    status              text      not null,
    buc_type            text      not null,
    er_sakseier         boolean   not null,
    opprettet_bruker    text      not null,
    opprettet_tidspunkt timestamp not null,
    endret_bruker       text      not null,
    endret_tidspunkt    timestamp not null
);
CREATE INDEX ON rinasak (status);
CREATE UNIQUE INDEX ON rinasak (rinasak_id);
