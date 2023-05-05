package com.collegeproject.Testmate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class QueryRunner implements ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        jdbcTemplate.execute("ALTER TABLE SPRING_SESSION_ATTRIBUTES MODIFY ATTRIBUTE_BYTES LONGBLOB;");

        jdbcTemplate.execute("ALTER TABLE organisers MODIFY image LONGBLOB;");
    }
}
