package com.smd21.smdinsole.guard.controller;

import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.guard.model.GuardLoginModel;
import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.guard.service.GuardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/${project.name}/api/guard")
@RestController
@RequiredArgsConstructor
@Api(description = "보호자 관리 Controller")
public class GuardRestController {
    final static Logger logger = LoggerFactory.getLogger(GuardRestController.class);

    @Autowired
    GuardService guardService;

    @ApiOperation(value = "로그인", notes="rows, page")
    @RequestMapping(value = "/get/token", method = RequestMethod.POST)
    public String getToken(@RequestBody GuardLoginModel guard) throws Exception {

        return guardService.getToken(guard.getGuardPhone(), guard.getGuardPwd());
    }

    @ApiOperation(value = "비밀번호 변경")
    @RequestMapping(value = "/upd/pwd", method = RequestMethod.POST)
    public int updGuardPwd(@RequestParam String guardPwd, @RequestParam String newGuardPwd) throws Exception {
        return guardService.changePassword(guardPwd, newGuardPwd);
    }

    @ApiOperation(value = "보호자 입력")
    @RequestMapping(value = "/ins/{shoesNo}", method = RequestMethod.POST)
    public GuardianModel insGuardInfo(@RequestBody GuardianModel guardInfo, @PathVariable long shoesNo) throws Exception {

        int retVal = guardService.insGuardInfo(guardInfo, shoesNo);
        if(retVal == 0) return null;

        return guardInfo;
    }

    @ApiOperation(value = "보호자 리스트")
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    public List<GuardianModel> selGuardList(@ApiIgnore HttpServletRequest request) throws Exception {
        return guardService.selGuardianList();
    }
}
