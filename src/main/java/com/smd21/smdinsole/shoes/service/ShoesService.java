package com.smd21.smdinsole.shoes.service;

import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;

import java.util.List;

public interface ShoesService {

    public ShoesInfoModel insShoesInfo(ShoesInfoModel shoseInfo);
    public void updShoesInfo(ShoesInfoModel shoseInfo);
    public List<ShoesInfoModel> selShoesInfoListByGuard(long guardNo);


}
