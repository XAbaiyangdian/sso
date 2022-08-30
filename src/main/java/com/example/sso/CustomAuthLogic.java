package com.example.sso;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

//各支付链自定义实现用户会话、权限校验逻辑
@Service
public class CustomAuthLogic {
    private static final String tokenName = "custom_token";
    private Map<String, String> tokenLoginIdMap = new HashMap<>();
    private Map<String, String> loginIdtokenMap = new HashMap<>();

    //例子中使用cookie保存登陆凭证，各支付链可自定义实现。
    public String getCustomTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null || request.getCookies().length == 0) {
            return null;
        }
        return Stream.of(request.getCookies())
                .filter(cookie -> tokenName.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public void setCustomTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void saveSession(String token, String loginId) {
        tokenLoginIdMap.put(token, loginId);
        loginIdtokenMap.put(loginId, token);
    }

    public String deleteSessionByToken(String token) {
        String loginId = tokenLoginIdMap.get(token);
        tokenLoginIdMap.remove(token);
        loginIdtokenMap.remove(loginId);
        return loginId;
    }

    public String deleteSessionByLoginId(String loginId) {
        String token = loginIdtokenMap.get(loginId);
        loginIdtokenMap.remove(loginId);
        tokenLoginIdMap.remove(token);
        return token;
    }

    public Boolean isLogin(HttpServletRequest request) {
        String token = getCustomTokenFromCookie(request);
        return StringUtils.isNotBlank(token) && tokenLoginIdMap.containsKey(token);
    }
}
