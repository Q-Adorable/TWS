package cn.thoughtworks.school.services;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class FlywayService {

    @Value("${flyway.url}")
    private String DbUrl;

    @Value("${flyway.user}")
    private String DbUser;

    @Value("${flyway.password}")
    private String DbPassword;

    public void migrateDatabase() throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(DbUrl, DbUser, DbPassword);
        flyway.setLocations("classpath:db/migration");
        flyway.clean();
        flyway.migrate();
    }
}