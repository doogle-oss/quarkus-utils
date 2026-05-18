-- Table: flyway_quarkus_history

-- DROP TABLE IF EXISTS flyway_quarkus_history;

CREATE TABLE IF NOT EXISTS flyway_quarkus_history
(
    installed_rank integer NOT NULL PRIMARY KEY,
    version varchar(50),
    description varchar(200) NOT NULL,
    type varchar(20) NOT NULL,
    script varchar(1000) NOT NULL,
    checksum integer,
    installed_by varchar(100) NOT NULL,
    installed_on timestamp without time zone NOT NULL DEFAULT now(),
    execution_time integer NOT NULL,
    success boolean NOT NULL
);

ALTER TABLE IF EXISTS flyway_quarkus_history
    OWNER to quarkus_dbo;

-- Index: flyway_quarkus_history_s_idx

-- DROP INDEX IF EXISTS flyway_quarkus_history_s_idx;

CREATE INDEX IF NOT EXISTS flyway_quarkus_history_s_idx
    ON flyway_quarkus_history (success ASC NULLS LAST);