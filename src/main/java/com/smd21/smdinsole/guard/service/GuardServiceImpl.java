package com.smd21.smdinsole.guard.service;

import com.smd21.smdinsole.app.exception.AppException;
import com.smd21.smdinsole.app.security.provider.JwtTokenProvider;
import com.smd21.smdinsole.common.RootService;
import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.guard.dao.GuardDao;
import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.controller.ShoesRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuardServiceImpl implements GuardService{
    final static Logger logger = LoggerFactory.getLogger(GuardServiceImpl.class);

    @Autowired
    RootService rootService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    GuardDao guardDao;

    private GuardianModel loadUserByUserNo(String guardPhone, String pwd) throws UsernameNotFoundException {
        Map<String, String> loginInfo = new HashMap<>();

        loginInfo.put("guardPhone", guardPhone);
        loginInfo.put("guardPwd", pwd);
        GuardianModel guardModel = guardDao.getGuardian(loginInfo);
        if(guardModel != null) return guardModel;
        else throw new UsernameNotFoundException(AppException.NO_MATCH_GUARD.getReasonPhrase());
    }

    @Override
    public String getToken(String phoneNumber, String pwd) {

        TokenUserModel tokenUserModel = new TokenUserModel();
        GuardianModel guardianModel = this.loadUserByUserNo(phoneNumber, pwd);

        tokenUserModel.setGuardPhone(guardianModel.getGuardPhone());
        tokenUserModel.setMasterGuardNo(guardianModel.getMasterGuardNo());
        tokenUserModel.setGuardNo(guardianModel.getGuardNo());
        String[] arrRoles = {"ROLE_ACCESS"};
        List<String> roles = Arrays.asList(arrRoles);
        tokenUserModel.setRoles(roles);
        String token = jwtTokenProvider.createToken(tokenUserModel);

        return token;
    }

    @Override
    public long getGuardCheck(String phoneNumber) throws Exception {
        Map<String, String> loginInfo = new HashMap<>();

        loginInfo.put("guardPhone", phoneNumber);
        GuardianModel guardModel = guardDao.getGuardian(loginInfo);
        if(guardModel != null) {
            if(guardModel.getGuardPwd() == null)
                // 첫 로그인 시도
                return guardModel.getGuardNo();
        }else{
            // 등록된 보호자가 아님
            throw new Exception(AppException.NO_GUARD.getReasonPhrase());
        }
        
        // 로그인 진행
        return 0;
    }

    @Override
    public int changePassword(String password, String newPassword) {
        TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, String> loginInfo = new HashMap<>();

        loginInfo.put("guardPhone", user.getGuardPhone());
        loginInfo.put("guardPwd", password);

        GuardianModel guardModel = guardDao.getGuardian(loginInfo);


        if(guardModel != null) {
            guardModel.setGuardPwd(newPassword);
            return guardDao.updGuardPwd(guardModel);
        }else{
            return -1;  // 보호자 계정 없음
        }
    }

    @Override
    @Transactional
    public int insGuardInfo(GuardianModel guardInfo, long shoesNo)  {
        int retValue = guardDao.insGuardian(guardInfo);

//        GuardianModel masterGuard = new GuardianModel();
//        masterGuard.setMasterGuardNo(guardInfo.getGuardNo());
//        masterGuard.setGuardNo(guardInfo.getGuardNo());
//        userDao.updGuardian(masterGuard);
        if(retValue == 1 && guardInfo.getMasterGuardNo() == 0) {
            // 마스터 일 경우 릴레이션 추가
            Map<String, Long> info = new HashMap<String, Long>();
            info.put("shoesNo", shoesNo);
            info.put("masterGuardNo", guardInfo.getGuardNo());
            retValue = guardDao.insShoesGuard(info);

            // 앱 설치 링크 SMS발송
        }
        return retValue;
    }

    @Override
    public int delGuardian(long guardNo) {
        return guardDao.delGuardian(guardNo);
    }

    @Override
    public List<GuardianModel> selGuardianList() {
        long masterGuardNo = rootService.getMasterGusrdNo();

        return guardDao.selGuardianList(masterGuardNo);
    }

    @Override
    public int regGuardian(GuardianModel guardInfo) {
        return guardDao.regGuardian(guardInfo);
    }


}
