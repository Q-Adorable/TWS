package cn.thoughtworks.school.services;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class FlywayService {

    @Value("${database.url}")
    private String dbUrl;

    @Value("${database.username}")
    private String dbUser;

    @Value("${database.password}")
    private String dbPassword;

    public void clean() throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dbUrl, dbUser, dbPassword);
        flyway.clean();
    }
}