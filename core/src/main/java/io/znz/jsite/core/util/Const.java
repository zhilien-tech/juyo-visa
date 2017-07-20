package io.znz.jsite.core.util;

/**
 * Created by Chaly on 15/10/30.
 */
public class Const {

	/** JSITE.xml文件路径 */
	public static final String JSITE_XML_PATH = "/jsite.xml";

	public static final String CURRENT_USER = "user";

	/**
	 * 插件路径
	 */
	public static final String PLUGIN_PATH = "/WEB-INF/plugin/";
	/**
	 * 数据库备份路径
	 */
	public static final String BACKUP_PATH = "/WEB-INF/backup/";

	/**
	 * 上传路径
	 */
	public static final String IMAGE_PATH = "/upload/images/";

	public static final String SUFFIX_FTL = ".ftl";

	public static final String TPL_PATH = "WEB-INF/views";
	public static final String RES_PATH = "/res";
	public static final String UPLOAD_PATH = "/upload/";

	/**
	 * 异常信息统一头信息<br>
	 * 非常遗憾的通知您,程序发生了异常
	 */
	public static final String Exception_Head = "OH,MY GOD! SOME ERRORS OCCURED! AS FOLLOWS :";

	/***
	 * 存在的session的名字
	 * 
	 */

	//平台管理员
	public static final String PLAT_ADMIN = "admin";
	//当前登录用户信息
	public static final String SESSION_NAME = "newusersession";
	//当前登录公司信息
	public static final String USER_COMPANY_KEY = "newcompanysession";
	//当前登录用户的职位功能
	public static final String AUTHS_KEY = "authskeyfunction";
	//无效数据id
	public static final int INVALID_DATA_ID = -1;

}
