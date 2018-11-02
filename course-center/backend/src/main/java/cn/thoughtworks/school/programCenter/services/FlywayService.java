package cn.thoughtworks.school.programCenter.services;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class FlywayService {

    @Value("${flyway.url}")
    private String dbUrl;

    @Value("${flyway.user}")
    private String dbUser;

    @Value("${flyway.password}")
    private String dbPassword;

    @Value("${flyway.locations}")
    private String dbLocations;

    public void migrateDatabase() throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dbUrl,dbUser, dbPassword);
        flyway.setLocations(dbLocations);
        flyway.clean();
        flyway.migrate();
    }
}