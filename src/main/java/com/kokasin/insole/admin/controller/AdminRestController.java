package com.kokasin.insole.admin.controller;

import com.kokasin.insole.admin.service.AdminService;
import com.kokasin.insole.device.model.DeviceInfoModel;
import com.kokasin.insole.common.model.TokenUserModel;
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

@RequestMapping("/api/admin")
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
    @RequestMapping(value = "/device/ins", method = RequestMethod.POST)
    public DeviceInfoModel insDeviceInfo(@RequestBody DeviceInfoModel deviceInfo) throws Exception {

        adminService.insDeviceInfo(deviceInfo);
        return deviceInfo;
    }

    @ApiOperation(value = "단말정보 수정")
    @RequestMapping(value = "/device/upd", method = RequestMethod.POST)
    public DeviceInfoModel updDeviceInfo(@RequestBody DeviceInfoModel deviceInfo) throws Exception {

        adminService.updDeviceInfo(deviceInfo);
        return deviceInfo;
    }

    @ApiOperation(value = "단말 List 조회")
    @RequestMapping(value = "/device/list", method = RequestMethod.POST)
    public List<DeviceInfoModel> selDeviceInfoList(@ApiIgnore HttpServletRequest request) throws Exception {

        TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        long masterGuardNo = user.getMasterGuardNo();

        if(masterGuardNo == 0) masterGuardNo = user.getGuardNo();
        List<DeviceInfoModel> deviceList = adminService.selDeviceInfoListByGuard(masterGuardNo);

        return deviceList;
    }



}
