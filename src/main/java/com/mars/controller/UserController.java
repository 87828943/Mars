package com.mars.controller;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.Date;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mars.entity.User;
import com.mars.entity.result.ResponseData;
import com.mars.exception.MarsException;
import com.mars.service.UserService;
import com.mars.utils.CookieUtil;
import com.mars.utils.MD5Util;
import com.mars.utils.RedisUtil;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Value("${mars.md5.password.key}")
    private String MARS_MD5_PASSWORD_KEY;
    @Value("${mars.session.user.key}")
    private String MARS_SESSION_USER_KEY;
    @Value("${mars.cookie.user.key}")
    private String MARS_COOKIE_USER_KEY;
    @Value("${default.logo}")
    private String DEFAULT_LOGO;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    private ResponseData login(User user, HttpServletRequest request,HttpServletResponse response){
        if(user==null || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())){
            return new ResponseData(MarsException.PARAM_EXCEPTION);
        }
        String name = user.getName();
        String password = user.getPassword();
        User loginUser = userService.findByNameOrEmail(name, name);
        if(loginUser == null){
            return new ResponseData(MarsException.USER_EXIST);
        }
        if(!MD5Util.encrypt(MARS_MD5_PASSWORD_KEY + password).equals(loginUser.getPassword())){
            return new ResponseData(MarsException.PASSWORD_ERROR);
        }
        String cookieStr = "userId="+loginUser.getId().toString()+"#name="+loginUser.getName();
        String userCookie2 = loginUser.toString();
        //设置cookie
        CookieUtil.addCookie(response, MARS_COOKIE_USER_KEY, cookieStr, 24*60*7);
        //设置session
        HttpSession session = request.getSession();
        session.setAttribute(MARS_SESSION_USER_KEY,loginUser);
        session.setMaxInactiveInterval(3600*2);
        //但是当cookie关闭后，用于保存SessionID的JSESSIONID会消失(此时cookie并没有过期) ，所以得将JSESESSION持久化
        CookieUtil.addCookie(response, "JSESESSIONID", session.getId(), 2*60);
        return new ResponseData();
    }

    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    private ResponseData register(User user, HttpServletRequest request,HttpServletResponse response){
        if(user==null || StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())){
            return new ResponseData(MarsException.PARAM_EXCEPTION);
        }
        String email = user.getEmail();
        String name = user.getName();
        User isExistUser = userService.findByNameOrEmail(name, email);
        if(isExistUser!=null){
            if(email.equals(isExistUser.getEmail())){
                return new ResponseData(MarsException.EMAIL_EXIST);
            }
            if(name.equals(isExistUser.getName())){
                return new ResponseData(MarsException.NAME_EXIST);
            }
        }
        user.setCreateDate(new Date());
        user.setLogo(DEFAULT_LOGO);
        user.setPassword(MD5Util.encrypt(MARS_MD5_PASSWORD_KEY + user.getPassword()));
        User registerUser = userService.register(user);
        HttpSession session = request.getSession();
        session.setAttribute(MARS_SESSION_USER_KEY,registerUser);
        CookieUtil.addCookie(response, MARS_COOKIE_USER_KEY, user.toString(), 0);
        return new ResponseData();
    }


    @ResponseBody
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    private ResponseData logout(User user, HttpServletRequest request,HttpServletResponse response) throws Exception{
    	//首先是考虑编码问题
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
    	//false代表：不创建session对象，只是从request中获取。
        HttpSession session = request.getSession(false);
        if(session == null){
            return new ResponseData();
        }
        session.removeAttribute(MARS_SESSION_USER_KEY);
        CookieUtil.addCookie(response, MARS_COOKIE_USER_KEY, null, 0);
        return new ResponseData();
    }


    @ResponseBody
    @RequestMapping(value = "/forgotPassword",method = RequestMethod.POST)
    private ResponseData forgotPassword(String email,HttpServletRequest request){
        logger.info("forgotPassword email,toEmail:[{}]",email);
        if(StringUtils.isEmpty(email)){
            return new ResponseData(MarsException.PARAM_EXCEPTION);
        }
        User user = userService.findByEmail(email);
        if(user==null){
            return new ResponseData(MarsException.USER_EXIST);
        }
        String key = MD5Util.encrypt(user.getId()+"_"+UUID.randomUUID().toString()+"_"+user.getEmail()); // 密钥

        //设置秘钥10分钟有效期
        redisUtil.set(String.valueOf(user.getId()),key,10L,MINUTES);

        String url = "http://" + request.getServerName() + ":"+ request.getServerPort()+ request.getContextPath() + "/user/newPassword"+ "?eamil="+user.getEmail()+"&key="+key;
        String content ="您好，尊敬的："+user.getName()+" 先生/女士：<br/>　　请点击下方链接重置您的账号密码↓↓↓↓↓ <br/>                     "+"<a href='"+url+"' ><pre>"+url+"</pre></a><br/>    ps1:链接24小时内有效，失效后请重新操作；<br/>     ps2:链接打不开请复制到浏览器打开；<br/>     ps3:如若不是本人操作，请忽视本邮件。<br/>          <a href='http://www.guowenbo.top' target='_blank'><font color='red'>Mars</font></a>";

        if(!sendHtmlEmail(email,"[MARS-重置密码]",content)){
            return new ResponseData(MarsException.SENDEMAIL_ERROR);
        }
        return new ResponseData();
    }


    /*
    * 发送普通邮件
    * */
    boolean sendEmail(String toEmail,String subject,String content){
        try {
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setFrom(fromEmail);
            smm.setTo(toEmail);
            /* String[] adds = {"xxx@qq.com","yyy@qq.com"}; //同时发送给多人
            smm.setTo(adds);*/
            smm.setSubject(subject);//设置主题
            smm.setText(content);//设置内容
            javaMailSender.send(smm);//执行发送邮件
            logger.info("html email send success! toEmail:[{}]",toEmail);
        }catch (Exception e){
            logger.error("sendEmail error,toEmail:[{}]",toEmail,e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /*
    * 发送html邮件
    * */
    boolean sendHtmlEmail(String toEmail,String subject,String content){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
            logger.info("html email send success! toEmail:[{}]",toEmail);
        }catch (Exception e){
            logger.error("sendEmail error,toEmail:[{}]",toEmail,e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    /**
     * 
	 * @Description: 查询用户详情 
     * @author: zhaoxingxing
     * @date: 2018年6月22日 上午11:39:05
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/editUser",method = RequestMethod.POST)
    private String editUser(Model model,HttpServletRequest request){
    	HttpSession session = request.getSession();
    	User user = (User)session.getAttribute(MARS_SESSION_USER_KEY);
    	Cookie cookieByName = CookieUtil.getCookieByName(request, MARS_COOKIE_USER_KEY);
    	Long id = user.getId();
    	User user2 = userService.findById(id);
    	model.addAttribute(user2);
        return "user/editUser";
    }
}
