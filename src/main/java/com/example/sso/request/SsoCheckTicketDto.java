package com.example.sso.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SsoCheckTicketDto extends BaseSignRequest{

    private String ticket;

    private String ssoLogoutCall;

    public SsoCheckTicketDto(String ticket, String ssoLogoutCall) {
        this.ticket = ticket;
        this.ssoLogoutCall = ssoLogoutCall;
    }
}
