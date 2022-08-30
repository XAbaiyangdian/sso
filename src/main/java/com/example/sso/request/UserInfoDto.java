package com.example.sso.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto extends BaseSignRequest{

    private String loginId;

    public UserInfoDto(String loginId) {
        this.loginId = loginId;
    }
}
