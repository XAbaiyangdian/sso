package com.example.sso.response;

import lombok.Data;

@Data
public class CheckTicketResp {

    private String userId;

    private String mobilephone;

    private String cfcaKeyId;
}
