package com.example.sso.dto.response;

import lombok.Data;

@Data
public class CheckTicketResp {

    private String userId;

    private String mobile;

    private String cfcaKeyId;
}
