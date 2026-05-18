package org.doogleoss.flyway;

import org.flywaydb.core.Flyway;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MigrationService {

    private final Flyway flyway; 

    public MigrationService(Flyway flyway) {
        this.flyway = flyway;
    }

    public void checkMigration() {
        Log.info(flyway.info().current().getVersion().toString());
    }
}
