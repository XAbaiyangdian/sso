package com.example.sso;

import com.alibaba.fastjson.JSON;
import com.example.common.OkHttpClient;
import com.example.common.UrlUtil;
import com.example.sso.request.PushUserDto;
import com.example.sso.request.SsoCheckTicketDto;
import com.example.sso.request.SsoLogoutDto;
import com.example.sso.request.UserInfoDto;
import com.example.sso.response.SsoResult;
import com.example.sso.response.UserInfoResp;
import lombok.extern.slf4j.Slf4j;
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
        String result = OkHttpClient.post(checkTicketUrl, ssoCheckTicketDto.toJsonString());
        log.info("checkTicket result: " + result);
        return JSON.parseObject(result, SsoResult.class);
    }

    public UserInfoResp getUserInfo(String userId, String clientCode, String clientSecretkey) {
        UserInfoDto userInfoDto = new UserInfoDto(userId);
        userInfoDto.setTimestamp(System.currentTimeMillis());
        userInfoDto.setClientCode(clientCode);
        userInfoDto.setSignature(SsoSignUtil.sign(userInfoDto.toSignMap(), clientSecretkey));

        String userInfoUrl = ssoServerUrl + SsoConsts.Api.ssoUserInfo;
        String result = OkHttpClient.post(userInfoUrl, userInfoDto.toJsonString());
        log.info("getUserInfo result: " + result);
        SsoResult ssoResult = JSON.parseObject(result, SsoResult.class);
        if (!ssoResult.isSuccess() || ssoResult.getData() == null) {
            return null;
        }
        return ssoResult.parseData(UserInfoResp.class);
    }

    public void logout(String userId, String clientCode, String clientSecretkey) {
        SsoLogoutDto ssoLogoutDto = new SsoLogoutDto(userId);
        ssoLogoutDto.setClientCode(clientCode);
        ssoLogoutDto.setTimestamp(System.currentTimeMillis());
        ssoLogoutDto.setSignature(SsoSignUtil.sign(ssoLogoutDto.toSignMap(), clientSecretkey));

        String logoutUrl = ssoServerUrl + SsoConsts.Api.ssoLogout;
        String result = OkHttpClient.post(logoutUrl, ssoLogoutDto.toJsonString());
        log.info("logout result: " + result);
    }

    public SsoResult pushUser(String mobilephone, String uscc, String cfcaKeyId, String realName, String idCard, String clientCode, String clientSecretkey) {
        PushUserDto pushUserDto = new PushUserDto(mobilephone, uscc, cfcaKeyId, realName, idCard);
        pushUserDto.setTimestamp(System.currentTimeMillis());
        pushUserDto.setClientCode(clientCode);
        pushUserDto.setSignature(SsoSignUtil.sign(pushUserDto.toSignMap(), clientSecretkey));

        String pushUserUrl = ssoServerUrl + SsoConsts.Api.ssoPushUser;
        String result = OkHttpClient.post(pushUserUrl, pushUserDto.toJsonString());
        log.info("pushUser result: " + result);
        return JSON.parseObject(result, SsoResult.class);
    }
}
