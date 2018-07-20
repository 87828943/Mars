package com.mars.controller;

import com.mars.common.aop.LoggerAnnotation;
import com.mars.entity.BillType;
import com.mars.entity.result.ResponseData;
import com.mars.service.BillTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillTypeService billTypeService;

    @ResponseBody
    @RequestMapping(value = "/getBillTypeName",method = RequestMethod.GET)
    @LoggerAnnotation(desc = "获取账单类型")
    public ResponseData getBillTypeName(){
        ResponseData responseData = new ResponseData();
        List<BillType> all = billTypeService.findAll();
        responseData.setData(all);
        return responseData;
    }
}
