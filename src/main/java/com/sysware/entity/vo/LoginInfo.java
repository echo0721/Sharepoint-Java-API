package com.sysware.entity.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

@Getter
@Setter
public class LoginInfo {
    private Long loginTime;
    private String username;
    private Pair<String, String> token ;
}
