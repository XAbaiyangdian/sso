package com.example.sso;

import com.alibaba.fastjson.JSON;
import com.example.common.OkHttpClient;
import com.example.common.UrlUtil;
import com.example.sso.request.PushUserDto;
import com.example.sso.request.SsoCheckTicketDto;
import com.example.sso.request.SsoLogoutDto;
import com.example.sso.request.UserInfoDto;
import com.example.sso.response.SsoResult;
import com.example.sso.response.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SsoService {

    @Value("${ssoserver.url}")
    private String ssoServerUrl;


    public String buildServerAuthUrl(String customRedirectUrl, String back) {
        String redirectUrl = UrlUtil.joinParam(customRedirectUrl, SsoConsts.ParamName.back, back);
        String ssoServerLoginUrl = ssoServerUrl + SsoConsts.Api.ssoAuth;
        ssoServerLoginUrl = UrlUtil.joinParam(ssoServerLoginUrl, SsoConsts.ParamName.redirect, redirectUrl);
        return ssoServerLoginUrl;
    }

    public SsoResult checkTicket(String ticket, String customLogoutCallUrl, String clientCode, String clientSecretkey) {
        SsoCheckTicketDto ssoCheckTicketDto = new SsoCheckTicketDto(ticket, customLogoutCallUrl);
        ssoCheckTicketDto.setTimestamp(System.currentTimeMillis());
        ssoCheckTicketDto.setClientCode(clientCode);
        ssoCheckTicketDto.setSignature(SsoSignUtil.sign(ssoCheckTicketDto.toSignMap(), clientSecretkey));

        String checkTicketUrl = ssoServerUrl + SsoConsts.Api.ssoCheckTicket;
        String result = OkHttpClient.postForm(checkTicketUrl, ssoCheckTicketDto.toMap());
        log.info("checkTicket result: " + result);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        try {
            return JSON.parseObject(result, SsoResult.class);
        } catch (Exception e) {
            return null;
        }
    }

    public UserInfo getUserInfo(String loginId, String clientCode, String clientSecretkey) {
        UserInfoDto userInfoDto = new UserInfoDto(loginId);
        userInfoDto.setTimestamp(System.currentTimeMillis());
        userInfoDto.setClientCode(clientCode);
        userInfoDto.setSignature(SsoSignUtil.sign(userInfoDto.toSignMap(), clientSecretkey));

        String userInfoUrl = ssoServerUrl + SsoConsts.Api.ssoUserInfo;
        String result = OkHttpClient.postForm(userInfoUrl, userInfoDto.toMap());
        log.info("getUserInfo result: " + result);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        SsoResult ssoResult = JSON.parseObject(result, SsoResult.class);
        if (!ssoResult.isSuccess() || ssoResult.getData() == null) {
            return null;
        }
        try {
            return JSON.parseObject(JSON.toJSONString(ssoResult.getData()), UserInfo.class);
        } catch (Exception e) {
            return null;
        }
    }

    public SsoResult logout(String loginId, String clientCode, String clientSecretkey) {
        SsoLogoutDto ssoLogoutDto = new SsoLogoutDto(loginId);
        ssoLogoutDto.setClientCode(clientCode);
        ssoLogoutDto.setTimestamp(System.currentTimeMillis());
        ssoLogoutDto.setSignature(SsoSignUtil.sign(ssoLogoutDto.toSignMap(), clientSecretkey));

        String logoutUrl = ssoServerUrl + SsoConsts.Api.ssoLogout;
        String result = OkHttpClient.postForm(logoutUrl, ssoLogoutDto.toMap());
        log.info("logout result: " + result);
        if (StringUtils.isBlank(result)) {
            return SsoResult.error();
        }
        try {
            return JSON.parseObject(result, SsoResult.class);
        } catch (Exception e) {
            return null;
        }
    }

    public SsoResult pushUser(String mobilephone, String uscc, String cfcaKeyId, String clientCode, String clientSecretkey) {
        PushUserDto pushUserDto = new PushUserDto(mobilephone, uscc, cfcaKeyId);
        pushUserDto.setTimestamp(System.currentTimeMillis());
        pushUserDto.setClientCode(clientCode);
        pushUserDto.setSignature(SsoSignUtil.sign(pushUserDto.toSignMap(), clientSecretkey));

        String pushUserUrl = ssoServerUrl + SsoConsts.Api.ssoPushUser;
        String result = OkHttpClient.postForm(pushUserUrl, pushUserDto.toMap());
        log.info("pushUser result: " + result);
        if (StringUtils.isBlank(result)) {
            return SsoResult.error();
        }
        try {
            return JSON.parseObject(result, SsoResult.class);
        } catch (Exception e) {
            return null;
        }
    }
}
