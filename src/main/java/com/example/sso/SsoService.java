package com.example.sso;

import com.alibaba.fastjson.JSON;
import com.example.common.OkHttpClient;
import com.example.common.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class SsoService {

    @Value("${ssoserver.url}")
    private String ssoServerUrl;
    @Value("${ssoserver.secretkey}")
    private String ssoServerSecretkey;
    @Value("${custom.redirect.url}")
    private String customRedirectUrl;
    @Value("${custom.logoutcall.url}")
    private String customLogoutCallUrl;


    public String buildServerAuthUrl(String back) {
        String redirectUrl = UrlUtil.joinParam(customRedirectUrl, SsoConsts.ParamName.back, back);
        String ssoServerLoginUrl = ssoServerUrl + "/" + SsoConsts.Api.ssoAuth;
        ssoServerLoginUrl = UrlUtil.joinParam(ssoServerLoginUrl, SsoConsts.ParamName.redirect, redirectUrl);
        return ssoServerLoginUrl;
    }

    public SsoServerResult checkTicket(String ticket) {
        String checkTicketUrl = ssoServerUrl + "/" + SsoConsts.Api.ssoCheckTicket;
        Map<String, Object> params = new HashMap<>();
        params.put(SsoConsts.ParamName.ticket, ticket);
        params.put(SsoConsts.ParamName.ssoLogoutCall, customLogoutCallUrl);
        String result = OkHttpClient.postForm(checkTicketUrl, params);
        log.info("checkTicket result: " + result);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        SsoServerResult ssoServerResult = JSON.parseObject(result, SsoServerResult.class);
        return ssoServerResult;
    }

    public void logout(String loginId) {
        SsoLogoutDto ssoLogoutDto = new SsoLogoutDto(loginId, String.valueOf(System.currentTimeMillis()), UUID.randomUUID().toString());
        ssoLogoutDto.setSign(ssoLogoutDto.generateSign(ssoServerSecretkey));
        String logoutUrl = ssoServerUrl + "/" + SsoConsts.Api.ssoLogout;
        String result = OkHttpClient.postForm(logoutUrl, ssoLogoutDto.toMap());
        log.info("logout result: " + result);
    }

    public void logoutCall(SsoLogoutDto ssoLogoutDto) {
        if (!ssoLogoutDto.checkTimestamp()) {
            throw new IllegalArgumentException("logoutCall 超时");
        }
        if (!ssoLogoutDto.checkSign(ssoServerSecretkey)) {
            throw new IllegalArgumentException("logoutCall 统一认证中心签名校验失败");
        }
    }

}
