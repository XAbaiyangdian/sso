package com.example.sso;

import com.alibaba.fastjson.JSON;
import com.example.common.OkHttpClient;
import com.example.sso.dto.request.PushUserDto;
import com.example.sso.dto.response.SSOResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PushService {
    @Value("${ssoserver.url}")
    private String ssoServerUrl;

    @Value("${ssoclient.clientCode}")
    private String clientCode;

    @Value("${ssoclient.clientSecretKey}")
    private String clientSecretKey;

    public SSOResult pushUser(String mobile, String uscc, String cfcaKeyId, String realName, String idCard) {
        PushUserDto pushUserDto = new PushUserDto(mobile, uscc, cfcaKeyId, realName, idCard);
        pushUserDto.setTimestamp(System.currentTimeMillis());
        pushUserDto.setClientCode(clientCode);
        pushUserDto.setSignature(SSOSignUtil.sign(pushUserDto.toSignMap(), clientSecretKey));

        String pushUserUrl = ssoServerUrl + SSOConsts.Api.ssoPushUser;
        String result = OkHttpClient.post(pushUserUrl, pushUserDto.toJsonString());
        log.info("pushUser result: " + result);
        return JSON.parseObject(result, SSOResult.class);
    }
}
