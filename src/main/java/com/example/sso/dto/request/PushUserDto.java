package com.example.sso.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PushUserDto extends BaseSignRequest {

    //登录名
    private String loginName;
    //手机号
    private String mobile;
    //公司名称
    private String company;
    //统一社会信用代码
    private String uscc;
    //U盾 序列号
    private String cfcaKeyId;
    //实名信息 名称
    private String realName;
    //实名信息 身份证号
    private String idCard;

    public PushUserDto(String loginName, String mobile, String company, String uscc, String cfcaKeyId, String realName, String idCard) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.company = company;
        this.uscc = uscc;
        this.cfcaKeyId = cfcaKeyId;
        this.realName = realName;
        this.idCard = idCard;
    }
}
