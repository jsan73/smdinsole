package com.kokasin.insole.device.service;

import com.kokasin.insole.common.FCMService;
import com.kokasin.insole.common.sms.service.SMSService;
import com.kokasin.insole.common.sms.model.SMSModel;
import com.kokasin.insole.common.model.TokenUserModel;
import com.kokasin.insole.device.dao.DeviceDao;
import com.kokasin.insole.device.model.*;
import com.kokasin.insole.common.CommonUtil;
import com.kokasin.insole.common.RootService;
import com.kokasin.insole.guard.dao.GuardDao;
import com.kokasin.insole.guard.model.GuardianModel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    final static Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    final RootService rootService;

    final DeviceDao deviceDao;

    final GuardDao guardDao;

    final NoticeService noticeService;
    final FCMService fcmService;

    final
    SMSService smsService;

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
    public void watchDanger(LocationModel loc, DeviceInfoModel device) {
        long deviceNo = device.getDeviceNo();
        long guardNo = device.getGuardNo();

        Map<String, Long> deviceMap = new HashMap<>();
        deviceMap.put("deviceNo", deviceNo);
        deviceMap.put("guardNo", guardNo);

        double lat2 = loc.getLat();
        double lon2 = loc.getLng();

        DangerModel danger = new DangerModel();
        danger.setDeviceNo(deviceNo);
        danger.setLocNo(loc.getLocationNo());
        danger.setLat(lat2);
        danger.setLng(lon2);
        danger.setDangerYn("Y");

        DangerModel preDanger = deviceDao.getDanger(deviceNo);
        String preDangerYn = "N";
        if(preDanger != null) preDangerYn = preDanger.getDangerYn();


        NoticeModel notice = this.getNotice(device.getDeviceNo());
        if(notice == null) {
            // 알림해제를 하지 않았을 경우
            // 안심좀 리스트 조회
            List<ActiveRangeModel> rangeList = deviceDao.selActiveRangeList(deviceMap);
            if (rangeList.size() > 0) {
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

                if ("Y".equals(danger.getDangerYn())) {
                    // master guardno로 전체 보호자 검색
                    List<GuardianModel> guardList = guardDao.selGuardianList(device.getGuardNo());
                    if(guardList != null) {
                        List<String> receiver = new ArrayList<>();
                        for(GuardianModel guard: guardList)
                            receiver.add(guard.getGuardPhone());

                        noticeService.sendSmsToGuard(String.join(",",receiver), "안심존 이탈");
                    }

                    // 10분주기 전송으로 요청 (최초)
                    if("N".equals(preDangerYn))
                        noticeService.sendSmsToDevice(device.getDeviceNumber(), "600");
                }else{
                    // 안심존 내에 위치 할 경우
                    if("Y".equals(preDangerYn))
                        // 전 위치가 안심존 이탈 시 다시 원래로 복귀
                        noticeService.sendSmsToDevice(device.getDeviceNumber(), "3600");
                }
            }
        }else{
            // 알림 해제 설정을 했을경우
            danger.setDangerYn("N");
            if("Y".equals(preDangerYn))
                // 전 위치가 안심존 이탈 시 다시 원래로 복귀
                noticeService.sendSmsToDevice(device.getDeviceNumber(), "3600");
        }
        // 위험 정보 저장
        deviceDao.mergeDanger(danger);

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
        }

        if(option != 100) {
            // 알림 해제 설정
            long guardNo = rootService.getGuardNo();
            notice.setGuardNo(guardNo);
            deviceDao.insNotice(notice);

        }
        // noticeService.alram();

        return 1;
    }

    @Override
    public int reqCurrentLoc(String deviceIMEI) {
        // 현 위치 정보 요청

        DeviceInfoModel device = deviceDao.getDeviceInfo(deviceIMEI);
        TokenUserModel guard = rootService.getGuardInfo();

        // 요청내용 insert
        RequestLocModel rloc = new RequestLocModel();
        rloc.setDeviceNo(device.getDeviceNo());
        rloc.setReqGuardNo(guard.getGuardNo());
        deviceDao.insReqLoc(rloc);


        SMSModel sms = new SMSModel();

        sms.setReceiver(device.getDeviceNumber());
        sms.setMsg("어디?");
        return smsService.sendSms(sms, "DEVICE");
    }

    public int rcvCurrentLoc(long deviceNo) {
        // 현 위치 정보 요청에 대한 응답 수신
        List<RequestLocModel> reqList = deviceDao.selReqLoc(deviceNo);
        if(reqList != null) {
            for(RequestLocModel request : reqList) {
                // TODO : App push request.guardNo
                GuardianModel guard = guardDao.getGuardPush(request.getReqGuardNo());
                if(guard != null && !"".equals(guard.getPushToken()))
                    try {
                        fcmService.sendMessageTo(guard.getPushToken(), "위치정보수신", "요청하신 위치정보가 수신되었습니다");
                    }catch(Exception e){
                        logger.error(e.getMessage());
                    }
                deviceDao.updReqLoc(request);
            }
            return 1;
        }
        return 0;
    }

}
