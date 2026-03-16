UPDATE rinasak
SET status = 'UVIRKSOM',
    endret_bruker = 'migration-v7',
    endret_tidspunkt = NOW()
WHERE status IN ('OPPRETT_OPPGAVE', 'OPPGAVE_OPPRETTET');
