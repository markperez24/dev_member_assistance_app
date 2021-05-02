package com.member.assistance.backend.configuration;

import com.member.assistance.backend.audit.AuditorAwareServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@EnableJpaRepositories(
        basePackages = "com.member.assistance.backend",
        entityManagerFactoryRef = "maEntityManager",
        transactionManagerRef = "maTransactionManager"
)
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DatabaseConfiguration {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseConfiguration.class);

    @Autowired
    private Environment env;

    @Bean(name = "maEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean maEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(maDataSource());
        em.setPackagesToScan(
                new String[]{"com.member.assistance.backend"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect",
                env.getProperty("spring.jpa.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @Primary
    public DataSource maDataSource() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        try {
            dataSource.setDriverClassName(
                    env.getProperty("spring.datasource.driver-class-name"));
            dataSource.setUrl(env.getProperty("spring.datasource.url"));
            dataSource.setUsername(env.getProperty("spring.datasource.username"));
            dataSource.setPassword(env.getProperty("spring.datasource.password"));

        } catch (NullPointerException npe) {
            LOGGER.error(npe.getMessage());
        }
        return dataSource;
    }

    @Bean(name = "maTransactionManager")
    @Primary
    public PlatformTransactionManager maTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                maEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareServiceImpl();
    }
}
