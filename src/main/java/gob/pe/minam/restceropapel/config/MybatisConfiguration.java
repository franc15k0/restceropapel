package gob.pe.minam.restceropapel.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(basePackages={"gob.pe.minam.restceropapel.**.repository"}, annotationClass= Repository.class, sqlSessionFactoryRef="sqlSessionFactory")
@EnableTransactionManagement
public class MybatisConfiguration {

    public final String TYPE_ALIASES_PACKAGE = "gob.pe.minam.restceropapel";
    public final String TYPE_HANDLERS_PACKAGE = "gob.pe.minam.restceropapel.handler";
    public final String MAPPER_LOCATIONS_PATH = "classpath:sqlmap/mapper/*.xml";


    @Value("${spring.datasource.primary.jndi-name}")
    private String primaryJndiName;

    @Bean(name = "dataSource", destroyMethod = "")
    public DataSource oracleJndiDataSource() throws IllegalArgumentException, NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(primaryJndiName);
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        return (DataSource)bean.getObject();
    }
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(sessionFactoryBean, dataSource);
        return sessionFactoryBean.getObject();
    }
    public void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        sessionFactoryBean.setTypeHandlersPackage(TYPE_HANDLERS_PACKAGE);
        sessionFactoryBean.setConfigLocation(new ClassPathResource("sqlmap/mybatis-config.xml"));
        sessionFactoryBean.setMapperLocations(pathResolver.getResources(MAPPER_LOCATIONS_PATH));
    }
}
