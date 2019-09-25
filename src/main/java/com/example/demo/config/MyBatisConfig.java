package com.example.demo.config;

import com.example.demo.domain.DataBaseDomain;
import com.example.demo.domain.MyBatisConfigDomain;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @ClassName MyBatiesConfig
 * @Description TODO
 * @Author jiangjunpeng
 * @Date 2019-07-26 11:01
 * @Version 1.0
 */
@Configuration
public class MyBatisConfig {

	private static String MAPPERLOCATIONS = "classpath*:/com/example/demo/mapper/*.xml";

	public static DataSource buildDataSource(MyBatisConfigDomain dataBaseDomain) {
		return DataSourceBuilder.create()
				.driverClassName(dataBaseDomain.getDriverClassName())
				.url(dataBaseDomain.getUrl())
				.username(dataBaseDomain.getUserName())
				.password(dataBaseDomain.getPassWord())
				.build();
	}

	public static DataSourceTransactionManager buildTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	public static SqlSessionFactory buildSqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = pathMatchingResourcePatternResolver.getResources(MAPPERLOCATIONS);
		sqlSessionFactoryBean.setMapperLocations(resources);
		sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
		return sqlSessionFactoryBean.getObject();
	}

	public static SqlSession buildSqlSession(MyBatisConfigDomain dataBaseDomain) throws Exception {
		DataSource dataSource = buildDataSource(dataBaseDomain);
		SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory(dataSource);
		return sqlSessionFactory.openSession();
	}


	public static SqlSession buildSqlSession(DataSource dataSource) throws Exception {
		SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory(dataSource);
		return sqlSessionFactory.openSession();
	}

	public static LdapContext buildContextSource(DataBaseDomain dataBaseDomain) throws NamingException {
		String syncUrl = dataBaseDomain.getSyncUrl();
		String port = dataBaseDomain.getSyncDataBaseName();
		String syncUserName = dataBaseDomain.getSyncUserName();
		String syncPassWord = dataBaseDomain.getSyncPassWord();
		String url = "ldap://" + syncUrl + ":" + "389";
		Properties properties = new Properties();
		properties.setProperty(Context.SECURITY_AUTHENTICATION, "simple");
		properties.setProperty(Context.SECURITY_PRINCIPAL, syncUserName);
		properties.setProperty(Context.SECURITY_CREDENTIALS, syncPassWord);
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.setProperty(Context.PROVIDER_URL, url);
		return new InitialLdapContext(properties, null);
	}

}
