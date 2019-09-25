package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @ Description   :  前后端交互响应Map
 * @ Author        :  JiangJunpeng
 * @ CreateDate    :  2019/9/7 11:39
 * @ UpdateUser    :  JiangJunpeng
 * @ UpdateDate    :  2019/9/7 11:39
 * @ UpdateRemark  :  修改内容
 * @ Version       :  1.0
 */
public class ResponseMap {
	
	// 状态码
	private String state;
	
	// 接口调用状态描述
	private String msg;
	
	// 业务数据
	private Object data;
	
	// 接口调用唯一标识
	private String us;
	

	/**
	 * 构造方法
	 */
	public ResponseMap() {
		
	}
	
	/**
	 * 构造方法，生成Response对象
	 * @param state
	 * @param msg
	 * @param data
	 */
	public ResponseMap(String state, String msg, Object data) {
		super();
		this.state = state;
		this.msg = msg;
		this.data = data;
	}
	
	/*
	 * 将数据封装成map对象
	 */
	public Map<String,Object> getResponseMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("state", this.getState());
		map.put("msg", this.getMsg());
		map.put("data", this.getData());
		return map;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the us
	 */
	public String getUs() {
		return us;
	}

	/**
	 * @param us the us to set
	 */
	public void setUs(String us) {
		this.us = us;
	}

}
