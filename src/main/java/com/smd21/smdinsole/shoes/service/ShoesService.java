package com.smd21.smdinsole.shoes.service;

import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.ActiveRangeModel;
import com.smd21.smdinsole.shoes.model.DashboardModel;
import com.smd21.smdinsole.shoes.model.LocationModel;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;

import java.util.List;
import java.util.Map;

public interface ShoesService {
    public List<ShoesInfoModel> selShoesInfoListByGuard();
    public List<DashboardModel> selShoesInfoList();
    public int insActiveRange(ActiveRangeModel range);
    public int delActiveRange(long rangeNo);
    public List<ActiveRangeModel> selActiveRangeList(long shoesNo);
    public ShoesInfoModel insLocation(LocationModel loc);
    public void watchDanger(LocationModel loc, ShoesInfoModel shoesInfoModel);
    public List<LocationModel> selLocationList(Map<String, Long> report);

}
