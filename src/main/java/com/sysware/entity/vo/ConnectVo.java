package com.sysware.entity.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

@Getter
@Setter
public class ConnectVo {
    private String url;
    private String data;
    private String userId;
    private Pair<String, String> token ;
    private String domain;
    private String password;
    private String formDigestValue;
}
