package com.smd21.smdinsole.guard.controller;

import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.guard.service.GuardService;
import com.smd21.smdinsole.shoes.service.ShoesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "보호자 입력")
    @RequestMapping(value = "/ins/{shoesNo}", method = RequestMethod.POST)
    public GuardianModel insGuardInfo(@RequestBody GuardianModel guardInfo, @PathVariable long shoesNo) throws Exception {

        guardInfo = guardService.insGuardInfo(guardInfo, shoesNo);
        return guardInfo;
    }
}
