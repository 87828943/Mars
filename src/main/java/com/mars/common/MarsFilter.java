package com.mars.common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@WebFilter(urlPatterns = "/*")
public class MarsFilter implements Filter {

    //不拦截url列表
    private static List<String> urlList = new ArrayList<>();
    @Value("${mars.session.user.key}")
    private String MARS_SESSION_USER_KEY;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urlList.add("/index");
        urlList.add("/login");
        urlList.add("/register");
        urlList.add("/forgotPassword");
        urlList.add("/user/login");
        urlList.add("/user/register");
        urlList.add("/user/forgotPassword");
        urlList.add("/user/newPassword");
        urlList.add("/user/setNewPassword");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String requestURI = request.getRequestURI();
        if(request.getSession().getAttribute(MARS_SESSION_USER_KEY) !=null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            if(urlList.contains(requestURI) || containsSuffix(requestURI)){
                filterChain.doFilter(servletRequest,servletResponse);
            }else{
                //跳转主页
                String html = "<script type=\"text/javascript\">window.location.href=\"/index\"</script>";
                servletResponse.getWriter().write(html);
            }
        }
    }

    //不拦截资源文件后缀
    private boolean containsSuffix(String url) {
        if (url.endsWith(".js")
                || url.endsWith(".css")
                || url.endsWith(".jpg")
                || url.endsWith(".gif")
                || url.endsWith(".png")
                || url.endsWith(".html")
                || url.endsWith(".eot")
                || url.endsWith(".svg")
                || url.endsWith(".ttf")
                || url.endsWith(".woff")
                || url.endsWith(".ico")
                || url.endsWith(".woff2")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void destroy() {

    }
}
