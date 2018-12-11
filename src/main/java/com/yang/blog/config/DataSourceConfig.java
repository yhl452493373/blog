package com.yang.blog.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.yhl452493373.bean.DataSourceGroup;
import com.github.yhl452493373.bean.DataSourceProperties;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
@Configuration
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {
    static final String PACKAGE = "com.yang.blog.mapper";
    static final String MAPPER_LOCATION = "classpath:/mapper/*.xml";

    @Bean
    public DataSource dataSource(DataSourceGroup dataSourceGroup) {
        DataSourceProperties dataSourceProperties = null;
        if (dataSourceGroup.getDataSourceProperties() != null) {
            dataSourceProperties = dataSourceGroup.getDataSourceProperties();
        }
        if (dataSourceProperties == null)
            throw new NullPointerException("数据源未配置");
        return dataSourceProperties;
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager(DataSourceGroup dataSourceGroup) {
        return new DataSourceTransactionManager(dataSource(dataSourceGroup));
    }

    /**
     * 性能分析插件
     */
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false)
    private PerformanceInterceptor performanceInterceptor;

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, PaginationInterceptor paginationInterceptor) throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConfig.MAPPER_LOCATION));
        sessionFactory.setGlobalConfig(new GlobalConfig().setDbConfig(new GlobalConfig.DbConfig().setIdType(IdType.UUID)));
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setCacheEnabled(true);
        sessionFactory.setConfiguration(configuration);
        Set<Interceptor> interceptors = new HashSet<>();
        interceptors.add(paginationInterceptor);
        if (performanceInterceptor != null) {
            interceptors.add(performanceInterceptor);
        }
        sessionFactory.setPlugins(interceptors.toArray(new Interceptor[]{}));
        return sessionFactory.getObject();
    }
}