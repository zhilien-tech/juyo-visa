# Page 12: input the Work&Edu - Present info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
# No addtional info needed options are: H(HomeMaker),RT(Retired),N(Not Employed)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_ddlPresentOccupation"]
_select("ctl00_SiteContentPlaceHolder_FormView1_ddlPresentOccupation",answer)
if(answer == "N"):
    logging.info("Not Employed,Skipped,Fill the reason.")
    answer = "the not employed reason is..."
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxExplainOtherPresentOccupation",answer)
elif(answer == "H" or answer == "RT"):
    logging.info("HomeMaker or Retired,Skipped")
else:
    answer = "ORGName"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchName",answer)
    answer = "ORG Address"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchAddr1",answer)
    answer = "BeiJing"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchCity",answer)
    answer = "Beijing"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_ADDR_STATE",answer)
    answer = "100083"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_ADDR_POSTAL_CD",answer)
    answer = "18687184553"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_TEL",answer)
    answer = "CHIN"
    _select("ctl00_SiteContentPlaceHolder_FormView1_ddlEmpSchCountry",answer)
    answer = "10000"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxCURR_MONTHLY_SALARY",answer)
    answer = "My duties is to make everything happen."
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxDescribeDuties",answer)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_ddlPresentOccupation")
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 13: input the Work&Edu - Previous info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblPreviouslyEmployed_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblPreviouslyEmployed_0","ctl00_SiteContentPlaceHolder_FormView1_rblPreviouslyEmployed_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl00_tbDescribeDuties")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl_InsertButtonPrevEmpl"]
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
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_addlink_idx_str + "_InsertButtonPrevEmpl"
            _button(elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbEmployerName"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbEmployerStreetAddress1"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbEmployerStreetAddress2"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbEmployerCity"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbxPREV_EMPL_ADDR_STATE"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbxPREV_EMPL_ADDR_POSTAL_CD"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_DropDownList2"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbEmployerPhone"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbJobTitle"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbSupervisorSurname"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbSupervisorGivenName"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_ddlEmpDateFromDay"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_ddlEmpDateFromMonth"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbxEmpDateFromYear"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_ddlEmpDateToDay"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_ddlEmpDateToMonth"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbxEmpDateToYear"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + elm_idx_str + "_tbDescribeDuties"
        _textInput(elm_id,val)
else:
    time.sleep(3)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblOtherEduc_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblOtherEduc_0","ctl00_SiteContentPlaceHolder_FormView1_rblOtherEduc_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl00_tbxSchoolToYear")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl_InsertButtonPrevEduc"]
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
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_addlink_idx_str + "_InsertButtonPrevEduc"
            _button(elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxSchoolName"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxSchoolAddr1"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxSchoolAddr2"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxSchoolCity"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxEDUC_INST_ADDR_STATE"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxEDUC_INST_POSTAL_CD"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_ddlSchoolCountry"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxSchoolCourseOfStudy"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_ddlSchoolFromDay"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_ddlSchoolFromMonth"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxSchoolFromYear"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_ddlSchoolToDay"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_ddlSchoolToMonth"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + elm_idx_str + "_tbxSchoolToYear"
        _textInput(elm_id,val)
else:
    time.sleep(3)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")

# Page 14: input the Work&Edu - Addtional info
# Make sure page has been complete loaded
time.sleep(3)
element = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_UpdateButton3")
logging.info("Load Page Title :%s",driver.title)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblCLAN_TRIBE_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblCLAN_TRIBE_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblCLAN_TRIBE_IND_1",answer)
if answer:
    answer = "CLANNAME"
    _textInput("ctl00_SiteContentPlaceHolder_FormView1_tbxCLAN_TRIBE_NAME",answer)
else:
    time.sleep(3)
elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlLANGUAGES_ctl00_InsertButtonLANGUAGE")
data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlLANGUAGES_ctl_InsertButtonLANGUAGE"]
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
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlLANGUAGES_ctl" + elm_addlink_idx_str + "_InsertButtonLANGUAGE"
        _button(elm_id)
    elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlLANGUAGES_ctl" + elm_idx_str + "_tbxLANGUAGE_NAME"
    _textInput(elm_id,val)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblCOUNTRIES_VISITED_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblCOUNTRIES_VISITED_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblCOUNTRIES_VISITED_IND_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlCountriesVisited_ctl00_InsertButtonCountriesVisited")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlCountriesVisited_ctl_InsertButtonCountriesVisited"]
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
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlCountriesVisited_ctl" + elm_addlink_idx_str + "_InsertButtonCountriesVisited"
            _button(elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlCountriesVisited_ctl" + elm_idx_str + "_ddlCOUNTRIES_VISITED"
        _select(elm_id,val)
else:
    time.sleep(3)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblORGANIZATION_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblORGANIZATION_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblORGANIZATION_IND_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlORGANIZATIONS_ctl00_InsertButtonORGANIZATION")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlORGANIZATIONS_ctl_InsertButtonORGANIZATION"]
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
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlORGANIZATIONS_ctl" + elm_addlink_idx_str + "_InsertButtonORGANIZATION"
            _button(elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlORGANIZATIONS_ctl" + elm_idx_str + "_tbxORGANIZATION_NAME"
        _textInput(elm_id,val)
else:
    time.sleep(3)
answer = False
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblSPECIALIZED_SKILLS_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblSPECIALIZED_SKILLS_IND_1",answer)
answer = data_json["ctl00_SiteContentPlaceHolder_FormView1_rblMILITARY_SERVICE_IND_0"]
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblMILITARY_SERVICE_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblMILITARY_SERVICE_IND_1",answer)
if answer:
    elm_ipt_x = _wait_for_elm_by_id("ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_InsertButtonMILITARY_SERVICE")
    time.sleep(3)
    data = data_json["ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl_InsertButtonMILITARY_SERVICE"]
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
            elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_addlink_idx_str + "_InsertButtonMILITARY_SERVICE"
            _button(elm_id)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_ddlMILITARY_SVC_CNTRY"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_tbxMILITARY_SVC_BRANCH"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_tbxMILITARY_SVC_RANK"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_tbxMILITARY_SVC_SPECIALTY"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_ddlMILITARY_SVC_FROMDay"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_ddlMILITARY_SVC_FROMMonth"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_tbxMILITARY_SVC_FROMYear"
        _textInput(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_ddlMILITARY_SVC_TODay"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_ddlMILITARY_SVC_TOMonth"
        _select(elm_id,val)
        elm_id = "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl" + elm_idx_str + "_tbxMILITARY_SVC_TOYear"
        _textInput(elm_id,val)
else:
    time.sleep(3)
answer = False
_radiobox("ctl00_SiteContentPlaceHolder_FormView1_rblINSURGENT_ORG_IND_0","ctl00_SiteContentPlaceHolder_FormView1_rblINSURGENT_ORG_IND_1",answer)
#Button
time.sleep(3)
_button("ctl00_SiteContentPlaceHolder_UpdateButton3")