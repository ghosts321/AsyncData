package com.example.demo.domain;


import javax.naming.ldap.LdapContext;
import javax.sql.DataSource;

/**
 * @ClassName DataBaseConnection
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-08-01 17:56
 * @Version 1.0
 */
public class DataBaseConnection {

	private DataSource mainDataSource;

	private LdapContext ldapContext;

	public DataSource getMainDataSource() {
		return mainDataSource;
	}

	public void setMainDataSource(DataSource mainDataSource) {
		this.mainDataSource = mainDataSource;
	}

	public LdapContext getLdapContext() {
		return ldapContext;
	}

	public void setLdapContext(LdapContext ldapContext) {
		this.ldapContext = ldapContext;
	}
}
