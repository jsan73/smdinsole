package com.kokasin.insole.device.controller;

import com.kokasin.insole.admin.service.AdminService;
import com.kokasin.insole.device.model.*;
import com.kokasin.insole.admin.model.CodeModel;
//import com.kokasin.insole.admin.service.AdminService;
import com.kokasin.insole.device.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequestMapping("/api/device")
@RestController
@RequiredArgsConstructor
@Api(description = "단말기 관리 Controller")
public class DeviceRestController {
    final static Logger logger = LoggerFactory.getLogger(DeviceRestController.class);

    final DeviceService deviceService;
    final AdminService adminService;

    @ApiOperation(value = "단말 List 조회")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<DeviceInfoModel> selDeviceInfoListByGuard(@ApiIgnore HttpServletRequest request) throws Exception {
        List<DeviceInfoModel> deviceList = deviceService.selDeviceInfoListByGuard();

        return deviceList;
    }

    @ApiOperation(value = "단말 정보 조회")
    @RequestMapping(value = "/get/{deviceIMEI}", method = RequestMethod.POST)
    public DeviceInfoModel getDeviceInfo(@PathVariable String deviceIMEI) throws Exception {
        DeviceInfoModel deviceInfo = deviceService.getDeviceInfo(deviceIMEI);

        return deviceInfo;
    }

    @ApiOperation(value = "단말 조회 List(Dashboard)")
    @RequestMapping(value = "/dashboard/list", method = RequestMethod.POST)
    public List<DashboardModel> selDeviceInfoList(@ApiIgnore HttpServletRequest request) throws Exception {
        List<DashboardModel> deviceList = deviceService.selDeviceInfoList(0);

        return deviceList;
    }

    @ApiOperation(value = "단말 조회 단일 (Dashboard)")
    @RequestMapping(value = "/dashboard/{deviceNo}", method = RequestMethod.POST)
    public DashboardModel getDashboardInfo(@PathVariable long deviceNo) throws Exception {
        List<DashboardModel> deviceList = deviceService.selDeviceInfoList(deviceNo);

        DashboardModel retValue = null;
        if(deviceList.size() > 0) retValue = deviceList.get(0);
        return retValue;
    }

    @ApiOperation(value = "단말 닉네임 변경")
    @RequestMapping(value = "/nickname/upd", method = RequestMethod.POST)
    public int insActiveRange(@RequestBody DeviceInfoModel device) throws Exception {
        int insResult = deviceService.updDeviceNickName(device);
        return insResult;
    }

    @ApiOperation(value = "활동 반경 추가")
    @RequestMapping(value = "/active/ins", method = RequestMethod.POST)
    public int insActiveRange(@RequestBody ActiveRangeModel activeRange) throws Exception {
        int insResult = deviceService.insActiveRange(activeRange);
        return insResult;
    }

    @ApiOperation(value = "활동 반경 삭제")
    @RequestMapping(value = "/active/del/{rangeNo}", method = RequestMethod.POST)
    public int insActiveRange(@PathVariable long rangeNo) throws Exception {
        return deviceService.delActiveRange(rangeNo);
    }

    @ApiOperation(value = "활동 반경 수정")
    @RequestMapping(value = "/active/upd", method = RequestMethod.POST)
    public int updActiveRange(@RequestBody ActiveRangeModel activeRange) throws Exception {
        return deviceService.updActiveRange(activeRange);
    }

    @ApiOperation(value = "활동 반경 리스트")
    @RequestMapping(value = "/active/list/{deviceNo}", method = RequestMethod.POST)
    public List<ActiveRangeModel> selActiveRangeList(@PathVariable long deviceNo) {
        return deviceService.selActiveRangeList(deviceNo);
    }

    @ApiOperation(value = "활동 반경 정보")
    @RequestMapping(value = "/active/{deviceNo}/{rangeNo}", method = RequestMethod.POST)
    public ActiveRangeModel getActiveRange(@PathVariable long deviceNo, @PathVariable long rangeNo) {
        return deviceService.getActiveRange(deviceNo, rangeNo);
    }


    @ApiOperation(value = "현재위치 요청")
    @RequestMapping(value = "/req/{deviceIMEI}", method = RequestMethod.POST)
    public int reqCurrentLoc(@PathVariable String deviceIMEI) {
        return deviceService.reqCurrentLoc(deviceIMEI);
    }

    @ApiOperation(value = "Code List 조회")
    @RequestMapping(value = "/code/list/{grupCd}", method = RequestMethod.POST)
    public List<CodeModel> selCodeList(@PathVariable String grupCd) throws Exception {

        return adminService.selCodeList(grupCd);
    }

    @ApiOperation(value = "Code List 조회")
    @RequestMapping(value = "/notice/get/{deviceNo}", method = RequestMethod.POST)
    public NoticeModel getNotice(@PathVariable long deviceNo) throws Exception {

        return deviceService.getNotice(deviceNo);
    }

    @ApiOperation(value = "알람 해제 및 설정")
    @RequestMapping(value = "/notice/set/{option}", method = RequestMethod.POST)
    public int setNotice(@PathVariable int option, @RequestBody NoticeModel notice) {
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        // option 100 reset
        if(option != 100) {
            if (option == 0) {
                // 다음날 오전 9시까지 해제
                cal1.add(Calendar.DATE, 1); // 일 계산
                cal1.set(Calendar.HOUR, 9);
            } else if (option == 99) {
                // 재 설정까지 무기한
                cal1.add(Calendar.DATE, 1000);
            } else {
                cal1.add(Calendar.HOUR, option);
            }

            Date date = new Date(cal1.getTimeInMillis());

            String nextNotiTime = dateFormat.format(date);
            notice.setNextNotiTime(nextNotiTime);
        }
        return deviceService.setNotice(notice, option);
    }

    @ApiOperation(value = "위치 정보 수신 (단말 -> 서버)")
    @RequestMapping(value = "/loc/ins", method = RequestMethod.POST)
    public int insLocation(@RequestParam String deviceIMEI, @RequestParam String location) throws Exception {
        LocationModel loc = new LocationModel();
        DeviceInfoModel device = deviceService.getDeviceInfo(deviceIMEI);

        if(device != null) {
            loc.setDeviceNo(device.getDeviceNo());
            String[] params = location.split(",");
            if (params != null && params.length == 5) {
                if ("NTY".equals(params[0])) {
                    loc.setLat(Double.parseDouble(params[1]));
                    loc.setLng(Double.parseDouble(params[2]));
                    int typeInfo = Integer.parseInt(params[3]);
                    loc.setStatus(typeInfo);
                    loc.setReportDate(params[4]);

                    int locResult = deviceService.insLocation(loc);
                    if (locResult > 0) {
                        deviceService.watchDanger(loc, device);

                        // 현재 위치 전송 요구 일 경우 처리
                        deviceService.rcvCurrentLoc(device.getDeviceNo());
                    }
                }
            }
        }else{
            return 0;
        }

        return 1;
    }
    @ApiOperation(value = "위치기록 리스트 정보")
    @RequestMapping(value = "/loc/list/{deviceNo}", method = RequestMethod.POST)
    public List<LocationModel> selLocationList(@PathVariable long deviceNo, @RequestParam int days){
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, days*-1); // 일 계산
        Date date = new Date(cal1.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String sDate = dateFormat.format(date).concat("000000");
        String eDate = dateFormat.format(date).concat("235959");

        SearchModel search = new SearchModel();
        search.setDeviceNo(deviceNo);
        search.setStartDt(sDate);
        search.setEndDt(eDate);
        return deviceService.selLocationList(search);
    }

    @ApiOperation(value = "현 위치기록 조회")
    @RequestMapping(value = "/loc/get/{deviceNo}", method = RequestMethod.POST)
    public LocationModel getLocation(@PathVariable long deviceNo){
        SearchModel search = new SearchModel();
        search.setDeviceNo(deviceNo);
        return deviceService.getLocation(search);
    }
}
