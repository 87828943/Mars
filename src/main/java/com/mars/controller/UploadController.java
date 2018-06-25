package com.mars.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mars.common.aop.LoggerAnnotation;
import com.mars.entity.result.ResponseData;
import com.mars.utils.DateDealwithUtil;

@Controller
public class UploadController {

    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Value("${web.upload-path}")  
    private String  path;
    
    @Value("${server.context-path}")  
    private String  contxtePath; 

    @ResponseBody
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    @LoggerAnnotation(desc = "用户登录")
    public ResponseData upload(@RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest request,HttpServletResponse response)throws Exception{
    	ResponseData res = new ResponseData();
    	if (file != null) {  
            if (file.getName() != null || "".equals(file.getName())) {  
                String[] name = file.getContentType().split("/");  
                if ("BMP".equals(name[name.length - 1]) || "JPG".equals(name[name.length - 1])  
                        || "JPEG".equals(name[name.length - 1]) || "bmp".equals(name[name.length - 1])  
                        || "jpg".equals(name[name.length - 1]) || "jpeg".equals(name[name.length - 1])) {  
                    // 物理地址  
                    file.transferTo(new File(path + DateDealwithUtil.getSHC()));
                    res.setResMsg("上传成功!");  
                    // 网络地址  
                    res.setData(contxtePath + "/" + DateDealwithUtil.getSHC());
                    // 先删除原来的文件，再将网络地址写入数据库  
                    return res;  
                } else {  
                	res.setResCode("1");
                    res.setResMsg("请选择文件!");
                }  
            } else {  
            	res.setResCode("1");
                res.setResMsg("请选择文件!");
            }  
        } else {  
            res.setResCode("1");
            res.setResMsg("请选择文件!");
        }  
        return res;  
    }

    
}
