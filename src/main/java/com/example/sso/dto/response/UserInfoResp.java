package com.example.sso.dto.response;

import lombok.Data;

@Data
public class UserInfoResp {
    //用户id
    private String userId;
    //登录名
    private String loginName;
    //手机号
    private String mobile;
    //U盾 序列号
    private String cfcaKeyId;
    //公司名称
    private String company;
    //统一社会信用代码
    private String uscc;
    //实名信息 名称
    private String realName;
    //实名信息 身份证号
    private String idCard;
}
