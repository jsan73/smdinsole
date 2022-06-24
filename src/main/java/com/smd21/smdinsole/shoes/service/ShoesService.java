package com.smd21.smdinsole.shoes.service;

import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.*;

import java.util.List;
import java.util.Map;

public interface ShoesService {
    public List<ShoesInfoModel> selShoesInfoListByGuard();
    public List<DashboardModel> selShoesInfoList(long shoesNo);
    public ShoesInfoModel getShoesInfo(String shoesNumber);
    public int updShoesNickName(ShoesInfoModel shoes);
    public int insActiveRange(ActiveRangeModel range);
    public int delActiveRange(long rangeNo);
    public int updActiveRange(ActiveRangeModel range);
    public List<ActiveRangeModel> selActiveRangeList(long shoesNo);
    public ActiveRangeModel getActiveRange(long shoesNo, long rangeNo);
    public ShoesInfoModel insLocation(LocationModel loc);
    public void watchDanger(LocationModel loc, ShoesInfoModel shoesInfoModel);
    public List<LocationModel> selLocationList(SearchModel search);

    public NoticeModel getNotice(long shoesNo);
    public int setNotice(NoticeModel notice, int option);

}
