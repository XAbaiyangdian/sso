package com.example.sso.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class PushUserDto extends BaseSignRequest{

    private String mobilephone;

    //统一社会信用代码
    private String uscc;

    private String cfcaKeyId;

    public PushUserDto(String mobilephone, String uscc, String cfcaKeyId) {
        this.mobilephone = mobilephone;
        this.uscc = uscc;
        this.cfcaKeyId = cfcaKeyId;
    }
}
