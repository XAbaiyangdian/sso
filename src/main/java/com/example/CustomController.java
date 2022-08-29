package com.example;

import com.alibaba.fastjson.JSON;
import com.example.common.RestResp;
import com.example.sso.SsoConsts;
import com.example.sso.SsoLogoutDto;
import com.example.sso.SsoServerResult;
import com.example.sso.SsoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@Slf4j
public class CustomController {

    @Resource
    private SsoService ssoService;
    @Resource
    private CustomAuthLogic customAuthLogic;

    @PostMapping("/custom/loginverify")
    public RestResp loginverify(HttpServletRequest request) {
        return customAuthLogic.isLogin(request) ? RestResp.success(1) : RestResp.success(0);
    }

    //客户端登陆接口
    @GetMapping("/custom/login")
    public Object login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String back = request.getParameter(SsoConsts.ParamName.back);
        String ticket = request.getParameter(SsoConsts.ParamName.ticket);

        //1. 已登录无需重复登录
        if (customAuthLogic.isLogin(request)) {
            back = StringUtils.isNotBlank(back) ? back : "/";
            response.sendRedirect(back);
            return null;
        }

        //2. 客户端登陆 重定向到ssoserver
        if (StringUtils.isBlank(ticket)) {
            String serverAuthUrl = ssoService.buildServerAuthUrl(back);
            response.sendRedirect(serverAuthUrl);
        } else {
            //3. ssoserver登陆后重定向回客户端登陆接口
            SsoServerResult ssoServerResult = ssoService.checkTicket(ticket);
            if (!ssoServerResult.isSuccess()) {
                throw new Exception(ssoServerResult.getMsg());
            }
            String loginId = (String) ssoServerResult.getData();
            //获取到loginId后 以下为客户端自定义的前后端保持会话方案，这里仅为例子。
            String token = UUID.randomUUID().toString();

            customAuthLogic.saveSession(token, loginId);
            customAuthLogic.setCustomTokenToCookie(response, token);

            response.sendRedirect(back);
        }
        return null;
    }

    //被各客户端自己的用户调用
    @PostMapping("/custom/logout")
    public RestResp logout(HttpServletRequest request) {
        if (!customAuthLogic.isLogin(request)) {
            return RestResp.fail("非登陆状态");
        }
        String token = customAuthLogic.getCustomTokenFromCookie(request);
        String loginId = customAuthLogic.deleteSessionByToken(token);
        if (StringUtils.isNotBlank(loginId)) {
            ssoService.logout(loginId);
        }
        return RestResp.success();
    }

    //仅被ssoserver调用
    //当任一客户端有用户退出登陆后， ssoserver 会调用所有注册过的客户端的此接口来进行统一退出登陆， 需要校验ssoserver的签名。
    @PostMapping("/custom/logoutCall")
    public RestResp logoutCall(SsoLogoutDto ssoLogoutDto) {
        log.info("logoutCall: " + JSON.toJSONString(ssoLogoutDto));
        ssoService.logoutCall(ssoLogoutDto);
        customAuthLogic.deleteSessionByLoginId(ssoLogoutDto.getLoginId());
        return RestResp.success();
    }

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }
}
