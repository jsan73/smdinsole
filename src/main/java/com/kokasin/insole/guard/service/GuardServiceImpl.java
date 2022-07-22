package com.kokasin.insole.guard.service;

import com.kokasin.insole.app.exception.AppException;
import com.kokasin.insole.guard.dao.GuardDao;
import com.kokasin.insole.app.security.provider.JwtTokenProvider;
import com.kokasin.insole.common.RootService;
import com.kokasin.insole.common.model.TokenUserModel;
import com.kokasin.insole.guard.model.GuardLoginModel;
import com.kokasin.insole.guard.model.GuardianModel;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GuardServiceImpl implements GuardService{
    final static Logger logger = LoggerFactory.getLogger(GuardServiceImpl.class);

    final RootService rootService;

    final JwtTokenProvider jwtTokenProvider;

    final GuardDao guardDao;

    private GuardianModel loadUserByUserNo(String guardPhone, String pwd) throws UsernameNotFoundException {
        Map<String, String> loginInfo = new HashMap<>();

        loginInfo.put("guardPhone", guardPhone);
        loginInfo.put("guardPwd", pwd);
        GuardianModel guardModel = guardDao.getGuardian(loginInfo);
        if(guardModel != null) return guardModel;
        else throw new UsernameNotFoundException(AppException.NO_MATCH_GUARD.getReasonPhrase());
    }

    private GuardianModel loadUserByUserToken(String guardPhone, String refreshToken) throws UsernameNotFoundException {
        Map<String, String> loginInfo = new HashMap<>();

        loginInfo.put("guardPhone", guardPhone);
        GuardianModel guardModel = guardDao.getGuardian(loginInfo);
        if(guardModel != null) return guardModel;
        else throw new UsernameNotFoundException(AppException.NO_MATCH_GUARD.getReasonPhrase());
    }

    private String getToken(GuardianModel guard, String[] arrRoles, int expire) {
        TokenUserModel tokenUserModel = new TokenUserModel();

        tokenUserModel.setGuardPhone(guard.getGuardPhone());
        tokenUserModel.setMasterGuardNo(guard.getMasterGuardNo());
        tokenUserModel.setGuardNo(guard.getGuardNo());

        if(arrRoles != null) {
            List<String> roles = Arrays.asList(arrRoles);
            tokenUserModel.setRoles(roles);
        }
        String token = jwtTokenProvider.createToken(tokenUserModel, expire);
        return token;
    }

    @Override
    public Map<String, Object> login(GuardLoginModel guardLogin) {

//        TokenUserModel tokenUserModel = new TokenUserModel();
        GuardianModel guardianModel = this.loadUserByUserNo(guardLogin.getGuardPhone(), guardLogin.getGuardPwd());

        if(guardLogin.getAutoLogin() != guardianModel.getAutoLogin()) {
            guardianModel.setAutoLogin(guardLogin.getAutoLogin());
            guardDao.updGuardAutoLogin(guardianModel);
        }
//        tokenUserModel.setGuardPhone(guardianModel.getGuardPhone());
//        tokenUserModel.setMasterGuardNo(guardianModel.getMasterGuardNo());
//        tokenUserModel.setGuardNo(guardianModel.getGuardNo());
        String[] arrRoles = {"ROLE_ACCESS"};
        String token = getToken(guardianModel, arrRoles, 1);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("token", token);
        retMap.put("refreshToken", guardianModel.getRefreshToken());
        return retMap;
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
    public String getTokenByRefresh(String refreshToken) {
        TokenUserModel tokenUser = jwtTokenProvider.getTokenInfo(refreshToken);
        GuardianModel guardianModel = loadUserByUserToken(tokenUser.getGuardPhone(), refreshToken);

        String[] arrRoles = {"ROLE_ACCESS"};
        String token = getToken(guardianModel, arrRoles, 1);

        return token;
    }

    @Override
    public int updGuardPhone(GuardianModel guard) {
        guard.setGuardNo(rootService.getGuardNo());
        return guardDao.updGuardPhoneInfo(guard);
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
    public int insGuardInfo(GuardianModel guardInfo, long deviceNo)  {
        int retValue = guardDao.insGuardian(guardInfo);

//        GuardianModel masterGuard = new GuardianModel();
//        masterGuard.setMasterGuardNo(guardInfo.getGuardNo());
//        masterGuard.setGuardNo(guardInfo.getGuardNo());
//        userDao.updGuardian(masterGuard);
        if(retValue == 1 && guardInfo.getMasterGuardNo() == 0) {
            // 마스터 일 경우 릴레이션 추가
            Map<String, Long> info = new HashMap<String, Long>();
            info.put("deviceNo", deviceNo);
            info.put("masterGuardNo", guardInfo.getGuardNo());
            retValue = guardDao.insDeviceGuard(info);

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
        long masterGuardNo = rootService.getMasterGuardNo();

        return guardDao.selGuardianList(masterGuardNo);
    }

    @Override
    public int regGuardian(GuardianModel guardInfo) {

        String refreshToken = getToken(guardInfo, null, 365*2);
        guardInfo.setRefreshToken(refreshToken);


        return guardDao.regGuardian(guardInfo);
    }


}
