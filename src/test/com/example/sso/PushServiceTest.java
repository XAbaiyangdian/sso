package com.example.sso;

import com.example.sso.dto.response.SSOResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class PushServiceTest {

    @Resource
    private PushService pushService;
    @Test
    public void pushUser() {
        String mobile = "13211112222";
        String uscc = "91310000775785552L";
        String cfcaKeyId="xxxxxxxxxx";
        String realName="李彦宏";
        String idCard="420682198711075511";
        SSOResult ssoResult = pushService.pushUser(mobile, uscc, cfcaKeyId, realName, idCard);
        assert ssoResult.isSuccess();
        Object userId = ssoResult.getData();
    }
}