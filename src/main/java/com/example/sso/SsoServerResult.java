package com.example.sso;

import lombok.Data;

@Data
public class SsoServerResult {

    //200 or 500
    private Integer code;
    private String msg;
    private Object data;

    public Boolean isSuccess() {
        return this.code != null && this.code == 200;
    }

}
