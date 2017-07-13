import time
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.ui import WebDriverWait # available since 2.4.0
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC # available since 2.26.0
from selenium.webdriver.support.select import Select
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
import sys
import logging
import json
from pprint import pprint

#函数定义  begin 
#等待id为elm_id的元素可见
def _wait_for_elm_by_id(elm_id,timeout = 3600):
    _check_alert_to_close()
    logging.info("Waiting element shown, id: " + elm_id)
    element = WebDriverWait(driver, timeout).until(
        EC.visibility_of_element_located((By.ID, elm_id)                )
    )

#点击确认关闭alert弹出框
def _check_alert_to_close():
    try:
        alert = driver.switch_to_alert()
        alert.accept()
        logging.info("Alert accepted")
    except:
        pass

#填写id为elmId的输入框，如果输入框已经有值则忽略
def _textInput(elmId,val,timeout=3600,duplicate_substr=None):
	
	#从json数据读取用来填充的值
    val = ''
    if duplicate_substr != None:
        val = data_json[elmId + duplicate_substr]
    else:
        val = data_json[elmId]
        
    _check_alert_to_close()
    
    #等待该id的元素就绪(页面存在)
    logging.info("Waiting Input id: " + elmId)
    elm_ipt_x = WebDriverWait(driver, timeout).until(
        EC.presence_of_element_located((By.ID, elmId))
    )
    
    #获取input框原来的value值
    elm_ipt_val = elm_ipt_x.get_attribute("value");
    if elm_ipt_val == "":
        elm_ipt_x.send_keys(val)
    else:
        logging.info(elmId + " value is not empty,ignore sendkeys")
    logging.info("\"" + elmId + "\"" + ":" + "\"" + val + "\",")

#点击id为elmId的按钮
def _button(elmId,timeout=3600):
    _check_alert_to_close()
    logging.info("Waiting Button id: " + elmId)
    elm_btn_x = WebDriverWait(driver, timeout).until(
        EC.presence_of_element_located((By.ID, elmId))
    )
    elm_btn_x.click()
    logging.info("Click Button id: " + elmId)

#勾选checkbox
def _checkbox(elmId,val,timeout=3600):
    val = data_json[elmId]
    _check_alert_to_close()
    logging.info("Waiting Checkbox id: " + elmId)
    elm_ipt_x = WebDriverWait(driver, timeout).until(
        EC.presence_of_element_located((By.ID, elmId))
    )
    
    #如果没有选中，而且欲填值为true，则(click)勾选上
    if not elm_ipt_x.is_selected():
        if val:
            elm_ipt_x.click()
    if val:
        logging.info("\"" + elmId + "\"" + ":" + "True,")
    else:
        logging.info("\"" + elmId + "\"" + ":" + "False,")

#勾选单选组
def _radiobox(elmId1,elmId2,isSelectId1,timeout=3600):
	#是否选中第一个
    isSelectId1 = data_json[elmId1]
    _check_alert_to_close()
    logging.info("Waiting Radiobox id: " + elmId1 + ",timeout:" + str(timeout))
    if isSelectId1:
        elm_ipt_x = WebDriverWait(driver, timeout).until(
            EC.presence_of_element_located((By.ID, elmId1))
        )
    else:
        elm_ipt_x = WebDriverWait(driver, timeout).until(
            EC.presence_of_element_located((By.ID, elmId2))
        )
    elm_ipt_x.click()
    if isSelectId1:
        logging.info("\"" + elmId1 + "\"" + ":" + "True,")
    else:
        logging.info("\"" + elmId1 + "\"" + ":" + "False,")

#下拉列表选中
def _select(elmId,val,timeout=3600,duplicate_substr=None):
    _check_alert_to_close()
    val = ""
    if duplicate_substr != None:
        val = data_json[elmId + duplicate_substr]
    else:
        val = data_json[elmId]
        
    #等待下拉列表可被点击
    logging.info("Waiting Select id: " + elmId + ",value:" + val)
    elm_sel_x = Select(WebDriverWait(driver, timeout).until(
        EC.element_to_be_clickable((By.ID, elmId))
    )
    )
    
    #等待下拉列表的option选项就绪(xpath查找)
    opt_xpath = '//*[@id="' + elmId + '"]/option[@value="' + val + '"]'
    logging.info("Waiting Value: " + opt_xpath + " under Select id:" + elmId)
    elm_opt_x = WebDriverWait(driver, timeout).until(
        EC.presence_of_element_located((By.XPATH, opt_xpath))
    )
	
	#选中
    elm_sel_x.select_by_value(val)
    logging.info("\"" + elmId + "\"" + ":" + "\"" + val + "\",")

#函数定义  end
#主流程开始
# Main Process Start
if not sys.platform.startswith('win'):
    import coloredlogs
    coloredlogs.install(level='INFO')
logging.basicConfig(format='%(levelname)s:%(message)s', level=logging.INFO)

#import JSON data file,从文件加载json数据，命令行的第二个参数为完整文件名
data_json = None
file_name = sys.argv[1]
logging.info("Import json data from file : %s",file_name)
with open(file_name) as data_file:
    data_json = json.load(data_file)
#pprint(data_json)

# 打开火狐浏览器并且跳转到美国签证网站
driver = webdriver.Firefox("./")
#driver.implicitly_wait(3600)
driver._is_remote = False
driver.get("https://ceac.state.gov/GenNIV/Default.aspx")

# Page 1: input region select and wait human to input the code
# 第一页,默认选择地区为CHINA BEIJING,并且等待人工填写验证码
_select("ctl00_SiteContentPlaceHolder_ucLocation_ddlLocation","BEJ")
logging.info("Load Page Title :%s",driver.title)
logging.info("***SCRIPT***WAIT HUMAN INPUT VCODE***")
try:
    element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_ddlQuestions")
finally:
    logging.warn("Finally")
    #driver.quit()

# Page 2: input the secrect question
# 第二页，输入安全提示问题,填写'Mother Name' |-_-|
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_btnContinue")
logging.info("Load Page Title :%s",driver.title)
answer = "Mother Name"
_textInput("ctl00_SiteContentPlaceHolder_txtAnswer",answer)

#点击继续
_button("ctl00_SiteContentPlaceHolder_btnContinue")

# Page 3: input the personal info 1
# 第三页    输入个人信息1
# Make sure page has been complete loaded
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
#姓SURNAME
answer = "Alan"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_SURNAME",answer)
#名GIVEN_NAME
answer = "Liu"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_GIVEN_NAME",answer)

#Full Name in Native Alphabet用母语填写你的全姓名称
answer = "Alan Liu"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_FULL_NAME_NATIVE",answer)

#用母语填写全称姓名的时候，名字是生僻字电脑打不出的时候才选Does Not Apply/Technology Not Available
answer = True
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_FULL_NAME_NATIVE_NA",answer)

#是否使用曾用名
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblOtherNames_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblOtherNames_0","ctl00_SiteContentPlaceHolder_FormView1_rblOtherNames_1",answer)
#如果选择了使用曾用名，则需要填写曾用名
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_DListAlias_ctl00_tbxSURNAME")
    time.sleep(3)
    #从json读取曾用名(多个)，每有一个则需要点击add another 添加
    #每增加一个，id号的变化从100开始 加1，这里通过字符串拼接的方式实现，其实可以转为数字做数学运算再变为字符串
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_DListAlias_ctl_InsertButtonAlias"]
    for idx,val in enumerate(data):
        elm_idx_str = 0
        elm_addlink_idx_str = -1
        elm_id = ""
        if idx < 10:
            elm_idx_str = "0" + str(idx)
        else:
            elm_idx_str = str(idx)
        if idx-1 < 10:
            elm_addlink_idx_str = "0" + str(idx-1)
        else:
            elm_addlink_idx_str = str(idx-1)
        if idx != 0:
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_DListAlias_ctl" + elm_addlink_idx_str + "_InsertButtonAlias"
            _button(elm_id)
            
        #填写当前曾用姓    
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_DListAlias_ctl" + elm_idx_str + "_tbxSURNAME"
        _textInput(elm_id,val)
        #填写当前曾用名    
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_DListAlias_ctl" + elm_idx_str + "_tbxGIVEN_NAME"
        _textInput(elm_id,val)
        
#是否使用姓名电码，如果使用的话则需要填写
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblTelecodeQuestion_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblTelecodeQuestion_0","ctl00_SiteContentPlaceHolder_FormView1_rblTelecodeQuestion_1",answer)
if answer:
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_TelecodeSURNAME",answer)
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_TelecodeGIVEN_NAME",answer)
    
#选择性别
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblAPP_GENDER_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblAPP_GENDER_0","ctl00_SiteContentPlaceHolder_FormView1_rblAPP_GENDER_1",answer)

#选择婚姻状况  Marital Status
maritalStatus = data_json["ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_MARITAL_STATUS"] # M,S,D,W
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_MARITAL_STATUS",maritalStatus)

#Select
answer = "28"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlDOBDay",answer,duplicate_substr="_1")
#Select
answer = "JAN"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlDOBMonth",answer,duplicate_substr="_1")
#Text Input
answer = "1983"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxDOBYear",answer,duplicate_substr="_1")
#Text Input
answer = "BEIJING"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_POB_CITY",answer)
#Text Input
answer = "BEIJING"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_POB_ST_PROVINCE",answer)
#Checkbox
answer = True
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_POB_ST_PROVINCE_NA",answer)
#Select
answer = "CHIN"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_POB_CNTRY",answer)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 3: input the personal info 2
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
#Select
answer = "CHIN"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_NATL",answer)
#Radiobox
answer = False
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblAPP_OTH_NATL_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblAPP_OTH_NATL_IND_1",answer)
#Radiobox
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblPermResOtherCntryInd_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPermResOtherCntryInd_0","ctl00_SiteContentPlaceHolder_FormView1_rblPermResOtherCntryInd_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlOthPermResCntry_ctl00_ddlOthPermResCntry")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlOthPermResCntry_ctl_InsertButtonOTHER_PERM_RES"]

    for idx,val in enumerate(data):
        elm_idx_str = 0
        elm_addlink_idx_str = -1
        elm_id = ""
        if idx < 10:
            elm_idx_str = "0" + str(idx)
        else:
            elm_idx_str = str(idx)
        if idx-1 < 10:
            elm_addlink_idx_str = "0" + str(idx-1)
        else:
            elm_addlink_idx_str = str(idx-1)
        if idx != 0:
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlOthPermResCntry_ctl" + elm_addlink_idx_str + "_InsertButtonOTHER_PERM_RES"
            _button(elm_id)
            logging.info("Click btn:" + str(elm_btn_x) + ",id: " + elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlOthPermResCntry_ctl" + elm_idx_str + "_ddlOthPermResCntry"
        _select(elm_id,val)
#Text Input
answer = "1101009198710189765"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_NATIONAL_ID",answer)
#Checkbox
answer = True
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_SSN_NA",answer)
#Checkbox
answer = True
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_TAX_ID_NA",answer)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 4: input the Address and Phone info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
#Text Input
answer = "Street address 1"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_LN1",answer)
#Text Input
answer = "Street address 2"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_LN2",answer)
#Text Input
answer = "BeiJing"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_CITY",answer)
#Text Input
answer = "Beijing"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_STATE",answer)
#Text Input
answer = "100083"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_POSTAL_CD",answer)
#Select
answer = "CHIN"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlCountry",answer)
#Radiobox
answer = True
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblMailingAddrSame_0","ctl00_SiteContentPlaceHolder_FormView1_rblMailingAddrSame_1",answer)
#Text Input
answer = "18687184553"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_HOME_TEL",answer)
#Text Input
answer = "82459957"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_MOBILE_TEL",answer)
#Checkbox
answer = True
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_BUS_TEL_NA",answer)
#Text Input
answer = "82459957@qq.com"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_EMAIL_ADDR",answer)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 5: input the Passport info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
#Select
answer = "R"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_TYPE",answer)
time.sleep(3)
#Text Input
answer = "G21323345"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_NUM",answer)
#Checkbox
answer = True
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxPPT_BOOK_NUM_NA",answer)
time.sleep(3)
#Select
answer = "CHIN"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_ISSUED_CNTRY",answer)
#Text Input
answer = "KUNMING"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_ISSUED_IN_CITY",answer)
#Text Input
answer = "YUNNAN"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_ISSUED_IN_STATE",answer)
#Select
answer = "CHIN"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_ISSUED_IN_CNTRY",answer)
#Select
answer = "28"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_ISSUED_DTEDay",answer)
#Select
answer = "01"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_ISSUED_DTEMonth",answer)
#Text Input
answer = "2005"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_ISSUEDYear",answer)
#Select
answer = "27"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_EXPIRE_DTEDay",answer)
#Select
answer = "01"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_EXPIRE_DTEMonth",answer)
#Text Input
answer = "2025"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_EXPIREYear",answer)
#Radiobox
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblLOST_PPT_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblLOST_PPT_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblLOST_PPT_IND_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl00_tbxLOST_PPT_NUM")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl_InsertButtonLostPPT"]
    for idx,val in enumerate(data):
        elm_idx_str = 0
        elm_addlink_idx_str = -1
        elm_id = ""
        if idx < 10:
            elm_idx_str = "0" + str(idx)
        else:
            elm_idx_str = str(idx)
        if idx-1 < 10:
            elm_addlink_idx_str = "0" + str(idx-1)
        else:
            elm_addlink_idx_str = str(idx-1)
        if idx != 0:
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl" + elm_addlink_idx_str + "_InsertButtonLostPPT"
            _button(elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl" + elm_idx_str + "_tbxLOST_PPT_NUM"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl" + elm_idx_str + "_ddlLOST_PPT_NATL"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl" + elm_idx_str + "_tbxLOST_PPT_EXPL"
        _textInput(elm_id,val)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 6: input the Travel info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
#Select
answer = "B"
_select("ctl00_SiteContentPlaceHolder_FormView1_dlPrincipalAppTravel_ctl00_ddlPurposeOfTrip",answer)
#Select
answer = "B1-B2"
_select("ctl00_SiteContentPlaceHolder_FormView1_dlPrincipalAppTravel_ctl00_ddlOtherPurpose",answer)
#Radiobox    now   this radio does not exist  20170713
#answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblSpecificTravel_0"]
#_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblSpecificTravel_0","ctl00_SiteContentPlaceHolder_FormView1_rblSpecificTravel_1",answer)
#elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlTravelLoc_ctl00_tbxSPECTRAVEL_LOCATION")
time.sleep(3)

#Select
answer = "28"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlTRAVEL_DTEDay",answer)
#Select
answer = "4"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlTRAVEL_DTEMonth",answer)
#Text Input
answer = "2017"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxTRAVEL_DTEYear",answer)

# Intended Length of Stay in U.S.
#Input stay count
answer = "2"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxTRAVEL_LOS",answer)

# choose stay period
answer="W"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlTRAVEL_LOS_CD",answer)

#---------------------------------------------------------------------------------------------
#stay more than 24h?
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_ddlTRAVEL_LOS_NOT24H"]
if answer:
	# Street Address (Line 1) Error
	answer = "USA Address Stay"
	_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxStreetAddress1",answer)
	#City
	answer = "Palo Alto"
	_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxCity",answer)
	#State 
	answer = "CA"
	_select("ctl00_SiteContentPlaceHolder_FormView1_ddlTravelState",answer)
	#ZIP Code (if known)
	answer = "10020"
	_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbZIPCode",answer)
#---------------------------------------------------------------------------------------------

#Person/Entity Paying for Your Trip
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_ddlWhoIsPaying"]
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlWhoIsPaying",answer)
if answer == "O":
    answer = "PayerSurName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerSurname",answer)
    answer = "PayerGivName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerGivenName",answer)
    answer = "18687184444"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerPhone",answer)
    answer = "18687184444@163.com"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPAYER_EMAIL_ADDR",answer)
    answer = "O"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlPayerRelationship",answer)
    answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblPayerAddrSameAsInd_0"]
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPayerAddrSameAsInd_0","ctl00_SiteContentPlaceHolder_FormView1_rblPayerAddrSameAsInd_1",answer)
elif answer == "C":
    answer = "CompanyName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayingCompany",answer)
    answer = "01056647568"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerPhone",answer)
    answer = "Employee"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxCompanyRelation",answer)
    answer = "Company Address 1"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerStreetAddress1",answer)
    answer = "Company Address 2"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerStreetAddress2",answer)
    answer = "BeiJing"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerCity",answer)
    answer = "BeiJing"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerStateProvince",answer)
    answer = "100010"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPayerPostalZIPCode",answer)
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlPayerCountry",answer)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 7: input the Travel Companions info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
#Radiobox
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblOtherPersonsTravelingWithYou_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblOtherPersonsTravelingWithYou_0","ctl00_SiteContentPlaceHolder_FormView1_rblOtherPersonsTravelingWithYou_1",answer)
if answer:
    element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_rblGroupTravel_0")
    #Radiobox
    answer2 = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblGroupTravel_0"]
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblGroupTravel_0","ctl00_SiteContentPlaceHolder_FormView1_rblGroupTravel_1",answer2)
    if answer2:
        #Text Input
        answer3 = "Travel Group Name"
        _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxGroupName",answer3)
    else:
        elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl00_tbxSurname")
        time.sleep(3)
        data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl_InsertButtonPrincipalPOT"]
        for idx,val in enumerate(data):
            elm_idx_str = 0
            elm_addlink_idx_str = -1
            elm_id = ""
            if idx < 10:
                elm_idx_str = "0" + str(idx)
            else:
                elm_idx_str = str(idx)
            if idx-1 < 10:
                elm_addlink_idx_str = "0" + str(idx-1)
            else:
                elm_addlink_idx_str = str(idx-1)
            if idx != 0:
                elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl" + elm_addlink_idx_str + "_InsertButtonPrincipalPOT"
                _button(elm_id)
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl" + elm_idx_str + "_tbxSurname"
            _textInput(elm_id,val)
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl" + elm_idx_str +"_tbxGivenName"
            _textInput(elm_id,val)
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl" + elm_idx_str + "_ddlTCRelationship"
            _select(elm_id,val)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 8: input the Previous U.S. Travel info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
#Radiobox
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblPREV_US_TRAVEL_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_US_TRAVEL_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_US_TRAVEL_IND_1",answer)
if answer:
	# wait for the visit table element show up
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl_InsertButtonPREV_US_VISIT"]
    for idx,val in enumerate(data):
        elm_idx_str = 0
        elm_addlink_idx_str = -1
        elm_id = ""
        
        if idx < 10:
            elm_idx_str = "0" + str(idx)
        else:
            elm_idx_str = str(idx)
        if idx-1 < 10:
            elm_addlink_idx_str = "0" + str(idx-1)
        else:
            elm_addlink_idx_str = str(idx-1)
        if idx != 0:
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + elm_addlink_idx_str + "_InsertButtonPREV_US_VISIT"
            _button(elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + elm_idx_str + "_ddlPREV_US_VISIT_DTEDay"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + elm_idx_str + "_ddlPREV_US_VISIT_DTEMonth"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + elm_idx_str + "_tbxPREV_US_VISIT_DTEYear"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + elm_idx_str + "_tbxPREV_US_VISIT_LOS"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + elm_idx_str + "_ddlPREV_US_VISIT_LOS_CD"
        _select(elm_id,val)
    #Radiobox
    answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblPREV_US_DRIVER_LIC_IND_0"]
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_US_DRIVER_LIC_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_US_DRIVER_LIC_IND_1",answer)
    if answer:
        answer2 = "100210120120"
        _textInput("ctl00_SiteContentPlaceHolder_FormView1_dtlUS_DRIVER_LICENSE_ctl00_tbxUS_DRIVER_LICENSE",answer2)
        answer2 = "CA"
        _select("ctl00_SiteContentPlaceHolder_FormView1_dtlUS_DRIVER_LICENSE_ctl00_ddlUS_DRIVER_LICENSE_STATE",answer2)
    else:
        time.sleep(3)
#Radiobox
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_IND_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_CANCELLED_IND_0")
    time.sleep(3)
    #Select
    answer2 = "28"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlPREV_VISA_ISSUED_DTEDay",answer2)
    #Select
    answer2 = "1"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlPREV_VISA_ISSUED_DTEMonth",answer2)
    #Text Input
    answer2 = "2005"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPREV_VISA_ISSUED_DTEYear",answer2)
    #Text Input
    answer2 = "12345678"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxPREV_VISA_FOIL_NUMBER",answer2)
    #Radiobox
    answer2 = True
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_SAME_TYPE_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_SAME_TYPE_IND_1",answer2)
    #Radiobox
    answer2 = True
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_SAME_CNTRY_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_SAME_CNTRY_IND_1",answer2)
    #Radiobox
    answer2 = True
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_TEN_PRINT_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_TEN_PRINT_IND_1",answer2)
    #Radiobox
    answer2 = False
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_LOST_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_LOST_IND_1",answer2)
    #Radiobox
    answer2 = False
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_CANCELLED_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_CANCELLED_IND_1",answer2)
else:
    time.sleep(3)
#Radiobox
answer = False
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_REFUSED_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_REFUSED_IND_1",answer)
#Radiobox
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblIV_PETITION_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblIV_PETITION_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblIV_PETITION_IND_1",answer)
if answer:
    #Text Input
    answer = "Exp......"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxIV_PETITION_EXPL",answer)
else:
    time.sleep(3)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 9: input the US Contact info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
##Checkbox
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxUS_POC_ORG_NA_IND",True)
time.sleep(3)
#Text Input
answer = "ContactSurname"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_SURNAME",answer)
#Text Input
answer = "ContactGivname"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_GIVEN_NAME",answer)
#Select
answer = "R"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlUS_POC_REL_TO_APP",answer)
#Text Input
answer = "Contact Address Line"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_LN1",answer)
#Text Input
answer = "Palo Alto"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_CITY",answer)
#Select
answer = "CA"
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlUS_POC_ADDR_STATE",answer)
#Text Input
answer = "10010"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_POSTAL_CD",answer)
#Text Input
answer = "18687184667"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_HOME_TEL",answer)
#Text Input
answer = "18687184667@163.com"
_textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_EMAIL_ADDR",answer)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 10: input the Family-Relatives info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_SURNAME_UNK_IND"]
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_SURNAME_UNK_IND",answer)
if not answer:
    answer = "FatherSurName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxFATHER_SURNAME",answer)
else:
    time.sleep(3)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_GIVEN_NAME_UNK_IND"]
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_GIVEN_NAME_UNK_IND",answer)
if not answer:
    answer = "FatherGivName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxFATHER_GIVEN_NAME",answer)
else:
    time.sleep(3)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_SURNAME_UNK_IND"] and data_json["ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_GIVEN_NAME_UNK_IND"]
if answer:
    time.sleep(3)
else:
    answer = "27"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlFathersDOBDay",answer)
    answer = "APR"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlFathersDOBMonth",answer)
    answer = "1957"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxFathersDOBYear",answer)
    answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblFATHER_LIVE_IN_US_IND_0"]
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblFATHER_LIVE_IN_US_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblFATHER_LIVE_IN_US_IND_1",answer)
    if answer:
        answer = "S"
        _select("ctl00_SiteContentPlaceHolder_FormView1_ddlFATHER_US_STATUS",answer)
    else:
        time.sleep(3)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_SURNAME_UNK_IND"]
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_SURNAME_UNK_IND",answer)
if not answer:
    answer = "MotherSurName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxMOTHER_SURNAME",answer)
else:
    time.sleep(3)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_GIVEN_NAME_UNK_IND"]
_checkbox("ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_GIVEN_NAME_UNK_IND",answer)
if not answer:
    answer = "MotherGivName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxMOTHER_GIVEN_NAME",answer)
else:
    time.sleep(3)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_SURNAME_UNK_IND"] and data_json["ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_GIVEN_NAME_UNK_IND"]
if answer:
    time.sleep(3)
else:
    answer = "27"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlMothersDOBDay",answer)
    answer = "APR"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlMothersDOBMonth",answer)
    answer = "1957"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxMothersDOBYear",answer)
    answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblMOTHER_LIVE_IN_US_IND_0"]
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblMOTHER_LIVE_IN_US_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblMOTHER_LIVE_IN_US_IND_1",answer)
    if answer:
        answer = "C"
        _select("ctl00_SiteContentPlaceHolder_FormView1_ddlMOTHER_US_STATUS",answer)
    else:
        time.sleep(3)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblUS_IMMED_RELATIVE_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblUS_IMMED_RELATIVE_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblUS_IMMED_RELATIVE_IND_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl00_ddlUS_REL_STATUS")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl_InsertButtonUSRelative"]
    for idx,val in enumerate(data):
        elm_idx_str = 0
        elm_addlink_idx_str = -1
        elm_id = ""
        if idx < 10:
            elm_idx_str = "0" + str(idx)
        else:
            elm_idx_str = str(idx)
        if idx-1 < 10:
            elm_addlink_idx_str = "0" + str(idx-1)
        else:
            elm_addlink_idx_str = str(idx-1)
        if idx != 0:
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + elm_addlink_idx_str + "_InsertButtonUSRelative"
            _button(elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + elm_idx_str + "_tbxUS_REL_SURNAME"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + elm_idx_str + "_tbxUS_REL_GIVEN_NAME"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + elm_idx_str + "_ddlUS_REL_TYPE"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + elm_idx_str + "_ddlUS_REL_STATUS"
        _select(elm_id,val)
else:
    answer = True
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblUS_OTHER_RELATIVE_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblUS_OTHER_RELATIVE_IND_1",answer)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 11: input the Family-Relatives info
# This is a option page depends on marital status,For chinese , we just process 4 conditions:
# Married,Single,Divorced & Widowed
# Make sure page has been complete loaded
time.sleep(3)
logging.info("Load Page Title :%s",driver.title)
#maritalStatus = "M" # M,S,D,W
#define in personal info 1 page
if(maritalStatus == "M"):
    answer = "SpouseSurName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSpouseSurname",answer)
    answer = "SpouseGivName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSpouseGivenName",answer)
    answer = "28"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlDOBDay",answer,duplicate_substr="_2")
    answer = "FEB"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlDOBMonth",answer,duplicate_substr="_2")
    answer = "1983"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxDOBYear",answer,duplicate_substr="_2")
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseNatDropDownList",answer)
    answer = "BeiJing"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSpousePOBCity",answer)
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlSpousePOBCountry",answer)
    answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseAddressType"]
    if answer == "O":
        _select("ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseAddressType","O")
        answer = "Spouse Address Line"
        _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_LN1",answer)
        answer = "Spouse Address Line"
        _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_LN1",answer)
        answer = "Beijing"
        _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_CITY",answer)
        answer = "Beijing"
        _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_STATE",answer)
        answer = "100083"
        _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_POSTAL_CD",answer)
        answer = "CHIN"
        _select("ctl00_SiteContentPlaceHolder_FormView1_ddlSPOUSE_ADDR_CNTRY",answer)
    else:
        _select("ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseAddressType","H")
    #Button
    time.sleep(3)
    _button("ctl00_SiteContentPlaceHolder_UpdateButton3")
elif(maritalStatus == "D"):
    answer = "1"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxNumberOfPrevSpouses",answer)
    time.sleep(3)
    answer = "SpouseSurName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxSURNAME",answer)
    time.sleep(3)
    answer = "SpouseSurName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxSURNAME",answer)
    answer = "SpouseGivName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxGIVEN_NAME",answer)
    answer = "28"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDOBDay",answer)
    answer = "FEB"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDOBMonth",answer)
    answer = "1983"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxDOBYear",answer)
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlSpouseNatDropDownList",answer)
    answer = "BeiJing"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxSpousePOBCity",answer)
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlSpousePOBCountry",answer)
    answer = "28"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDomDay",answer)
    answer = "2"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDomMonth",answer)
    answer = "2010"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_txtDomYear",answer)
    answer = "28"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDomEndDay",answer)
    answer = "2"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDomEndMonth",answer)
    answer = "2012"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_txtDomEndYear",answer)
    answer = "EXP.....EXP...."
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxHowMarriageEnded",answer)
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlMarriageEnded_CNTRY",answer)
    #Button
    time.sleep(3)
    _button("ctl00_SiteContentPlaceHolder_UpdateButton3")
elif(maritalStatus == "W"):
    answer = "SpouseSurName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSURNAME",answer)
    answer = "SpouseGivName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxGIVEN_NAME",answer)
    answer = "28"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlDOBDay",answer,duplicate_substr="_3")
    answer = "FEB"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlDOBMonth",answer,duplicate_substr="_3")
    answer = "1983"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxDOBYear",answer,duplicate_substr="_3")
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseNatDropDownList",answer)
    answer = "BeiJing"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxSpousePOBCity",answer)
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlSpousePOBCountry",answer)
    #Button
    time.sleep(3)
    _button("ctl00_SiteContentPlaceHolder_UpdateButton3")
else:
    logging.info("Marital status is single or other status")

not_exist_timeout = 3600
# Page 15: input the Security&Background Part1 info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)

answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblDisease_0","ctl00_SiteContentPlaceHolder_FormView1_rblDisease_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
    
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblDisorder_0","ctl00_SiteContentPlaceHolder_FormView1_rblDisorder_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
    
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblDruguser_0","ctl00_SiteContentPlaceHolder_FormView1_rblDruguser_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 16: input the Security&Background Part2 info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)

answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblArrested_0","ctl00_SiteContentPlaceHolder_FormView1_rblArrested_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
    
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblControlledSubstances_0","ctl00_SiteContentPlaceHolder_FormView1_rblControlledSubstances_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
    
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblProstitution_0","ctl00_SiteContentPlaceHolder_FormView1_rblProstitution_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
    
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblMoneyLaundering_0","ctl00_SiteContentPlaceHolder_FormView1_rblMoneyLaundering_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
    
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblHumanTrafficking_0","ctl00_SiteContentPlaceHolder_FormView1_rblHumanTrafficking_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
    
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblAssistedSevereTrafficking_0","ctl00_SiteContentPlaceHolder_FormView1_rblAssistedSevereTrafficking_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
    
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblHumanTraffickingRelated_0","ctl00_SiteContentPlaceHolder_FormView1_rblHumanTraffickingRelated_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 17: input the Security&Background Part3 info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblIllegalActivity_0","ctl00_SiteContentPlaceHolder_FormView1_rblIllegalActivity_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblTerroristActivity_0","ctl00_SiteContentPlaceHolder_FormView1_rblTerroristActivity_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblTerroristSupport_0","ctl00_SiteContentPlaceHolder_FormView1_rblTerroristSupport_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblTerroristOrg_0","ctl00_SiteContentPlaceHolder_FormView1_rblTerroristOrg_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblGenocide_0","ctl00_SiteContentPlaceHolder_FormView1_rblGenocide_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblTorture_0","ctl00_SiteContentPlaceHolder_FormView1_rblTorture_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblExViolence_0","ctl00_SiteContentPlaceHolder_FormView1_rblExViolence_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblChildSoldier_0","ctl00_SiteContentPlaceHolder_FormView1_rblChildSoldier_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblReligiousFreedom_0","ctl00_SiteContentPlaceHolder_FormView1_rblReligiousFreedom_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPopulationControls_0","ctl00_SiteContentPlaceHolder_FormView1_rblPopulationControls_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblTransplant_0","ctl00_SiteContentPlaceHolder_FormView1_rblTransplant_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 18: input the Security&Background Part4 info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblRemovalHearing_0","ctl00_SiteContentPlaceHolder_FormView1_rblRemovalHearing_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblImmigrationFraud_0","ctl00_SiteContentPlaceHolder_FormView1_rblImmigrationFraud_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblFailToAttend_0","ctl00_SiteContentPlaceHolder_FormView1_rblFailToAttend_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblVisaViolation_0","ctl00_SiteContentPlaceHolder_FormView1_rblVisaViolation_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 19: input the Security&Background Part5 info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblChildCustody_0","ctl00_SiteContentPlaceHolder_FormView1_rblChildCustody_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblVotingViolation_0","ctl00_SiteContentPlaceHolder_FormView1_rblVotingViolation_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblRenounceExp_0","ctl00_SiteContentPlaceHolder_FormView1_rblRenounceExp_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
answer = False
try:
    _radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblAttWoReimb_0","ctl00_SiteContentPlaceHolder_FormView1_rblAttWoReimb_1",answer,not_exist_timeout)
except TimeoutException:
    logging.info("Timeout exception catched,element not exists.")
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 20: Upload photo pre page
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_btnUploadPhoto")
logging.info("Load Page Title :%s",driver.title)
_button("ctl00_SiteContentPlaceHolder_btnUploadPhoto")

# Page 21: Upload photo page
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_cphMain_imageFileUpload")
logging.info("Load Page Title :%s",driver.title)
_textInput("ctl00_cphMain_imageFileUpload","~/SSSS.jpg")
time.sleep(5)
_button("ctl00_cphButtons_btnUpload")

# Page 22: Upload photo - Continue using this Photo
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_cphButtons_btnContinue")
logging.info("Load Page Title :%s",driver.title)
time.sleep(5)
_button("ctl00_cphButtons_btnContinue")

# Page 23: Confirm Photo
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
time.sleep(5)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 24N: Go through the review pages
for i in range(1,8):
    time.sleep(3)
    element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
    logging.info("Load Page Title :%s",driver.title)
    time.sleep(5)
    _button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 25:Sign and submit
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_CodeTextBox")
logging.info("Load Page Title :%s",driver.title)
answer = False
_radiobox("ctl00_SiteContentPlaceHolder_FormView3_rblPREP_IND_0","ctl00_SiteContentPlaceHolder_FormView3_rblPREP_IND_1",answer)
answer = "G21323345"
_textInput("ctl00_SiteContentPlaceHolder_PPTNumTbx",answer)
logging.info("***SCRIPT***WAIT HUMAN SIGN AND SUMIT***")
# Wait for human to read and type in the code
#_button("ctl00_SiteContentPlaceHolder_btnSignApp")
elm_sel_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_SignedSuccessText")
time.sleep(5)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")
