package com.kokasin.insole.device.service;

import com.kokasin.insole.device.dao.DeviceDao;
import com.kokasin.insole.device.model.*;
import com.kokasin.insole.common.CommonUtil;
import com.kokasin.insole.common.RootService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DeviceServiceImpl implements DeviceService {
    final static Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    RootService rootService;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    private NoticeService noticeService;

    @Override
    public List<DeviceInfoModel> selDeviceInfoListByGuard() {

        long masterGuardNo = rootService.getMasterGuardNo();
        return deviceDao.selDeviceInfoListByGuard(masterGuardNo);
    }

    @Override
    public List<DashboardModel> selDeviceInfoList(long deviceNo) {
        long masterGuardNo = rootService.getMasterGuardNo();
        SearchModel search = new SearchModel();
        search.setGuardNo(masterGuardNo);
        if(deviceNo != 0) search.setDeviceNo(deviceNo);
        return deviceDao.selDeviceInfoList(search);
    }

    @Override
    public DeviceInfoModel getDeviceInfo(String deviceIMEI) {
        return deviceDao.getDeviceInfo(deviceIMEI);

    }


    @Override
    public int updDeviceNickName(DeviceInfoModel device) {
        Map<String, Object> deviceMap = new HashMap<>();
        deviceMap.put("deviceNo", device.getDeviceNo());
        deviceMap.put("nickName", device.getNickName());

        return deviceDao.updDeviceNickName(deviceMap);
    }

    @Override
    public int insActiveRange(ActiveRangeModel range) {
        range.setGuardNo(rootService.getMasterGuardNo());
        range.setRegUser(rootService.getGuardNo());
        return deviceDao.insActiveRange(range);

        //return range;
    }

    @Override
    public int delActiveRange(long rangeNo) {
        long masterGuardNo = rootService.getMasterGuardNo();

        Map<String, Long> rangeMap = new HashMap<>();
        rangeMap.put("rangeNo", rangeNo);
        rangeMap.put("guardNo", masterGuardNo);
        return deviceDao.delActiveRange(rangeMap);
    }

    @Override
    public int updActiveRange(ActiveRangeModel rangeInfo) {
        rangeInfo.setRegUser(rootService.getGuardNo());
        return deviceDao.updActiveRange(rangeInfo);
    }

    @Override
    public List<ActiveRangeModel> selActiveRangeList(long deviceNo) {
        long masterGuardNo = rootService.getMasterGuardNo();

        Map<String, Long> deviceMap = new HashMap<>();
        deviceMap.put("deviceNo", deviceNo);
        deviceMap.put("guardNo", masterGuardNo);

        List<ActiveRangeModel> rangeList = deviceDao.selActiveRangeList(deviceMap);

        return rangeList;
    }

    @Override
    public ActiveRangeModel getActiveRange(long deviceNo, long rangeNo) {
        long masterGuardNo = rootService.getMasterGuardNo();

        Map<String, Long> deviceMap = new HashMap<>();
        deviceMap.put("deviceNo", deviceNo);
        deviceMap.put("guardNo", masterGuardNo);
        deviceMap.put("rangeNo", rangeNo);

        ActiveRangeModel range = deviceDao.getActiveRange(deviceMap);

        return range;
    }

    @Override
    public int insLocation(LocationModel loc) {
        // 배터리 정보
        String[] cb = CommonUtil.getBinary(loc.getStatus());
        int battery = 0;
        if("1".equals(cb[1])) battery = 4;      // 충전중
        else if("1".equals(cb[2])) battery = 5;      // 충전 완료
        else {
            battery = Integer.parseInt(cb[0].concat(cb[3]), 2);
        }

        loc.setBattery(battery);

        // 위치 측위 정보 (GPS:4, CELL:5, SAVE-WIFI:6, 없음:7)
        for(int i=4; i<=7; i++) {
            if("1".equals(cb[i])) {
                loc.setStatus(i);
                break;
            }
        }
        return deviceDao.insLocation(loc);
    }

    @Override
    @Transactional
    public void watchDanger(LocationModel loc, DeviceInfoModel deviceInfoModel) {
        long deviceNo = deviceInfoModel.getDeviceNo();
        long guardNo = deviceInfoModel.getGuardNo();
        Map<String, Long> deviceMap = new HashMap<>();
        deviceMap.put("deviceNo", deviceNo);
        deviceMap.put("guardNo", guardNo);

        // 활동 반경 리스트 조회
        List<ActiveRangeModel> rangeList = deviceDao.selActiveRangeList(deviceMap);
        if(rangeList.size() > 0) {

            double lat2 = loc.getLat();
            double lon2 = loc.getLng();

            DangerModel danger = new DangerModel();
            danger.setDeviceNo(deviceNo);
            danger.setLocNo(loc.getLocationNo());
            danger.setLat(lat2);
            danger.setLng(lon2);
            danger.setDangerYn("Y");

            for (ActiveRangeModel range : rangeList) {
                // 활동 반경 이내인지 여부 조회
                double lat1 = range.getLat();
                double lon1 = range.getLng();
                double radius = range.getRadius();

                double distance = CommonUtil.distance(lat1, lon1, lat2, lon2, "meter");
                if (distance < radius) {
                    // 활동 반경 내에 위치
                    danger.setDangerYn("N");
                    break;
                }
            }
            // 위험 정보 저장
            deviceDao.mergeDanger(danger);
            if("Y".equals(danger.getDangerYn())) {
                // 활동 반경 내에 위치 하지 않았을 경우 알림 발송 (알림 제외 처리 되지 않은 경우)
                // 위치 전송주기 변경 요청 문자 발송
                noticeService.checkDanger(deviceInfoModel);
            }

        }

    }


    @Override
    public List<LocationModel> selLocationList(SearchModel search) {
        long masterGuardNo = rootService.getMasterGuardNo();
        search.setGuardNo(masterGuardNo);

        return deviceDao.selLocationList(search);
    }

    @Override
    public LocationModel getLocation(SearchModel search) {
        long masterGuardNo = rootService.getMasterGuardNo();
        search.setGuardNo(masterGuardNo);

        return deviceDao.getLocation(search);
    }

    @Override
    public NoticeModel getNotice(long deviceNo){
        return deviceDao.getNotice(deviceNo);
    }

    @Override
    @Transactional
    public int setNotice(NoticeModel notice, int option) {
        NoticeModel c_notice = deviceDao.getNotice(notice.getDeviceNo());

        if(c_notice != null) {
            // 기존 알림 해제 설정이 되어 있는 상태 - 사용 안함으로 설정 후 재 설정
            deviceDao.updNoticeCancel(c_notice);

            // 예약 문자 확인 후 취소
        }

        if(option == 100) {
            // 알림 설정 디폴트로 전환 문자 발송

        }else{
            // 알림 해제 설정
            long guardNo = rootService.getGuardNo();
            notice.setGuardNo(guardNo);
            deviceDao.insNotice(notice);

            // 알림 해제 문자 발송 및 알림 예약 문자 발송

        }
        noticeService.alram();

        return 1;
    }

}
