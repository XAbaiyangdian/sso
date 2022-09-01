package com.example.sso.dto.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SSOResult {
    private int status;
    private String message;
    private Object data;

    public boolean isSuccess() {
        return this.status == 1;
    }

    public <T> T parseData(Class<T> clazz) {
        if (data == null) {
            return null;
        }
        if (data instanceof String) {
            return JSON.parseObject((String) data, clazz);
        }
        return JSON.parseObject(JSON.toJSONString(data), clazz);
    }
}
