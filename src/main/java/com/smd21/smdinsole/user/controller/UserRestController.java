package com.smd21.smdinsole.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/${project.name}/api/user")
@RestController
@RequiredArgsConstructor
@Api(description = "User 관리 Controller")
public class UserRestController {

    final static Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @ApiOperation(value = "User 리스트를 조회한다.", notes="rows, page")
//    @ApiImplicitParams({
//            //@ApiImplicitParam(name = "파라미터 명(영문)", value = "파라미터 명(한글)", required = true, dataType = "파라미터 타입(string, int)", paramType="파라미터 괄호안의 문자열"),
//            @ApiImplicitParam(name = "parameter1", value = "파라미터1", dataType = "string", required = true, paramType="required"),
//            @ApiImplicitParam(name = "more", value = "more", required = false, dataType = "string")
//    })
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    @ResponseBody
    public String selUserId() throws Exception {

        return "user";
    }
}
