package com.example.sso.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SsoLogoutDto extends BaseSignRequest{

    private String userId;

    public SsoLogoutDto(String userId) {
        this.userId = userId;
    }
}
