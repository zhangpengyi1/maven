package com.zxs.demotest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zxs.common.annotation.SysLog;
import com.zxs.common.base.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "test")
@RestController
@RequestMapping("/test")
public class TestController {

    @ApiOperation("test")
    @SysLog(value = "test")
    @GetMapping("/hello")
    public R hello(){
        return R.ok().put("data", "hello world！");
    }

}
