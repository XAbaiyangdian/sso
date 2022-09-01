package com.example.sso.dto.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class BaseSignRequest {
    private Long timestamp;
    private String clientCode;
    private String signature;

    public Map<String, Object> toSignMap() {
        String s = JSON.toJSONString(this, SerializerFeature.IgnoreNonFieldGetter, SerializerFeature.WriteMapNullValue);
        Map map = JSON.parseObject(s, Map.class);
        map.remove("signature");
        return map;
    }
    public String toJsonString() {
        return JSON.toJSONString(this, SerializerFeature.IgnoreNonFieldGetter, SerializerFeature.WriteMapNullValue);
    }


}
