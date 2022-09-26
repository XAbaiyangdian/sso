package com.example.sso.dto.response;

import lombok.Data;

@Data
public class CheckTicketResp {

    //用户id
    private String userId;
    //登录名
    private String loginName;
    //统一社会信用代码
    private String uscc;
    //手机号
    private String mobile;
    //u盾序列还
    private String cfcaKeyId;
}
