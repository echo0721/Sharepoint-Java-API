package com.sysware.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PermissionEnum {
    Edit("edit", "编辑"),
    View("view", "查看"),
    Contribute("contribute", "完全控制");

    private String id;
    private String name;

}

