package com.example.sso;

import com.alibaba.fastjson.JSON;
import com.example.common.OkHttpClient;
import com.example.common.UrlUtil;
import com.example.sso.dto.request.SSOCheckTicketDto;
import com.example.sso.dto.request.SSOLogoutDto;
import com.example.sso.dto.request.UserInfoDto;
import com.example.sso.dto.response.SSOResult;
import com.example.sso.dto.response.UserInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SSOService {

    @Value("${ssoserver.url}")
    private String ssoServerUrl;


    public String buildServerAuthUrl(String customRedirectUrl, String back) {
        String redirectUrl = UrlUtil.joinParam(customRedirectUrl, SSOConsts.ParamName.back, back);
        String ssoServerLoginUrl = ssoServerUrl + SSOConsts.Api.ssoAuth;
        ssoServerLoginUrl = UrlUtil.joinParam(ssoServerLoginUrl, SSOConsts.ParamName.redirect, redirectUrl);
        return ssoServerLoginUrl;
    }

    public SSOResult checkTicket(String ticket, String customLogoutCallUrl, String clientCode, String clientSecretkey) {
        SSOCheckTicketDto ssoCheckTicketDto = new SSOCheckTicketDto(ticket, customLogoutCallUrl);
        ssoCheckTicketDto.setTimestamp(System.currentTimeMillis());
        ssoCheckTicketDto.setClientCode(clientCode);
        ssoCheckTicketDto.setSignature(SSOSignUtil.sign(ssoCheckTicketDto.toSignMap(), clientSecretkey));

        String checkTicketUrl = ssoServerUrl + SSOConsts.Api.ssoCheckTicket;
        String result = OkHttpClient.post(checkTicketUrl, ssoCheckTicketDto.toJsonString());
        log.info("checkTicket result: " + result);
        return JSON.parseObject(result, SSOResult.class);
    }

    public UserInfoResp getUserInfo(String userId, String clientCode, String clientSecretKey) {
        UserInfoDto userInfoDto = new UserInfoDto(userId);
        userInfoDto.setTimestamp(System.currentTimeMillis());
        userInfoDto.setClientCode(clientCode);
        userInfoDto.setSignature(SSOSignUtil.sign(userInfoDto.toSignMap(), clientSecretKey));

        String userInfoUrl = ssoServerUrl + SSOConsts.Api.ssoUserInfo;
        String result = OkHttpClient.post(userInfoUrl, userInfoDto.toJsonString());
        log.info("getUserInfo result: " + result);
        SSOResult ssoResult = JSON.parseObject(result, SSOResult.class);
        if (!ssoResult.isSuccess() || ssoResult.getData() == null) {
            return null;
        }
        return ssoResult.parseData(UserInfoResp.class);
    }

    public void logout(String userId, String clientCode, String clientSecretkey) {
        SSOLogoutDto ssoLogoutDto = new SSOLogoutDto(userId);
        ssoLogoutDto.setClientCode(clientCode);
        ssoLogoutDto.setTimestamp(System.currentTimeMillis());
        ssoLogoutDto.setSignature(SSOSignUtil.sign(ssoLogoutDto.toSignMap(), clientSecretkey));

        String logoutUrl = ssoServerUrl + SSOConsts.Api.ssoLogout;
        String result = OkHttpClient.post(logoutUrl, ssoLogoutDto.toJsonString());
        log.info("logout result: " + result);
    }

}
