package com.smd21.smdinsole.shoes.controller;

import com.smd21.smdinsole.admin.model.CodeModel;
import com.smd21.smdinsole.admin.service.AdminService;
import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.*;
import com.smd21.smdinsole.shoes.service.ShoesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/${project.name}/api/shoes")
@RestController
@RequiredArgsConstructor
@Api(description = "단말기 관리 Controller")
public class ShoesRestController {
    final static Logger logger = LoggerFactory.getLogger(ShoesRestController.class);

    final ShoesService shoesService;
    final AdminService adminService;

    @ApiOperation(value = "단말 List 조회")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<ShoesInfoModel> selShoesInfoListByGuard(@ApiIgnore HttpServletRequest request) throws Exception {
        List<ShoesInfoModel> shoesList = shoesService.selShoesInfoListByGuard();

        return shoesList;
    }

    @ApiOperation(value = "단말 정보 조회")
    @RequestMapping(value = "/get/{shoesNumber}", method = RequestMethod.POST)
    public ShoesInfoModel getShoesInfo(@PathVariable String shoesNumber) throws Exception {
        ShoesInfoModel shoesInfo = shoesService.getShoesInfo(shoesNumber);

        return shoesInfo;
    }

    @ApiOperation(value = "단말 조회 List(Dashboard)")
    @RequestMapping(value = "/dashboard/list", method = RequestMethod.POST)
    public List<DashboardModel> selShoesInfoList(@ApiIgnore HttpServletRequest request) throws Exception {
        List<DashboardModel> shoesList = shoesService.selShoesInfoList(0);

        return shoesList;
    }

    @ApiOperation(value = "단말 조회 단일 (Dashboard)")
    @RequestMapping(value = "/dashboard/{shoesNo}", method = RequestMethod.POST)
    public DashboardModel getDashboardInfo(@PathVariable long shoesNo) throws Exception {
        List<DashboardModel> shoesList = shoesService.selShoesInfoList(shoesNo);

        DashboardModel retValue = null;
        if(shoesList.size() > 0) retValue = shoesList.get(0);
        return retValue;
    }

    @ApiOperation(value = "단말 닉네임 변경")
    @RequestMapping(value = "/nickname/upd", method = RequestMethod.POST)
    public int insActiveRange(@RequestBody ShoesInfoModel shoes) throws Exception {
        int insResult = shoesService.updShoesNickName(shoes);
        return insResult;
    }

    @ApiOperation(value = "활동 반경 추가")
    @RequestMapping(value = "/active/ins", method = RequestMethod.POST)
    public int insActiveRange(@RequestBody ActiveRangeModel activeRange) throws Exception {
        int insResult = shoesService.insActiveRange(activeRange);
        return insResult;
    }

    @ApiOperation(value = "활동 반경 삭제")
    @RequestMapping(value = "/active/del/{rangeNo}", method = RequestMethod.POST)
    public int insActiveRange(@PathVariable long rangeNo) throws Exception {
        return shoesService.delActiveRange(rangeNo);
    }

    @ApiOperation(value = "활동 반경 수정")
    @RequestMapping(value = "/active/upd", method = RequestMethod.POST)
    public int updActiveRange(@RequestBody ActiveRangeModel activeRange) throws Exception {
        return shoesService.updActiveRange(activeRange);
    }

    @ApiOperation(value = "활동 반경 리스트")
    @RequestMapping(value = "/active/list/{shoesNo}", method = RequestMethod.POST)
    public List<ActiveRangeModel> selActiveRangeList(@PathVariable long shoesNo) {
        return shoesService.selActiveRangeList(shoesNo);
    }

    @ApiOperation(value = "활동 반경 정보")
    @RequestMapping(value = "/active/{shoesNo}/{rangeNo}", method = RequestMethod.POST)
    public ActiveRangeModel getActiveRange(@PathVariable long shoesNo, @PathVariable long rangeNo) {
        return shoesService.getActiveRange(shoesNo, rangeNo);
    }


    @ApiOperation(value = "현재위치 요청")
    @RequestMapping(value = "/req/{shoesNo}", method = RequestMethod.POST)
    public int reqCurrentLocation(@PathVariable long shoesNo) {
        
        // 기기에 SMS 발송
        // 현재 위치 수신 시 Push 전송
        return 1;
    }

    @ApiOperation(value = "Code List 조회")
    @RequestMapping(value = "/code/list/{grupCd}", method = RequestMethod.POST)
    public List<CodeModel> selCodeList(@PathVariable String grupCd) throws Exception {

        return adminService.selCodeList(grupCd);
    }

    @ApiOperation(value = "Code List 조회")
    @RequestMapping(value = "/notice/get/{shoesNo}", method = RequestMethod.POST)
    public NoticeModel getNotice(@PathVariable long shoesNo) throws Exception {

        return shoesService.getNotice(shoesNo);
    }

    @ApiOperation(value = "알람 해제 및 설정")
    @RequestMapping(value = "/notice/set/{option}", method = RequestMethod.POST)
    public int setNotice(@PathVariable int option, @RequestBody NoticeModel notice) {
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
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
        return shoesService.setNotice(notice, option);
    }

    @ApiOperation(value = "위치 정보 수신")
    @RequestMapping(value = "/loc/ins", method = RequestMethod.POST)
    public int insLocation(@RequestParam String shoesId, @RequestParam String location) throws Exception {
        LocationModel loc = new LocationModel();

        loc.setShoesNumber(shoesId);
        String[] params = location.split(",");
        if(params != null && params.length == 5) {
            if("NTY".equals(params[0])) {
                loc.setLatitude(Double.parseDouble(params[1]));
                loc.setLongitude(Double.parseDouble(params[2]));
                int typeInfo = Integer.parseInt(params[3]);
                loc.setStatus(typeInfo);
                loc.setReportDate(params[4]);

                ShoesInfoModel shoesInfoModel = shoesService.insLocation(loc);
                if(shoesInfoModel == null) throw new Exception();

                shoesService.watchDanger(loc, shoesInfoModel);
            }
        }

        return 1;
    }
    @ApiOperation(value = "위치기록 정보")
    @RequestMapping(value = "/history/{shoesNo}", method = RequestMethod.POST)
    public List<LocationModel> selLocationList(@PathVariable long shoesNo, @RequestParam int days){
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, days*-1); // 일 계산
        Date date = new Date(cal1.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String sDate = dateFormat.format(date).concat("000000");
        String eDate = dateFormat.format(date).concat("235959");

        SearchModel search = new SearchModel();
        search.setShoesNo(shoesNo);
        search.setStartDt(sDate);
        search.setEndDt(eDate);
        return shoesService.selLocationList(search);
    }
}
