package com.smd21.smdinsole.guard.controller;

import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.guard.service.GuardService;
import com.smd21.smdinsole.shoes.service.ShoesService;
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
    public String getToken(@RequestBody GuardianModel guard) throws Exception {

        return guardService.getToken(guard.getGuardPhone(), guard.getGuadrPwd());
    }

    @ApiOperation(value = "비밀번호 변경")
    @RequestMapping(value = "/upd/pwd", method = RequestMethod.POST)
    public GuardianModel updGuardPwd(@RequestBody String guardPwd, @RequestBody String newGuardPwd) throws Exception {
        return guardService.changePassword(guardPwd, newGuardPwd);
    }

    @ApiOperation(value = "보호자 입력")
    @RequestMapping(value = "/ins/{shoesNo}", method = RequestMethod.POST)
    public GuardianModel insGuardInfo(@RequestBody GuardianModel guardInfo, @PathVariable long shoesNo) throws Exception {

        guardInfo = guardService.insGuardInfo(guardInfo, shoesNo);
        return guardInfo;
    }

    @ApiOperation(value = "보호자 리스트")
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    public List<GuardianModel> selGuardList(@ApiIgnore HttpServletRequest request) throws Exception {
        try {
            TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            long masterGuardNo = user.getMasterGuardNo();

            if(masterGuardNo == 0) masterGuardNo = user.getGuardNo();
            List<GuardianModel> guardList = guardService.selGuardianList(masterGuardNo);

            return guardList;
        } catch(Exception e) {
            return null;
        }
    }
}
