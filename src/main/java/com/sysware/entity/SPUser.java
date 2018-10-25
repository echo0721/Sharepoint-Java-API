package com.sysware.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SPUser {
    private String userId; //eap 端用户id
    private String username;//SharePoint 端用户名
    private String password;//SharePoint 端密码
    private String principalId; // sharepoint 中用户的 权限id(组id)
}
