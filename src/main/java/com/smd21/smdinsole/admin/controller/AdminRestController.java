package com.smd21.smdinsole.admin.controller;

import com.smd21.smdinsole.admin.model.CodeModel;
import com.smd21.smdinsole.admin.service.AdminService;
import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.guard.model.GuardLoginModel;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;
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

@RequestMapping("/${project.name}/api/admin")
@RestController
@RequiredArgsConstructor
@Api(description = "관리자 Controller (단말기, 보호자 관리)")
public class AdminRestController {
    final static Logger logger = LoggerFactory.getLogger(AdminRestController.class);

    @Autowired
    AdminService adminService;

    @ApiOperation(value = "로그인", notes="rows, page")
    @RequestMapping(value = "/get/token", method = RequestMethod.POST)
    public String getToken(@RequestParam String loginId, @RequestParam String password) throws Exception {

        return adminService.getToken(loginId, password);
    }

    @ApiOperation(value = "단말정보 입력")
    @RequestMapping(value = "/shoes/ins", method = RequestMethod.POST)
    public ShoesInfoModel insShoesInfo(@RequestBody ShoesInfoModel shoesInfo) throws Exception {

        adminService.insShoesInfo(shoesInfo);
        return shoesInfo;
    }

    @ApiOperation(value = "단말정보 수정")
    @RequestMapping(value = "/shoes/upd", method = RequestMethod.POST)
    public ShoesInfoModel updShoesInfo(@RequestBody ShoesInfoModel shoesInfo) throws Exception {

        adminService.updShoesInfo(shoesInfo);
        return shoesInfo;
    }

    @ApiOperation(value = "단말 List 조회")
    @RequestMapping(value = "/shoes/list", method = RequestMethod.POST)
    public List<ShoesInfoModel> selShoesInfoList(@ApiIgnore HttpServletRequest request) throws Exception {

        TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        long masterGuardNo = user.getMasterGuardNo();

        if(masterGuardNo == 0) masterGuardNo = user.getGuardNo();
        List<ShoesInfoModel> shoesList = adminService.selShoesInfoListByGuard(masterGuardNo);

        return shoesList;
    }



}
