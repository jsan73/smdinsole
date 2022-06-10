package com.smd21.smdinsole.shoes.dao;

import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.*;

import java.util.List;
import java.util.Map;

public interface ShoesDao {
    public List<ShoesInfoModel> selShoesInfoListByGuard(long guardNo);
    public List<DashboardModel> selShoesInfoList(long guardNo);
    public int updShoesBattery(Map<String, Long> shoseMap);
    public int updShoesNickName(Map<String, Object> shoseMap);
    public ShoesInfoModel getShoesInfo(String shoesNumber);
    public int insActiveRange(ActiveRangeModel range);
    public int delActiveRange(Map<String, Long> rangeMap);
    public List<ActiveRangeModel> selActiveRangeList(Map<String, Long> shoseMap);
    public int insLocation(LocationModel loc);
    public List<LocationModel> selLocationList(Map<String, Long> report);
    public int mergeDanger(DangerModel danger);
}
