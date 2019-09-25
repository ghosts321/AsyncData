package com.example.demo.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * @author luoliang
 * @date 2018/6/20
 */
public class UploadFileInfo implements Serializable {

	private String id;

	private String filename;

	private String oldFileName;

	private Long totalSize;

	private String type;

	private String location;

	private String url;

	private String path;

	private String applyId;

	private String createUserId;

	private Date createDate = new Date();
	/*
	是否传输完成（上传0未完成，1完成）
	 */
	private String isCom = "0";
	/*
	上传完成日期
	 */
	private Date comDate;

	private String totalChunks;

	private String fileid;

	private String name;

	private String mimetype;

	private Long size;

	private String localpath;

	private String state;

	public void flush() {
		this.url = location;
		this.fileid = id;
		this.name = filename;
		this.size = totalSize;
		this.state = "200";
	}

	public Date getComDate() {
		return comDate;
	}

	public void setComDate(Date comDate) {
		this.comDate = comDate;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIsCom() {
		return isCom;
	}

	public void setIsCom(String isCom) {
		this.isCom = isCom;
	}

	public String getOldFileName() {
		return oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTotalChunks() {
		return totalChunks;
	}

	public void setTotalChunks(String totalChunks) {
		this.totalChunks = totalChunks;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getLocalpath() {
		return localpath;
	}

	public void setLocalpath(String localpath) {
		this.localpath = localpath;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
