package com.example;

import com.example.sso.CustomAuthLogic;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public class AuthFilter implements Filter {

    @Resource
    private CustomAuthLogic customAuthLogic;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //一些接口需要放过登陆校验
        if (!skipAuth(httpServletRequest.getRequestURI()) && !customAuthLogic.isLogin(httpServletRequest)) {
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.getOutputStream().write(1);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public Boolean skipAuth(String uri) {
        return "/".equals(uri) ||
                "/index".equals(uri) ||
                "/custom/login".equals(uri) ||
                "/custom/logoutCall".equals(uri) ||
                uri.startsWith("/js/");
    }
}
