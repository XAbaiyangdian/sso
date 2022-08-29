package com.example.sso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

public class SignUtil {
    public static String joinParams(Map<String, Object> paramsMap) {
        // 保证字段按照字典顺序排列
        if (paramsMap instanceof TreeMap == false) {
            paramsMap = new TreeMap<>(paramsMap);
        }

        // 按照 k1=v1&k2=v2&k3=v3 排列
        StringBuilder sb = new StringBuilder();
        for (String key : paramsMap.keySet()) {
            Object value = paramsMap.get(key);
            sb.append(key).append("=").append(value).append("&");
        }

        // 删除最后一位 &
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
    public static String md5(String str) {
        str = (str == null ? "" : str);
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] btInput = str.getBytes();
        MessageDigest mdInst = null;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        mdInst.update(btInput);
        byte[] md = mdInst.digest();
        int j = md.length;
        char[] strA = new char[j * 2];
        int k = 0;
        for (byte byte0 : md) {
            strA[k++] = hexDigits[byte0 >>> 4 & 0xf];
            strA[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(strA);
    }
}
