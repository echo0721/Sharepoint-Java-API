package hk.quantr.sharepoint.entity;

public class RestOfficeVo {

    private String groupId  ; //组id
    private String docType  ; //excel
    private String docName  ;//文档名称
    private String userId  ; //用户id

    public RestOfficeVo(String groupId, String docType, String docName, String userId) {
        this.groupId = groupId;
        this.docType = docType;
        this.docName = docName;
        this.userId = userId;
    }

    public RestOfficeVo() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
