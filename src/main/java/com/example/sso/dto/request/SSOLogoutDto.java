package com.example.sso.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SSOLogoutDto extends BaseSignRequest{

    private String userId;

    public SSOLogoutDto(String userId) {
        this.userId = userId;
    }
}
