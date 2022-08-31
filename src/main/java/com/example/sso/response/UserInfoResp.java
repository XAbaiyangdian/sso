package com.example.sso.response;

import lombok.Data;

@Data
public class UserInfoResp {

    private String userId;

    private String mobilephone;

    private String cfcaKeyId;

    private String uscc;

    private String realName;

    private String idCard;
}
