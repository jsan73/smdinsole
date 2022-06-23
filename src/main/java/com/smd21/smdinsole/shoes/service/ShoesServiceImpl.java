package com.smd21.smdinsole.shoes.service;

import com.smd21.smdinsole.common.CommonUtil;
import com.smd21.smdinsole.common.RootService;
import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.shoes.dao.ShoesDao;
import com.smd21.smdinsole.shoes.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ShoesServiceImpl implements ShoesService {
    final static Logger logger = LoggerFactory.getLogger(ShoesServiceImpl.class);

    @Autowired
    RootService rootService;

    @Autowired
    ShoesDao shoesDao;

    @Autowired
    private NoticeService noticeService;

    @Override
    public List<ShoesInfoModel> selShoesInfoListByGuard() {

        long masterGuardNo = rootService.getMasterGusrdNo();
        return shoesDao.selShoesInfoListByGuard(masterGuardNo);
    }

    @Override
    public List<DashboardModel> selShoesInfoList(long shoesNo) {
        long masterGuardNo = rootService.getMasterGusrdNo();
        SearchModel search = new SearchModel();
        search.setGuardNo(masterGuardNo);
        if(shoesNo != 0) search.setShoesNo(shoesNo);
        return shoesDao.selShoesInfoList(search);
    }

    @Override
    public ShoesInfoModel getShoesInfo(String shoesNumber) {
        return shoesDao.getShoesInfo(shoesNumber);

    }


    @Override
    public int updShoesNickName(ShoesInfoModel shoes) {
        Map<String, Object> shoesMap = new HashMap<>();
        shoesMap.put("shoesNo", shoes.getShoesNo());
        shoesMap.put("nickName", shoes.getNickName());

        return shoesDao.updShoesNickName(shoesMap);
    }

    @Override
    public int insActiveRange(ActiveRangeModel range) {
        range.setGuardNo(rootService.getMasterGusrdNo());
        range.setRegUser(rootService.getGusrdNo());
        return shoesDao.insActiveRange(range);

        //return range;
    }

    @Override
    public int delActiveRange(long rangeNo) {
        long masterGuardNo = rootService.getMasterGusrdNo();

        Map<String, Long> rangeMap = new HashMap<>();
        rangeMap.put("rangeNo", rangeNo);
        rangeMap.put("guardNo", masterGuardNo);
        return shoesDao.delActiveRange(rangeMap);
    }

    @Override
    public int updActiveRange(ActiveRangeModel rangeInfo) {
        rangeInfo.setRegUser(rootService.getGusrdNo());
        return shoesDao.updActiveRange(rangeInfo);
    }

    @Override
    public List<ActiveRangeModel> selActiveRangeList(long shoesNo) {
        long masterGuardNo = rootService.getMasterGusrdNo();

        Map<String, Long> shoesMap = new HashMap<>();
        shoesMap.put("shoesNo", shoesNo);
        shoesMap.put("guardNo", masterGuardNo);

        List<ActiveRangeModel> rangeList = shoesDao.selActiveRangeList(shoesMap);

        return rangeList;
    }

    @Override
    public ActiveRangeModel getActiveRange(long shoesNo, long rangeNo) {
        long masterGuardNo = rootService.getMasterGusrdNo();

        Map<String, Long> shoesMap = new HashMap<>();
        shoesMap.put("shoesNo", shoesNo);
        shoesMap.put("guardNo", masterGuardNo);
        shoesMap.put("rangeNo", rangeNo);

        ActiveRangeModel range = shoesDao.getActiveRange(shoesMap);

        return range;
    }

    @Override
    public ShoesInfoModel insLocation(LocationModel loc) {
        // 현재 위치 수신 단말 번호로 shoes 조회
        ShoesInfoModel shoesInfoModel = shoesDao.getShoesInfo(loc.getShoesNumber());
        if(shoesInfoModel != null) {
            loc.setShoesNo(shoesInfoModel.getShoesNo());
            int retVal = shoesDao.insLocation(loc);
            if(retVal == 1) return shoesInfoModel;
        }
        return null;
    }

    @Override
    @Transactional
    public void watchDanger(LocationModel loc, ShoesInfoModel shoesInfoModel) {
        long shoesNo = shoesInfoModel.getShoesNo();
        long guardNo = shoesInfoModel.getGuardNo();
        Map<String, Long> shoesMap = new HashMap<>();
        shoesMap.put("shoesNo", shoesNo);
        shoesMap.put("guardNo", guardNo);

        // 배터리 정보 UPDATE
        String[] cb = CommonUtil.getBinary(loc.getStatus());
        int battery = Integer.parseInt(cb[0].concat(cb[3]), 2) ;

        shoesMap.put("battery", (long)battery);
        shoesDao.updShoesBattery(shoesMap);

        // 활동 반경 리스트 조회
        List<ActiveRangeModel> rangeList = shoesDao.selActiveRangeList(shoesMap);
        if(rangeList.size() > 0) {

            double lat2 = loc.getLatitude();
            double lon2 = loc.getLongitude();

            DangerModel danger = new DangerModel();
            danger.setShoesNo(shoesNo);
            danger.setLocNo(loc.getLocationNo());
            danger.setLat(lat2);
            danger.setLon(lon2);
            danger.setDangerYn("Y");

            for (ActiveRangeModel range : rangeList) {
                // 활동 반경 이내인지 여부 조회
                double lat1 = range.getLatitude();
                double lon1 = range.getLongitude();
                double radius = range.getRadius();

                double distance = CommonUtil.distance(lat1, lon1, lat2, lon2, "meter");
                if (distance < radius) {
                    // 활동 반경 내에 위치
                    danger.setDangerYn("N");
                    break;
                }
            }
            // 위험 정보 저장
            shoesDao.mergeDanger(danger);
            if("Y".equals(danger.getDangerYn())) {
                // 활동 반경 내에 위치 하지 않았을 경우 알림 발송 (알림 제외 처리 되지 않은 경우)
                // 위치 전송주기 변경 요청 문자 발송
                noticeService.checkDanger(shoesInfoModel);
            }

        }

    }


    @Override
    public List<LocationModel> selLocationList(SearchModel search) {
        long masterGuardNo = rootService.getMasterGusrdNo();
        search.setGuardNo(masterGuardNo);

        return shoesDao.selLocationList(search);
    }

    @Override
    @Transactional
    public int setNotice(NoticeModel notice, int option) {
        NoticeModel c_notice = shoesDao.getNotice(notice.getShoesNo());
        if(c_notice != null) {
            // 기존 알림 해제 설정이 되어 있는 상태 - 사용 안함으로 설정 후 재 설정
            shoesDao.updNoticeCance(c_notice);

            // 예약 문자 확인 후 취소
        }

        if(option == 100) {
            // 알림 설정 디폴트로 전환 문자 발송

        }else{
            // 알람 해제 설정
            shoesDao.insNotice(notice);

            // 알림 해제 문자 발송 및 알림 예약 문자 발송

        }
        noticeService.alram();

        return 1;
    }

}
