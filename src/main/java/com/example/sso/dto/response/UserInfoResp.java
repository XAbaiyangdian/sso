package com.example.sso.dto.response;

import lombok.Data;

@Data
public class UserInfoResp {

    private String userId;

    private String mobile;

    private String cfcaKeyId;

    private String uscc;

    private String realName;

    private String idCard;
}
