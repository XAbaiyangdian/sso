package com.example.sso.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SSOCheckTicketDto extends BaseSignRequest{

    private String ticket;

    private String ssoLogoutCall;

    public SSOCheckTicketDto(String ticket, String ssoLogoutCall) {
        this.ticket = ticket;
        this.ssoLogoutCall = ssoLogoutCall;
    }
}
