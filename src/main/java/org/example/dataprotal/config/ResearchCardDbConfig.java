package org.example.dataprotal.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.example.dataprotal.repository.researchcard",
        entityManagerFactoryRef = "fifthEntityManagerFactory",
        transactionManagerRef = "fifthTransactionManager"
)
public class ResearchCardDbConfig {

    @Value("${spring.datasource.fifth.url}")
    private String fifthDbUrl;

    @Value("${spring.datasource.fifth.username}")
    private String fifthDbUsername;

    @Value("${spring.datasource.fifth.password}")
    private String fifthDbPassword;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String fifthDbDdlAuto;

    @Bean(name = "fifthDataSource")
    public DataSource fifthDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(fifthDbUrl);
        builder.username(fifthDbUsername);
        builder.password(fifthDbPassword);
        return builder.build();
    }

    @Bean(name = "fifthEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean fifthEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(fifthDataSource())
                .packages("org.example.dataprotal.model.researchcard")
                .persistenceUnit("fifth")
                .properties(hibernateProperties())
                .build();
    }

    @Bean(name = "fifthTransactionManager")
    public PlatformTransactionManager fifthTransactionManager(
            @Qualifier("fifthEntityManagerFactory") EntityManagerFactory fifthTransactionManager) {
        return new JpaTransactionManager(fifthTransactionManager);
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", fifthDbDdlAuto);
        return properties;
    }

}
