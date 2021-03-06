package com.mars.common;


import com.mars.entity.User;
import com.mars.service.UserService;
import com.mars.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@WebFilter(urlPatterns = "/*")
public class MarsFilter implements Filter {
	@Autowired
	private UserService userService;
    //不拦截url列表
    private static List<String> urlList = new ArrayList<>();
    @Value("${mars.session.user.key}")
    private String MARS_SESSION_USER_KEY;
    @Value("${mars.cookie.user.key}")
    private String MARS_COOKIE_USER_KEY;

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
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestURI = request.getRequestURI();
        if(request.getSession().getAttribute(MARS_SESSION_USER_KEY) != null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
        	Cookie cookieByName = CookieUtil.getCookieByName(request, MARS_COOKIE_USER_KEY);
        	if(cookieByName!=null && StringUtils.isNotBlank(cookieByName.getValue())){
        		String value = cookieByName.getValue();
            	String userId = value.replace("userId=","");
            	if(userId != null){
            		User user = userService.findById(Long.parseLong(userId));
            		if(user!=null){
                        request.getSession().setAttribute(MARS_SESSION_USER_KEY, user);
                        filterChain.doFilter(servletRequest,servletResponse);
                    }else{
                        CookieUtil.addCookie(response, MARS_COOKIE_USER_KEY, null, 0);
                        //跳转主页
                        String html = "<script type=\"text/javascript\">window.location.href=\"/index\"</script>";
                        servletResponse.getWriter().write(html);
                    }
            	}
        	}else if(urlList.contains(requestURI) || containsSuffix(requestURI)){
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
