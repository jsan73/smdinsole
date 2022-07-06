package com.kokasin.insole.device.service;

import com.kokasin.insole.device.model.*;

import java.util.List;

public interface DeviceService {
    public List<DeviceInfoModel> selDeviceInfoListByGuard();
    public List<DashboardModel> selDeviceInfoList(long deviceNo);
    public DeviceInfoModel getDeviceInfo(String deviceIMEI);
    public int updDeviceNickName(DeviceInfoModel device);
    public int insActiveRange(ActiveRangeModel range);
    public int delActiveRange(long rangeNo);
    public int updActiveRange(ActiveRangeModel range);
    public List<ActiveRangeModel> selActiveRangeList(long deviceNo);
    public ActiveRangeModel getActiveRange(long deviceNo, long rangeNo);
    public int insLocation(LocationModel loc);
    public void watchDanger(LocationModel loc, DeviceInfoModel deviceInfoModel);
    public List<LocationModel> selLocationList(SearchModel search);

    LocationModel getLocation(SearchModel search);

    public NoticeModel getNotice(long deviceNo);
    public int setNotice(NoticeModel notice, int option);

}
