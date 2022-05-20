package com.smd21.smdinsole.shoes.dao;

import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.ActiveRangeModel;
import com.smd21.smdinsole.shoes.model.DangerModel;
import com.smd21.smdinsole.shoes.model.LocationModel;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;

import java.util.List;
import java.util.Map;

public interface ShoesDao {
    public List<ShoesInfoModel> selShoesInfoListByGuard(long guardNo);
    public int updShoesBattery(Map<String, Long> shoseMap);
    public ShoesInfoModel getShoesInfo(String shoesNumber);
    public int insActiveRange(ActiveRangeModel range);
    public int delActiveRange(Map<String, Long> rangeMap);
    public List<ActiveRangeModel> selActiveRangeList(Map<String, Long> shoseMap);
    public int insLocation(LocationModel loc);
    public List<LocationModel> selLocationList(Map<String, Long> report);
    public int mergeDanger(DangerModel danger);
}
