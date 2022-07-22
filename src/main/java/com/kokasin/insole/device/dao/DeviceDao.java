package com.kokasin.insole.device.dao;

import com.kokasin.insole.device.model.*;

import java.util.List;
import java.util.Map;

public interface DeviceDao {
    public List<DeviceInfoModel> selDeviceInfoListByGuard(long guardNo);
    public List<DashboardModel> selDeviceInfoList(SearchModel search);
//    public int updDeviceBattery(Map<String, Long> deviceMap);
    public int updDeviceNickName(Map<String, Object> deviceMap);
    public DeviceInfoModel getDeviceInfo(String devicIMEI);
    public int insActiveRange(ActiveRangeModel range);
    public int delActiveRange(Map<String, Long> rangeMap);
    public int updActiveRange(ActiveRangeModel range);
    public List<ActiveRangeModel> selActiveRangeList(Map<String, Long> deviceMap);
    public ActiveRangeModel getActiveRange(Map<String, Long> deviceMap);
    public int insLocation(LocationModel loc);
    public List<LocationModel> selLocationList(SearchModel search);
    public LocationModel getLocation(SearchModel search);
    public int mergeDanger(DangerModel danger);
    public DangerModel getDanger(long deviceNo);

    public NoticeModel getNotice(long deviceNo);
    public int insNotice(NoticeModel notice);
    public int updNoticeCancel(NoticeModel notice);

    public int insReqLoc(RequestLocModel rloc);
    public int updReqLoc(RequestLocModel rloc);
    public List<RequestLocModel> selReqLoc(long deviceNo);

}
