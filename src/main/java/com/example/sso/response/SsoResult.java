package com.example.sso.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SsoResult {
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;
    private Integer code;
    private String msg;
    private Object data;

    public Boolean isSuccess() {
        return this.code != null && this.code == CODE_SUCCESS;
    }

    public static SsoResult error() {
        return new SsoResult(CODE_ERROR);
    }

    public SsoResult(Integer code) {
        this.code = code;
    }


}
