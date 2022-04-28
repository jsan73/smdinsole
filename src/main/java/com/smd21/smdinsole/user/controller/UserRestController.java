package com.smd21.smdinsole.user.controller;

import com.smd21.smdinsole.user.model.GuardianModel;
import com.smd21.smdinsole.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/${project.name}/api")
@RestController
@RequiredArgsConstructor
@Api(description = "User 관리 Controller")
public class UserRestController {
    final static Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    UserService userService;

    @ApiOperation(value = "User 리스트를 조회한다.", notes="rows, page")
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    public String selUserId() throws Exception {

        return "user";
    }

    @ApiOperation(value = "로그인", notes="rows, page")
    @RequestMapping(value = "/get/token", method = RequestMethod.POST)
    public String getToken(@RequestBody GuardianModel guard) throws Exception {

        return userService.getToken(guard.getGuardPhone(), guard.getGuadrPwd());
    }
}
