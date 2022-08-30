package com.example.sso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SsoSignUtil {

    public static String sign(Map map, String secretKey) {
        String linkString = createLinkString(map);
        linkString += "&secretKey=" + secretKey;
        return encryptSHA256(linkString).toUpperCase();
    }

    public static Boolean checkSign(Map map, String signature, String secretKey) {
        String linkString = createLinkString(map);
        linkString += "&secretKey=" + secretKey;
        System.out.println("checkSign: " + linkString);
        if (encryptSHA256(linkString).toUpperCase().equals(signature)) {
            return true;
        }
        return false;
    }
    
    public static String encryptSHA256(String src) {
        if (src == null) {
            return null;
        }
        MessageDigest md = null;
        String result = null;
        byte[] b = src.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(b);

            result = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encryptSHA256(byte[] b) {
        if (b == null) {
            return null;
        }
        MessageDigest md = null;
        String result = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(b);

            result = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        StringBuffer buffer = new StringBuffer();
        for (String key : keys) {
            Object value = params.get(key);
            if (!keys.get(0).equals(key)) {
                buffer.append("&");
            }
            buffer.append(key + "=" + value);
        }
        return buffer.toString();
    }
}
