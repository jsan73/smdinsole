package com.kokasin.insole.guard.controller;

import com.kokasin.insole.guard.model.GuardLoginModel;
import com.kokasin.insole.guard.model.GuardianModel;
import com.kokasin.insole.guard.service.GuardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/api/guard")
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

    @ApiOperation(value = "로그인 체크", notes="rows, page")
    @RequestMapping(value = "/get/check", method = RequestMethod.POST)
    public long getGuardCheck(@RequestBody GuardLoginModel guard) throws Exception {

        return guardService.getGuardCheck(guard.getGuardPhone());
    }

    @ApiOperation(value = "가입", notes="rows, page")
    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public int regGuard(@RequestBody GuardianModel guard) throws Exception {

        return guardService.regGuardian(guard);
    }

    @ApiOperation(value = "비밀번호 변경")
    @RequestMapping(value = "/upd/pwd", method = RequestMethod.POST)
    public int updGuardPwd(@RequestParam String guardPwd, @RequestParam String newGuardPwd) throws Exception {
        return guardService.changePassword(guardPwd, newGuardPwd);
    }

    @ApiOperation(value = "보호자 입력")
    @RequestMapping(value = "/ins/{deviceNo}", method = RequestMethod.POST)
    public GuardianModel insGuardInfo(@RequestBody GuardianModel guardInfo, @PathVariable long deviceNo) throws Exception {

        int retVal = guardService.insGuardInfo(guardInfo, deviceNo);
        if(retVal == 0) return null;

        return guardInfo;
    }

    @ApiOperation(value = "보호자 삭제")
    @RequestMapping(value = "/del/{guardNo}", method = RequestMethod.POST)
    public int insGuardInfo( @PathVariable long guardNo) throws Exception {
        return guardService.delGuardian(guardNo);
    }

    @ApiOperation(value = "보호자 리스트")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<GuardianModel> selGuardList(@ApiIgnore HttpServletRequest request) throws Exception {

        return guardService.selGuardianList();
    }
}
