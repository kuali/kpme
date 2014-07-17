package org.kuali.kpme.edo.admin.web;

import org.apache.struts.upload.FormFile;
import org.kuali.kpme.edo.base.web.EdoForm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * $HeadURL$
 * $Revision$
 * Created with IntelliJ IDEA.
 * User: bradleyt
 * Date: 2/13/14
 * Time: 11:31 AM
 */
public class EdoAdminGroupMembersForm extends EdoForm {

    private Map<String,String> workflowSelectList;
    private List<String> departmentSelectList;
    private List<String> schoolSelectList;
    private List<String> campusSelectList;
    private Map<BigDecimal,String> reviewLevelSelectList;
    private FormFile UploadMbrFile;
    private String memberData;
    private String reviewLayersJSON;

    public String getReviewLayersJSON() {
        return reviewLayersJSON;
    }

    public void setReviewLayersJSON(String reviewLayersJSON) {
        this.reviewLayersJSON = reviewLayersJSON;
    }

    public Map<String, String> getWorkflowSelectList() {
        return workflowSelectList;
    }

    public void setWorkflowSelectList(Map<String, String> workflowSelectList) {
        this.workflowSelectList = workflowSelectList;
    }

    public List<String> getDepartmentSelectList() {
        return departmentSelectList;
    }

    public void setDepartmentSelectList(List<String> departmentSelectList) {
        this.departmentSelectList = departmentSelectList;
    }

    public List<String> getSchoolSelectList() {
        return schoolSelectList;
    }

    public void setSchoolSelectList(List<String> schoolSelectList) {
        this.schoolSelectList = schoolSelectList;
    }

    public List<String> getCampusSelectList() {
        return campusSelectList;
    }

    public void setCampusSelectList(List<String> campusSelectList) {
        this.campusSelectList = campusSelectList;
    }

    public Map<BigDecimal,String> getReviewLevelSelectList() {
        return reviewLevelSelectList;
    }

    public void setReviewLevelSelectList(Map<BigDecimal,String> reviewLevelSelectList) {
        this.reviewLevelSelectList = reviewLevelSelectList;
    }

    public FormFile getUploadMbrFile() {
        return UploadMbrFile;
    }

    public void setUploadMbrFile(FormFile uploadMbrFile) {
        UploadMbrFile = uploadMbrFile;
    }

    public String getMemberData() {
        return memberData;
    }

    public void setMemberData(String memberData) {
        this.memberData = memberData;
    }
}
