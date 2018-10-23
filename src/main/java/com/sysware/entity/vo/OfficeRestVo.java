package com.sysware.entity.vo;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfficeRestVo {

    private String userId  ; //eap中用户id
    private String groupId  ; //组id
    private String docName  ;//文档名称
    private String docType  ; //excel
    private String fileId ; //fileServer 中文件id
    private String shareId; //被共享用户id
    private String permission ; //协同编辑权限 .
    private String newName; // 重命名的新名称
    private String version ;// 版本号
    private String data ;//请求data
    private String urlStuff ; //url后的参数

}
