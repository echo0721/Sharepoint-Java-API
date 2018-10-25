package com.sysware.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SPFile {
    private String uniqueId ; //SharePoint端文件id
    private String serverRelativeUrl ; // SharePoint端文件夹 相对路径
    private String name ;    //名称
    private String linkingUrl ; //      下载链接
    private String version ;
    private String itemId ;  //lists中的itemId
    private String docType ;  //文档类型

}
