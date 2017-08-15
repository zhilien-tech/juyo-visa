/**
 * JapanData.java
 * io.znz.jsite.visa.simulator.data
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.data.jp;

import lombok.Data;

/**
 * 封装日本需要的签证数据
 * <p>
 *
 * @author   朱晓川
 * @Date	 2017年8月7日 	 
 */
@Data
public class JapanData {

	private String visaAccount = "1507-001";

	private String visaPasswd = "kintsu2017";

	/**指定番号,代理签证的公司编号*/
	private String agentNo = "GTP-BJ-084-0";

	/**
	 * 签证类型，个人1/数次N
	 * @see JapanVisaType1Enum
	 */
	private String visaType1;

	/**
	 * 签证类型2，数次签证的时候需要处理这个值，值为3或4
	 * @see JapanVisaType2Enum
	 */
	private String visaType2;

	/**
	 * 申请人代表姓名(简体)
	 */
	private String proposerNameCN;

	/**
	 * 申请人代表姓名(英文大写)
	 */
	private String proposerNameEN;

	/**
	 * 除主申请人之外的人数
	 */
	private String applicantCnt;

	/**
	 * 入境日期,格式:yyyy/MM/dd
	 */
	private String entryDate;

	/**
	 * 出境日期,格式:yyyy/MM/dd
	 */
	private String leaveDate;

	/**
	 * 申请人信息excel文件路径
	 */
	private String excelUrl;

	/*东北六县是否选择*/
	private boolean VISA_STAY_PREF_47;//沖縄県
	private boolean VISA_STAY_PREF_2; //青森県
	private boolean VISA_STAY_PREF_3; //岩手県
	private boolean VISA_STAY_PREF_4; //宮城県
	private boolean VISA_STAY_PREF_5; //秋田県
	private boolean VISA_STAY_PREF_6; //山形県
	private boolean VISA_STAY_PREF_7; //福島県

	private boolean VISA_VISIT_TYPE; //是否选择东北三县

	private boolean VISA_VISIT_PREF_3; //岩手県
	private boolean VISA_VISIT_PREF_4; //宮城県
	private boolean VISA_VISIT_PREF_7; //福島県

}
