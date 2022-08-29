package com.example.sso;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
public class SsoLogoutDto {

    private String loginId;

    private String timestamp;

    private String nonce;

    private String sign;

    public SsoLogoutDto(String loginId, String timestamp, String nonce) {
        this.loginId = loginId;
        this.timestamp = timestamp;
        this.nonce = nonce;
    }

    public Boolean checkTimestamp() {
        long disparity = Math.abs(System.currentTimeMillis() - Long.valueOf(this.timestamp));
        //10s
        return disparity <= 10 * 1000;
    }

    public Boolean checkSign(String secretkey) {
        String sign = this.generateSign(secretkey);
        return StringUtils.isNotBlank(this.sign) && this.sign.equals(sign);
    }

    public Map toMap() {
        Map<String, Object> map = new TreeMap<>();
        map.put("loginId", loginId);
        map.put("timestamp", timestamp);
        map.put("nonce", nonce);
        map.put("sign", sign);
        return map;
    }

    public String generateSign(String secretkey) {
        Map<String, Object> map = new TreeMap<>();
        map.put("loginId", loginId);
        map.put("timestamp", timestamp);
        map.put("nonce", nonce);

        String paramsStr = SignUtil.joinParams(map);
        String fullStr = paramsStr + "&key=" + secretkey;
        return SignUtil.md5(fullStr);
    }


}
