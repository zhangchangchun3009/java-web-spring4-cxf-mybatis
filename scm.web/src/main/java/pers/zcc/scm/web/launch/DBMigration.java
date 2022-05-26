package pers.zcc.scm.web.launch;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.jdbc.MysqlDataSource;

import pers.zcc.scm.common.util.EnvironmentProps;

public class DBMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBMigration.class);

    public static void main(String[] args) {
        migrateDbByFlyway();
    }

    public static void migrateDbByFlyway() {
        try {
            System.setProperty("spring.profiles.active", "dev");
            String url = EnvironmentProps.getAppPropAsString("scm.datasource.jdbc.ds1.write.url", "");
            String user = EnvironmentProps.getAppPropAsString("scm.datasource.jdbc.ds1.write.username", "");
            String password = EnvironmentProps.getAppPropAsString("scm.datasource.jdbc.ds1.write.password", "");
            MysqlDataSource ds = new MysqlDataSource();
            ds.setUrl(url);
            ds.setUser(user);
            ds.setPassword(password);
            Flyway flyway = Flyway.configure().dataSource(ds).load();
            flyway.baseline();
            MigrateResult res = flyway.migrate();
            if (res.success) {
                LOGGER.info("flyway.migrate db success.");
            } else {
                LOGGER.error("flyway.migrate db failed, list -- :");
                res.migrations.forEach(out -> {
                    LOGGER.error("file name: {}, description: {}, script version : {}", out.filepath, out.description,
                            out.version);
                });
            }
        } catch (Exception e) {
            LOGGER.error("flyway.migrate db occured excption,", e);
        }
    }

}
