# -*- coding: UTF-8 -*-
import time
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait # available since 2.4.0
from selenium.webdriver.common.by import By
from selenium.webdriver.support.select import Select
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities

from selenium.webdriver.support import expected_conditions as EC # available since 2.26.0
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException

import sys
import logging
import json
from pprint import pprint
import os

import urllib
import urllib.request
import urllib.error
import requests

from ctypes import cdll

_sopen = cdll.msvcrt._sopen
_close = cdll.msvcrt._close
_SH_DENYRW = 0x10

#判断文件是否被打开,预留作为参考的方法
def is_open(filename):
    if not os.access(filename, os.F_OK):
        return False # file doesn't exist
    h = _sopen(filename, 0, _SH_DENYRW, 0)
    if h == 3:
        _close(h)
        return False # file is not opened by anyone else
    return True # file is already open

#函数定义  begin 
#等待id为elm_id的元素可见
def _wait_for_elm_by_id(elm_id,timeout = 20):
    _check_alert_to_close()
    logging.info("Waiting element shown, id: " + elm_id)
    element = WebDriverWait(driver, timeout).until(
        EC.visibility_of_element_located((By.ID, elm_id))
    )

def _wait_for_xpath(xpath,timeout = 20):
    _check_alert_to_close()
    logging.info("Waiting for xpath: " + xpath)
    element = WebDriverWait(driver, timeout).until(
        EC.visibility_of_element_located((By.XPATH,xpath))
    )
	
def _wait_for_elm_by_name(name,timeout = 20):
    _check_alert_to_close()
    logging.info("Waiting element shown, name: " + name)
    element = WebDriverWait(driver, timeout).until(
        EC.visibility_of_element_located((By.NAME, name))
    )

#点击确认关闭alert弹出框
def _check_alert_to_close():
    try:
        alert = driver.switch_to_alert()
        alert.accept()
        logging.info("Alert accepted")
    except:
        pass

#填写name为elmId的输入框，如果输入框已经有值则忽略
def _textInputByName(elmId,val,timeout=20,duplicate_substr=None):
	
    if val == None:
	    val=''
    else:
	    logging.info("Waiting Input name: " + elmId)
    _check_alert_to_close()
    #等待该id的元素就绪(页面存在)
    
    elm_ipt_x = WebDriverWait(driver, timeout).until(
        EC.presence_of_element_located((By.NAME, elmId))
    )
    
    #获取input框原来的value值
    elm_ipt_val = elm_ipt_x.get_attribute("value");
    if elm_ipt_val == "":
        elm_ipt_x.send_keys(val)
    else:
        logging.info(elmId + " value is not empty,ignore sendkeys")
    logging.info("\"" + elmId + "\"" + ":" + "\"" + val + "\",")

#填写id为elmId的输入框，如果输入框已经有值则忽略
def _textInputById(elmId,val,timeout=20,duplicate_substr=None):
	
    if val == None:
	    val=''
    else:
	    logging.info("Waiting Input name: " + elmId)
    _check_alert_to_close()
    #等待该id的元素就绪(页面存在)
    
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
def _buttonById(elmId,timeout=20):
    _check_alert_to_close()
    logging.info("Waiting Button id: " + elmId)
    elm_btn_x = driver.find_element_by_id(elmId)
    driver.execute_script("arguments[0].scrollIntoView();", elm_btn_x) #拖动到元素去
    elm_btn_x.click()
    logging.info("Click Button id: " + elmId)
	
#個人名簿登録
def _buttonByCustomerNameAndCount(customerName,count,buttonName="個人名簿登録"):
    xpath="//tr/td/div[contains(text(),'"+customerName+"') and contains(text(),'"+count+"')]/../../following-sibling::tr[1]/td/input[@value='"+buttonName+"']"
    _wait_for_xpath(xpath)
    target=driver.find_element_by_xpath(xpath)
    driver.execute_script("arguments[0].scrollIntoView();", target) #拖动到可见的元素去
    target.click()

#点击name为elmName的按钮
def _buttonByName(elmName,timeout=20):
    _check_alert_to_close()
    logging.info("Waiting Button name: " + elmName)
    elm_btn_x = driver.find_element_by_name(elmName)
    driver.execute_script("arguments[0].scrollIntoView();", elm_btn_x) #拖动到元素去
    elm_btn_x.click()
    logging.info("Click Button name: " + elmName)

#勾选单选组
def _radioBoxVisableByName(radioName,value,isSelectId1="false",timeout=20):
    _check_alert_to_close()
    elm_ipt_x = driver.find_element_by_xpath("//input[@name='"+radioName+"' and @value='"+value+"']")
    #是否选中第一个
    if isSelectId1:
        elm_ipt_x = driver.find_element_by_xpath("//body/descendant::input[@name='"+radioName+"'][1]") ;    
    elm_ipt_x.click()

def callbackfunc(blocknum, blocksize, totalsize):
    '''回调函数
    @blocknum: 已经下载的数据块
    @blocksize: 数据块的大小
    @totalsize: 远程文件的大小
    '''
    global url
    percent = 100.0 * blocknum * blocksize / totalsize
    if percent > 100:
        percent = 100
    downsize=blocknum * blocksize
    if downsize >= totalsize:
    	downsize=totalsize
    s ="%.2f%%"%(percent)+"====>"+"%.2f"%(downsize/1024/1024)+"M/"+"%.2f"%(totalsize/1024/1024)+"M \r"
    sys.stdout.write(s)
    sys.stdout.flush()
    if percent == 100:
        print('')

def feedBackError(requrl,errorCode,errorMsg="未知错误"):
    '''向服务器反馈错误信息
    @requrl:请求地址
    @errorCode: 错误类型,1代表受付番号未生成之前,数据错误,修改后再次递送即可;2代表个人名簿登录失败;3代表归国报告上传失败
    @errorMsg: 错误消息
    '''
    try:
        payload  = {'errorCode':errorCode,'errorMsg':errorMsg}
        res = requests.post(url = requrl,data=payload)
        result = res.text
        sys.exit(0)
    except Exception as e:
        logging.info("反馈错误信息失败")
        logging.info(e)
    
#函数定义  end
#主流程开始
# Main Process Start
task_id=sys.argv[2]
logging.info("task_id:" + task_id)
server_host="http://218.244.148.21:9004"
feed_back_url=server_host + "/visa/simulator/japanErrorHandle/"	+ task_id

if not sys.platform.startswith('win'):
    import coloredlogs
    coloredlogs.install(level='INFO')
logging.basicConfig(stream=sys.stdout,format='%(levelname)s:%(message)s', level=logging.INFO)
try:
    #import JSON data file,从文件加载json数据，命令行的第二个参数为完整文件名
    data_json = None
    file_name = sys.argv[1]
    logging.info("Import json data from file,use utf-8 : %s",file_name)
    #注意,json文件也请使用utf-8编码保存
    with open(file_name,encoding="utf-8") as data_file:
        data_json = json.load(data_file)
        data_file.close()
    
    # 打开火狐浏览器并且跳转到签证网站
    profileDir = "C:/Users/user/AppData/Roaming/Mozilla/Firefox/Profiles/e0dheleu.default"
    profile = webdriver.FirefoxProfile(profileDir)
    workDir=os.path.abspath(os.path.join(os.getcwd(), "./tmp"))
    pprint("workDir:" + workDir) 
    #自定义下载路径
    profile.set_preference("browser.download.dir",workDir)
    #使用自定义下载路径
    profile.set_preference('browser.download.folderList', 2) 
    #不显示下载管理器
    profile.set_preference('browser.download.manager.showWhenStarting', False) 
    #MIME
    profile.set_preference("browser.helperApps.neverAsk.saveToDisk", "application/pdf")
    #自动下载pdf必须添加
    profile.set_preference("plugin.disable_full_page_plugin_for_types", "application/pdf")
    profile.set_preference("pdfjs.disabled", True)
    
    driver = webdriver.Firefox(profile)
    driver.get("https://churenkyosystem.com/member/login.php")
    
    #1. login
    answer = data_json["visaAccount"]
    if not answer:
        answer = "1507-001"
    _textInputByName("LOGIN_ID",answer)
    
    answer = data_json["visaPasswd"]
    if not answer:
        answer = "kintsu2017"
    	
    _textInputByName("PASSWORD",answer)
    _buttonByName("SUBMIT_LOGIN_x")
    
    #2. text input applicant info 
    # Make sure page has been complete loaded
    _wait_for_elm_by_id("container")
    driver.find_element_by_xpath("//li[@class='navi_05']").click()
    
    time.sleep(3)
    _wait_for_elm_by_id("container")
    logging.info("Load Page Title :%s",driver.title)
    
    #指定番号
    answer = data_json["agentNo"]
    if not answer:
        answer = "gtu-sh-057-0"
    
    _textInputById("CHINA_AGENT_CODE",answer)
    _buttonByName("BTN_SEARCH")
    
    #签证类型
    #单次
    answer = data_json["visaType1"]
    if ("0"==answer):
        answer = "2"
        elm_ipt_x = driver.find_element_by_xpath("//input[@name='VISA_TYPE_1' and @value='"+answer+"']/..")
        elm_ipt_x.click()
    #多次1
    elif ("2"==answer) or ("3"==answer) or ("4"==answer):
        answer = "N"
        elm_ipt_x = driver.find_element_by_xpath("//input[@name='VISA_TYPE_1' and @value='"+answer+"']/..")
        elm_ipt_x.click()
        driver.find_element_by_xpath("//input[@id='VISA_3' and @value='3']/..").click()
        
        VISA_STAY_PREF_47=data_json["VISA_STAY_PREF_47"]
        if VISA_STAY_PREF_47:
            driver.find_element_by_xpath("//input[@id='VISA_STAY_PREF_47']/../label[@for='VISA_STAY_PREF_47']").click()
        
        VISA_STAY_PREF_2=data_json["VISA_STAY_PREF_2"]
        if VISA_STAY_PREF_2:
            driver.find_element_by_xpath("//input[@id='VISA_STAY_PREF_2']/../label[@for='VISA_STAY_PREF_2']").click()
            
        VISA_STAY_PREF_3=data_json["VISA_STAY_PREF_3"]
        if VISA_STAY_PREF_3:
            driver.find_element_by_xpath("//input[@id='VISA_STAY_PREF_3']/../label[@for='VISA_STAY_PREF_3']").click()
        
        VISA_STAY_PREF_4=data_json["VISA_STAY_PREF_4"]
        if VISA_STAY_PREF_4:
            driver.find_element_by_xpath("//input[@id='VISA_STAY_PREF_4']/../label[@for='VISA_STAY_PREF_4']").click()

        VISA_STAY_PREF_5=data_json["VISA_STAY_PREF_5"]
        if VISA_STAY_PREF_5:
            driver.find_element_by_xpath("//input[@id='VISA_STAY_PREF_5']/../label[@for='VISA_STAY_PREF_5']").click()
        
        VISA_STAY_PREF_6=data_json["VISA_STAY_PREF_6"]
        if VISA_STAY_PREF_6:
            driver.find_element_by_xpath("//input[@id='VISA_STAY_PREF_6']/../label[@for='VISA_STAY_PREF_6']").click()
   
        VISA_STAY_PREF_7=data_json["VISA_STAY_PREF_7"]
        if VISA_STAY_PREF_7:
            driver.find_element_by_xpath("//input[@id='VISA_STAY_PREF_7']/../label[@for='VISA_STAY_PREF_7']").click()
        
        #默认東北三県（岩手県、宮城県、福島県）への訪問の有無（通過は含まない）* 無
        driver.find_element_by_xpath("//input[@id='VISA_VISIT_TYPE_0']/..").click()                                   
    #多次2    
    elif ("5"==answer):
        answer = "N"
        elm_ipt_x = driver.find_element_by_xpath("//input[@name='VISA_TYPE_1' and @value='"+answer+"']/..")
        elm_ipt_x.click()
        driver.find_element_by_xpath("//input[@id='VISA_4' and @value='4']/..").click()
    else:
        logging.info("签证类型错误 :%s",answer)
        feedBackError(feed_back_url,1,"签证类型错误")
            
    #申请人姓名
    answer = data_json["proposerNameCN"]
    logging.info("proposerNameCN :%s",answer)
    elm_input = driver.find_element_by_name("APPLICANT_NAME")
    driver.execute_script("arguments[0].scrollIntoView();", elm_input)
    elm_input.send_keys(answer)
    
    answer = data_json["proposerNameEN"]
    _textInputByName("APPLICANT_PINYIN",answer)
    #人数
    applicant_count = data_json["applicantCnt"]
    _textInputByName("NUMBER_OF_TOURISTS",applicant_count)
    
    #出入境时间
    answer = data_json["entryDate"]
    pprint("entryDate:" + answer)
    driver.execute_script("document.getElementById('ARRIVAL_DATE').removeAttribute('readonly',0);")
    
    arrivalDate = driver.find_element_by_id("ARRIVAL_DATE")
    arrivalDate.clear()
    arrivalDate.send_keys(answer)
    arrivalDate.send_keys(Keys.TAB)
    
    answer = data_json["leaveDate"]
    pprint("leaveDate:" + answer)
    driver.execute_script("document.getElementById('DEPARTURE_DATE').removeAttribute('readonly',0);")
    
    departureDate = driver.find_element_by_id("DEPARTURE_DATE")
    departureDate.clear()
    departureDate.send_keys(answer)
    departureDate.send_keys(Keys.TAB)
    _check_alert_to_close()
    
    #点击确认
    _check_alert_to_close()
    _buttonByName("BTN_CHECK_x")
    
    #确认信息页
    time.sleep(3)
    _wait_for_elm_by_id("container")
    _radioBoxVisableByName("MAIL_STATUS","1")
    #-------------------------------------------------------点击确认生成受付番号------------------------------------------
    _buttonByName("BTN_CHECK_SUBMIT_x")
    _check_alert_to_close()
except Exception as e:
    logging.info(e)
    feedBackError(feed_back_url,1,"数据错误,请修改后再递送")

#3. 列表页，点击個人名簿登録
# 如果 不出现对应的按钮，则代表美元生成受付番号，需要重新填写
# Make sure page has been complete loaded
_wait_for_elm_by_id("container")
driver.find_element_by_xpath("//li[@class='navi_01']").click()
time.sleep(3)
_wait_for_elm_by_id("container")
logging.info("Load Page Title :%s",driver.title)
#列表页根据指定番号进行检索
answer = data_json["agentNo"]
if not answer:
    answer = "GTP-BJ-084-0"

_textInputById("CHINA_AGENT_CODE",answer)
_buttonByName("BTN_SEARCH_CHINA_AGENT")
time.sleep(2)
_wait_for_xpath("//input[@name='BTN_SEARCH_x']")
_buttonByName("BTN_SEARCH_x")

#获取受付番号
customerName = data_json["proposerNameCN"]
acceptance_number = None
try:
    xpath="//tr/td/div[contains(text(),'"+customerName+"') and contains(text(),'"+applicant_count+"')]/../../td/a"
    acceptance_number=driver.find_element_by_xpath(xpath).text
    #如果没有获取到受付番号，向服务器反馈错误信息
except Exception as e:
    logging.info(e)
    feedBackError(feed_back_url,1,"数据错误,请修改后再递送")

logging.info("受付番号 :%s",acceptance_number)
try:
    #名簿登録页
    answer = data_json["proposerNameCN"]
    _wait_for_elm_by_id("container",20)
    _buttonByCustomerNameAndCount(answer,applicant_count)
    _buttonByName("BTN_ADD_CSV_x")
    
    # Upload excel page
    # Make sure page has been complete loaded
    time.sleep(3)
    _wait_for_elm_by_id("container")
    logging.info("Load Page Title :%s",driver.title)
    
    #上传名簿,这里可能会因为文件数据问题，上传名簿失败
    answer = data_json["excelUrl"]
    _textInputByName("CSV_FILE",answer)
    _buttonByName("BTN_SUBMIT_x")
    _check_alert_to_close()
    time.sleep(3)
    
    #判断是否上传出错,如果找到errorMsg元素，则表示上传失败
    try:
        error_xpath="//input[@name='CSV_FILE']/./following-sibling::p[@class='errorMsg']"
        upload_error=driver.find_element_by_xpath(error_xpath)
        feedBackError(feed_back_url,2,"個人名簿登録失败")
    except NoSuchElementException as e:
        logging.info("個人名簿登録成功")
except Exception as e:
    logging.info(e)
    feedBackError(feed_back_url,2,"個人名簿登録失败")

#归国报告
time.sleep(3)
_wait_for_xpath("//li[@class='navi_04']")
driver.find_element_by_xpath("//li[@class='navi_04']").click()

time.sleep(3)
_wait_for_xpath("//li[@class='navi_01']")
driver.find_element_by_xpath("//li[@class='navi_01']").click()
_wait_for_elm_by_id("container")
logging.info("Load Page Title :%s",driver.title)
answer = data_json["agentNo"]
if not answer:
    answer = "GTP-BJ-084-0"

_textInputById("CHINA_AGENT_CODE",answer)
_buttonByName("BTN_SEARCH_CHINA_AGENT")
time.sleep(2)
_wait_for_xpath("//input[@name='BTN_SEARCH_x']")
_buttonByName("BTN_SEARCH_x")

_wait_for_elm_by_id("container")
answer = data_json["proposerNameCN"]
_wait_for_elm_by_id("container",20)
try:
    _buttonByCustomerNameAndCount(answer,applicant_count,"帰国報告")
    _wait_for_elm_by_id("container")
    _buttonByName("BTN_CHECK_x")

    _wait_for_elm_by_id("container")
    _buttonByName("BTN_CHECK_SUBMIT_x")

    #每次下载归国报告书之前判断文件夹里是否存在名为mpdf.pdf以及任务id为名的pdf文件，有的话则删除
    default_download_name=workDir+"\\mpdf.pdf"
    pdf_name=workDir+"\\"+task_id+".pdf"
    if os.path.isfile(default_download_name):
        os.remove(default_download_name)

    if os.path.isfile(pdf_name):
        os.remove(pdf_name)

    #点击归国报告书表示(下载归国报告书)
    _wait_for_elm_by_id("container")
    _check_alert_to_close()
    elm_btn = driver.find_element_by_xpath("//input[@name='BTN_BACK_x' and @value='帰国報告書の表示']")
    driver.execute_script("arguments[0].scrollIntoView();", elm_btn) 
    driver.implicitly_wait(10)
    elm_btn.click()
    time.sleep(3)

    #等待下载完毕
    flag = 1
    while flag == 1 :
        if os.path.isfile(default_download_name):
            flag = 0
except Exception as e:
    feedBackError(feed_back_url,3,"获取帰国報告失败")
    logging.info(e)

#重命名,因为重命名文件导致元数据丢失，重命名后的pdf文件打不开，暂时不使用重命名了
'''
if os.path.isfile(default_download_name):
    try:
        logging.info("default_download_name:" + default_download_name)
        os.rename(default_download_name,pdf_name)
        logging.info("[success]download file successfully.")    
    except urllib.error.HTTPError as e: 
        logging.info(e)
else:
    logging.info("帰国報告書下载失败")
'''

#python脚本下载方式,未使用，留作参考
'''
download_url=""
flag = 1
while flag == 1 :
    # 获取当前窗口句柄集合
    handles = driver.window_handles
    #切换窗口
    for handle in handles:
        driver.switch_to_window(handle)
        download_url = driver.current_url
        pprint(download_url)  # 输出当前窗口url
        if "download_return_report.php"  in download_url:
            flag = 0
            break

pdf_name=workDir+"\\"+task_id+".pdf"	
logging.info("pdf_name:" + pdf_name)
try:
    pprint("download_url:" + download_url)
    urllib.request.urlretrieve(download_url, pdf_name, callbackfunc)
    pprint("[succ]download file successfully.")    
except urllib.error.HTTPError as e: 
    pprint(e)
'''	

time.sleep(3)
#上传文件,上传完成后关闭文件
try:
    upload_url=server_host + "/visa/simulator/UploadJapan/"	+ task_id
    upload_file=open(default_download_name, 'rb')
    files = {'file':('report.pdf',upload_file,'application/pdf')}
    data = {'acceptanceNumber':acceptance_number}
    resp = requests.post(upload_url, files=files, data = data)
    pprint(resp.text)
    result = json.loads(resp.text)
    if "SUCCESS" == result['code']:
        logging.info("Upload SUCCESS，Task:" + task_id + "complete!")
        upload_file.close()
    else:
        feedBackError(feed_back_url,3,"帰国報告上传失败")
except Exception as e:
    feedBackError(feed_back_url,3,"帰国報告上传失败")
    logging.info(e)

#执行完成后把本地归国报告文件删除
if os.path.isfile(default_download_name):
    os.remove(default_download_name)   
driver.quit()