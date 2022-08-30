package com.example.sso.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SsoLogoutDto extends BaseSignRequest{

    private String loginId;

    public SsoLogoutDto(String loginId) {
        this.loginId = loginId;
    }
}
