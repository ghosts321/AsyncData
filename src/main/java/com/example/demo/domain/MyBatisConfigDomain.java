package com.example.demo.domain;

/**
 * @ClassName MyBatisConfigDomain
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-07-31 18:08
 * @Version 1.0
 */
public class MyBatisConfigDomain {

	private String driverClassName;

	private String url;

	private String userName;

	private String passWord;

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
}
