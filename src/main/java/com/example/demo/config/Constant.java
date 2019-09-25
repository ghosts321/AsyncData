package com.example.demo.config;

/**
 * 项目的一些系统配置全局常量
 * @author hjw
 *
 */
public class Constant {
	
	/**
	 * 系统响应前端标识
	 */
	public static final String SUCCESS_STATE = "200";			/** 成功标识 */
	public static final String PICSIZE_ERROR = "304";			/** 图片尺寸过大 */
	public static final String PARAMETE_ERROR = "400";			/** 参数错误（缺参或参数格式或校验不正确） */
	public static final String NO_PERMISSION = "401";			/** 没有权限（有token） */
	public static final String NO_TOKEN = "403";				/** 没有token（或token过期） */
	public static final String RESOURCE_NOTEXIST = "404";		/** 资源（文件、数据）不存在 */
	public static final String INTERFACE_DISABLE = "405";		/** 接口禁用 */
	public static final String MD5_ERREOR = "406";				/** 参数md5校验错误（完整性被破坏） */
	public static final String INTERFACE_OVERDUE = "410";		/** 接口过期 */
	public static final String DATA_LARGE = "413";				/** 请求数据包过大（或文件过大） */
	public static final String UNKNOWN_ERROR = "500";			/** 程序未知错误 */
	public static final String SERVICE_STOP = "503";			/** 接口访问过繁，服务暂时停止 */
	
	
}
