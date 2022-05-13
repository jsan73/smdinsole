package com.smd21.smdinsole.shoes.dao;

import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;

import java.util.List;

public interface ShoesDao {
    public long insShoesInfo(ShoesInfoModel shoseInfo);
    public List<ShoesInfoModel> selShoesInfoListByGuard(long guardNo);
    public void updShoesInfo(ShoesInfoModel shoseInfo);
}
