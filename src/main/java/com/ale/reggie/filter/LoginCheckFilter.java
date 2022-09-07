package com.ale.reggie.filter;

import com.ale.reggie.common.BaseContext;
import com.ale.reggie.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取请求url
        String requestURI = request.getRequestURI();

        //打印日志
        log.info("拦截到请求: {}", requestURI);

        //放行的url
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //判断请求是否需要处理
        boolean check = check(urls, requestURI);

        //如果不需要处理 则放行
        if (check) {
            log.info("本次请求不需要处理");
            filterChain.doFilter(request, response);
            return;
        }

        //如果登录 则放行 且将id保存入ThreadLocal域中
        Long id = (Long) request.getSession().getAttribute("employee");
        if (id != null) {
            log.info("用户已登录，本次请求不需要处理");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }

        Long id1 = (Long) request.getSession().getAttribute("user");
        if (id1 != null) {
            log.info("用户已登录，本次请求不需要处理");
            BaseContext.setCurrentId(id1);
            filterChain.doFilter(request, response);
            return;
        }

        //未登录 则处理
        log.info("用户未登录，本次请求被拦截");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    //路径匹配，检查本次请求是否需要放行

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}

