package com.example.sso.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class PushUserDto extends BaseSignRequest {

    private String mobilephone;
    //统一社会信用代码
    private String uscc;
    //U盾 id
    private String cfcaKeyId;
    //实名信息 名称
    private String realName;
    //实名信息 身份证号
    private String idCard;

    public PushUserDto(String mobilephone, String uscc, String cfcaKeyId, String realName, String idCard) {
        this.mobilephone = mobilephone;
        this.uscc = uscc;
        this.cfcaKeyId = cfcaKeyId;
        this.realName = realName;
        this.idCard = idCard;
    }
}
