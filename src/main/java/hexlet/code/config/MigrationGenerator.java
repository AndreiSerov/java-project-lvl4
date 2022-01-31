package hexlet.code.config;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;

import java.io.IOException;

/**
 * @author andreiserov
 */
public class MigrationGenerator {

    /**
     * Generate the next "DB schema DIFF" migration.
     */
    public static void main(String[] args) throws IOException {

        DbMigration dbMigration = DbMigration.create();

        dbMigration.addPlatform(Platform.POSTGRES);
        dbMigration.addPlatform(Platform.H2);

        dbMigration.generateMigration();
    }
}

