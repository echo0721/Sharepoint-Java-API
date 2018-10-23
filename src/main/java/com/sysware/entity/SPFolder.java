package com.sysware.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SPFolder {

    private String groupId;//团队id
    private String uniqueId;//SP端id
    private String name; //sp端 name
    private String ServerRelativeUrl; //相对路径

}
