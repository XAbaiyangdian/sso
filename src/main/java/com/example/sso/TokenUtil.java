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
public class TokenUtil {
    private static final String tokenName = "custom_token";
    private Map<String, String> tokenUserIdStore = new HashMap<>();
    private Map<String, String> userIdTokenStore = new HashMap<>();

    //例子中使用cookie保存登陆凭证，各支付链可自定义实现。
    public String importToken(HttpServletRequest request) {
        if (request.getCookies() == null || request.getCookies().length == 0) {
            return null;
        }
        return Stream.of(request.getCookies())
                .filter(cookie -> tokenName.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public void exportToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void save(String token, String userId) {
        tokenUserIdStore.put(token, userId);
        userIdTokenStore.put(userId, token);
    }

    public String delete(String token) {
        String userId = tokenUserIdStore.get(token);
        tokenUserIdStore.remove(token);
        userIdTokenStore.remove(userId);
        return userId;
    }

    public String deleteByUserId(String userId) {
        String token = userIdTokenStore.get(userId);
        userIdTokenStore.remove(userId);
        tokenUserIdStore.remove(token);
        return token;
    }

    public Boolean isLogin(HttpServletRequest request) {
        String token = importToken(request);
        return StringUtils.isNotBlank(token) && tokenUserIdStore.containsKey(token);
    }
}
