package com.smd21.smdinsole.shoes.controller;

import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.ActiveRangeModel;
import com.smd21.smdinsole.shoes.model.LocationModel;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;
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

@RequestMapping("/${project.name}/api/shoes")
@RestController
@RequiredArgsConstructor
@Api(description = "단말기 관리 Controller")
public class ShoesRestController {
    final static Logger logger = LoggerFactory.getLogger(ShoesRestController.class);

    final ShoesService shoesService;

    @ApiOperation(value = "단말 List 조회")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<ShoesInfoModel> selShoesInfoListByGuard(@ApiIgnore HttpServletRequest request) throws Exception {
        List<ShoesInfoModel> shoesList = shoesService.selShoesInfoListByGuard();

        return shoesList;
    }

    @ApiOperation(value = "활동 반경 추가")
    @RequestMapping(value = "/active/ins", method = RequestMethod.POST)
    public ActiveRangeModel insActiveRange(@RequestBody ActiveRangeModel activeRange) throws Exception {
        int insResult = shoesService.insActiveRange(activeRange);
        if(insResult == 0) return null;

        return activeRange;
    }

    @ApiOperation(value = "활동 반경 삭제")
    @RequestMapping(value = "/active/del", method = RequestMethod.POST)
    public int insActiveRange(@RequestParam long rangeNo) throws Exception {
        return shoesService.delActiveRange(rangeNo);
    }

    @ApiOperation(value = "활동 반경 리스트")
    @RequestMapping(value = "/active/list/{shoesNo}", method = RequestMethod.POST)
    public List<ActiveRangeModel> selActiveRangeList(@PathVariable long shoesNo) {
        return shoesService.selActiveRangeList(shoesNo);
    }

    @ApiOperation(value = "위치 정보 입력")
    @RequestMapping(value = "/loc/ins", method = RequestMethod.POST)
    public int insLocation(@RequestBody LocationModel loc) throws Exception {
        ShoesInfoModel shoesInfoModel = shoesService.insLocation(loc);
        if(shoesInfoModel == null) throw new Exception();

        shoesService.watchDanger(loc, shoesInfoModel);

        return 1;
    }

    @ApiOperation(value = "위치 정보 수신")
    @RequestMapping(value = "/loc/ins/{shoesId}", method = RequestMethod.POST)
    public int insLocation(@PathVariable String shoesId, @RequestParam String location) throws Exception {
        LocationModel loc = new LocationModel();

        loc.setShoesNumber(shoesId);
        String[] params = location.split(",");

        ShoesInfoModel shoesInfoModel = shoesService.insLocation(loc);
        if(shoesInfoModel == null) throw new Exception();

        shoesService.watchDanger(loc, shoesInfoModel);

        return 1;
    }
}
