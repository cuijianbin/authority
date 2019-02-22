package com.authority.datasourceconfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {
	//配置数据源，对应application.properties中的配置,test库
	@Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
	@Primary//如果是多数据源，必须写其中一个主键，随便某一个数据源都可以
    @ConfigurationProperties(prefix="spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

	//common库
    @Bean(name = "secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @ConfigurationProperties(prefix="spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    //zdr_data库
    @Bean(name = "thirddaryDataSource")
    @Qualifier("thirddaryDataSource")
    @ConfigurationProperties(prefix="spring.datasource.thirddary")
    public DataSource thirddaryDataSource() {
    	return DataSourceBuilder.create().build();
    }
    
    //配置操作数据库的JdbcTemplate,test库
    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    //common库
    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    //zdr_data库
    @Bean(name = "thirddaryJdbcTemplate")
    public JdbcTemplate thirddaryJdbcTemplate(@Qualifier("thirddaryDataSource") DataSource dataSource) {
    	return new JdbcTemplate(dataSource);
    }
}
