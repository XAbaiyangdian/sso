package com.example.sso;

import com.alibaba.fastjson.JSON;
import com.example.common.RestResp;
import com.example.sso.dto.request.SSOLogoutDto;
import com.example.sso.dto.response.CheckTicketResp;
import com.example.sso.dto.response.SSOResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
public class SSOController {

    @Resource
    private SSOService ssoService;
    @Resource
    private TokenUtil tokenUtil;

    @Value("${ssoclient.clientCode}")
    private String clientCode;
    @Value("${ssoclient.clientSecretKey}")
    private String clientSecretKey;
    @Value("${ssoclient.custom.redirect.url}")
    private String customRedirectUrl;
    @Value("${ssoclient.custom.logoutCall.url}")
    private String customLogoutCallUrl;

    @PostMapping("/custom/login_verify")
    public RestResp loginVerify() {
        return RestResp.success();
    }

    //客户端登陆接口
    @GetMapping("/custom/login")
    public Object login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String back = request.getParameter(SSOConsts.ParamName.back);
        String ticket = request.getParameter(SSOConsts.ParamName.ticket);

        //1. 已登录无需重复登录
        if (tokenUtil.isLogin(request)) {
            back = StringUtils.isNotBlank(back) ? back : "/";
            response.sendRedirect(back);
            return null;
        }

        //2. 客户端登陆 重定向到ssoserver
        if (StringUtils.isBlank(ticket)) {
            String serverAuthUrl = ssoService.buildServerAuthUrl(customRedirectUrl, back);
            response.sendRedirect(serverAuthUrl);
        } else {
            //3. 在ssoserver登陆后，携带ticket重定向回客户端登陆接口
            SSOResult ssoResult = ssoService.checkTicket(ticket, customLogoutCallUrl, clientCode, clientSecretKey);
            if (!ssoResult.isSuccess()) {
                throw new Exception(ssoResult.getMessage());
            }
            //userId mobilephone cfcaKeyId
            CheckTicketResp checkTicketResp = ssoResult.parseData(CheckTicketResp.class);
            //获取到userId后 以下为客户端自定义的前后端保持会话方案，这里仅为例子。
            String token = UUID.randomUUID().toString();

            tokenUtil.save(token, checkTicketResp.getUserId());
            tokenUtil.exportToken(response, token);

            response.sendRedirect(back);
        }
        return null;
    }

    //被各客户端自己的用户调用
    @PostMapping("/custom/logout")
    public RestResp logout(HttpServletRequest request) {
        String token = tokenUtil.importToken(request);
        String userId = tokenUtil.delete(token);
        if (StringUtils.isNotBlank(userId)) {
            ssoService.logout(userId, clientCode, clientSecretKey);
        }
        return RestResp.success();
    }

    //仅被ssoserver调用
    //当任一客户端有用户退出登陆后， ssoserver 会调用所有注册过的客户端的此接口来进行统一退出登陆， 需要校验ssoserver的签名。
    @PostMapping("/custom/logout_notify")
    public RestResp logoutCall(@RequestBody SSOLogoutDto ssoLogoutDto) {
        log.info("logoutCall: " + JSON.toJSONString(ssoLogoutDto));
        if (ssoLogoutDto.getTimestamp() < System.currentTimeMillis() - SSOConsts.TOKEN_TIMEOUT) {
            log.error("logoutCall: " + "时间戳校验失败");
            return RestResp.fail("时间戳校验失败");
        }
        if (!SSOSignUtil.checkSign(ssoLogoutDto.toSignMap(), ssoLogoutDto.getSignature(), clientSecretKey)) {
            log.error("logoutCall: " + "签名校验失败");
            return RestResp.fail("签名校验失败");
        }
        tokenUtil.deleteByUserId(ssoLogoutDto.getUserId());
        return RestResp.success();
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @RequestMapping("/")
    public void defaultPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index");
    }
}
