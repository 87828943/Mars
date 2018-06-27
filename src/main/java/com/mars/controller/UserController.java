package com.mars.controller;

import com.mars.common.aop.LoggerAnnotation;
import com.mars.entity.User;
import com.mars.entity.result.ResponseData;
import com.mars.exception.MarsException;
import com.mars.service.UserService;
import com.mars.utils.CookieUtil;
import com.mars.utils.MD5Util;
import com.mars.utils.RedisUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.MINUTES;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Value("${mars.md5.password.key}")
    private String MARS_MD5_PASSWORD_KEY;
    @Value("${mars.session.user.key}")
    private String MARS_SESSION_USER_KEY;
    @Value("${mars.cookie.user.key}")
    private String MARS_COOKIE_USER_KEY;
    @Value("${default.logo}")
    private String DEFAULT_LOGO;
    @Value("${mars.logo.folder}")
    private String UPLOADED_FOLDER;
    @Value("${mars.base.uri}")
    private String BASE_URI;

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
    @LoggerAnnotation(desc = "用户登录")
    public ResponseData login(User user, HttpServletRequest request,HttpServletResponse response){
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
        String cookieStr = "userId="+loginUser.getId().toString();
        //设置cookie
        CookieUtil.addCookie(response, MARS_COOKIE_USER_KEY, cookieStr, 24*60*60*7);
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
    @LoggerAnnotation(desc = "用户注册")
    public ResponseData register(User user, HttpServletRequest request,HttpServletResponse response){
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
    @LoggerAnnotation(desc = "用户注销")
    public ResponseData logout(User user, HttpServletRequest request,HttpServletResponse response) throws Exception{
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
    @LoggerAnnotation(desc = "找回密码发送邮件")
    public ResponseData forgotPassword(String email,HttpServletRequest request){
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

        String url = "http://" + request.getServerName() + ":"+ request.getServerPort()+ request.getContextPath() + "/user/newPassword"+ "?email="+user.getEmail()+"&key="+key;
        String content ="您好，尊敬的："+user.getName()+" 先生/女士：<br/>　　请点击下方链接重置您的账号密码↓↓↓↓↓ <br/>                     "+"<a href='"+url+"' ><pre>"+url+"</pre></a><br/>    ps1:链接24小时内有效，失效后请重新操作；<br/>     ps2:链接打不开请复制到浏览器打开；<br/>     ps3:如若不是本人操作，请忽视本邮件。<br/>          <a href='http://www.guowenbo.top' target='_blank'><font color='red'>Mars</font></a>";

        if(!sendHtmlEmail(email,"[MARS-重置密码]",content)){
            return new ResponseData(MarsException.SENDEMAIL_ERROR);
        }
        return new ResponseData();
    }

    @ResponseBody
    @RequestMapping(value = "/setNewPassword",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "重置密码")
    public ResponseData setNewPassword(String newPassword,String email,String key){
        if(StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(email) || StringUtils.isEmpty(key)){
            return new ResponseData(MarsException.PARAM_EXCEPTION);
        }
        User user = userService.findByEmail(email);
        if(user==null){
            return new ResponseData(MarsException.USER_EXIST);
        }

        String s = (String)redisUtil.get(String.valueOf(user.getId()));
        if(StringUtils.isEmpty(s) || !s.equals(key)){
            return new ResponseData(MarsException.LINK_VALIDE);
        }
        redisUtil.remove(String.valueOf(user.getId()));

        userService.setNewPasswordById(MD5Util.encrypt(MARS_MD5_PASSWORD_KEY+newPassword),new Date(),user.getId());

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
    @RequestMapping(value = "/editUserInfo",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "编辑用户信息")
    public String editUser(Model model,HttpServletRequest request){
        return "user/editUserInfo";
    }

    @ResponseBody
    @RequestMapping(value = "/editUserLogo",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "更改用户头像")
    public ResponseData editUserLogo(@RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        if (file.isEmpty()) {
            logger.error("未选择图片！");
            return new ResponseData(MarsException.NOTFOUND_IMAGE);
        }
        Long userId = super.getUserId();
        String savePath = "";
        try {
            File fileDir = new File(UPLOADED_FOLDER);
            if(!fileDir.exists()){
                fileDir.mkdir();
            }
            byte[] bytes = file.getBytes();
            savePath = MD5Util.encrypt(userId + new Date().getTime() + file.getName()) +".jpg";
            Path path = Paths.get(UPLOADED_FOLDER+savePath);
            Files.write(path, bytes);
        } catch (Exception e) {
            logger.error("图片上传失败！",e);
            return new ResponseData(MarsException.FAILED);
        }
        User user = getUser();
        user.setLogo(BASE_URI+savePath);
        userService.setLogoById(BASE_URI+savePath,new Date(),userId);
        getSession().setAttribute(MARS_SESSION_USER_KEY,user);
        return new ResponseData();
    }    
    @ResponseBody
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "更改用户信息")
    public ResponseData updateUserInfo(User user, HttpServletRequest request, HttpServletResponse response){
        if (user != null && user.getSex() == null) {
            logger.error("参数异常");
            return new ResponseData(MarsException.PARAM_EXCEPTION);
        }
        Long userId = super.getUserId();
        String description = user.getDescription();
        Integer sex = user.getSex();
        userService.updateUserbyId(description,sex,new Date(),userId);
        getSession().setAttribute(MARS_SESSION_USER_KEY,user);
        return new ResponseData();
    }

    @ResponseBody
    @RequestMapping(value = "/editUserPassword",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "更改密码")
    public ResponseData editUserPassword(String newPassword,String oldPassword,HttpServletResponse response){
        if(StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(oldPassword)){
            return new ResponseData(MarsException.PARAM_EXCEPTION);
        }
        String password = getUser().getPassword();

        if(!password.equals(MD5Util.encrypt(MARS_MD5_PASSWORD_KEY+oldPassword))){
            return new ResponseData(MarsException.PASSWORD_ERROR);
        }

        userService.setNewPasswordById(MD5Util.encrypt(MARS_MD5_PASSWORD_KEY+newPassword),new Date(),getUserId());
        getSession().removeAttribute(MARS_SESSION_USER_KEY);
        CookieUtil.addCookie(response, MARS_COOKIE_USER_KEY, null, 0);
        return new ResponseData();
    }

}
