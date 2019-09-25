package com.example.demo.domain;

/**
 * @ClassName DtaBaseDomain
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-07-26 18:53
 * @Version 1.0
 */
public class DataBaseDomain {

	private String mainDataBaseName;

	private String mainUrl;

	private String mainUserName;

	private String mainPassWord;

	private String syncDataBaseName;

	private String syncUrl;

	private String syncUserName;

	private String syncPassWord;

	public String getSyncDataBaseName() {
		return syncDataBaseName;
	}

	public void setSyncDataBaseName(String syncDataBaseName) {
		this.syncDataBaseName = syncDataBaseName;
	}

	public String getSyncUrl() {
		return syncUrl;
	}

	public void setSyncUrl(String syncUrl) {
		this.syncUrl = syncUrl;
	}

	public String getSyncUserName() {
		return syncUserName;
	}

	public void setSyncUserName(String syncUserName) {
		this.syncUserName = syncUserName;
	}

	public String getSyncPassWord() {
		return syncPassWord;
	}

	public void setSyncPassWord(String syncPassWord) {
		this.syncPassWord = syncPassWord;
	}

	public String getMainDataBaseName() {
		return mainDataBaseName;
	}

	public void setMainDataBaseName(String mainDataBaseName) {
		this.mainDataBaseName = mainDataBaseName;
	}

	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	public String getMainUserName() {
		return mainUserName;
	}

	public void setMainUserName(String mainUserName) {
		this.mainUserName = mainUserName;
	}

	public String getMainPassWord() {
		return mainPassWord;
	}

	public void setMainPassWord(String mainPassWord) {
		this.mainPassWord = mainPassWord;
	}
}
