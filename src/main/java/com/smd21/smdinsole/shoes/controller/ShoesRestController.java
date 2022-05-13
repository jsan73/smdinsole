package com.smd21.smdinsole.shoes.controller;

import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;
import com.smd21.smdinsole.shoes.service.ShoesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/${project.name}/api")
@RestController
@RequiredArgsConstructor
@Api(description = "단말기 관리 Controller")
public class ShoesRestController {
    final static Logger logger = LoggerFactory.getLogger(ShoesRestController.class);

    @Autowired
    ShoesService shoesService;

    @ApiOperation(value = "단말정보 입력")
    @RequestMapping(value = "/ins/shoes", method = RequestMethod.POST)
    public ShoesInfoModel insShoesInfo(@RequestBody ShoesInfoModel shoesInfo) throws Exception {

        shoesService.insShoesInfo(shoesInfo);
        return shoesInfo;
    }

    @ApiOperation(value = "단말정보 수정")
    @RequestMapping(value = "/upd/shoes", method = RequestMethod.POST)
    public ShoesInfoModel updShoesInfo(@RequestBody ShoesInfoModel shoesInfo) throws Exception {

        shoesService.updShoesInfo(shoesInfo);
        return shoesInfo;
    }

    @ApiOperation(value = "단말 List 조회")
    @RequestMapping(value = "/sel/shoes/{guardNo}", method = RequestMethod.POST)
    public List<ShoesInfoModel> selShoesInfoListByGuard(@PathVariable long guardNo) throws Exception {

        return shoesService.selShoesInfoListByGuard(guardNo);
    }

}
