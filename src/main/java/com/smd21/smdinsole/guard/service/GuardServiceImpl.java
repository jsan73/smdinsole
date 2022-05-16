package com.smd21.smdinsole.guard.service;

import com.smd21.smdinsole.app.security.provider.JwtTokenProvider;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuardServiceImpl implements GuardService{
    final static Logger logger = LoggerFactory.getLogger(GuardServiceImpl.class);

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
        else throw new UsernameNotFoundException("FAIL");
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
    public GuardianModel changePassword(String password, String newPassword) {
        try {
            TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            GuardianModel guardianModel = loadUserByUserNo(user.getGuardPhone(), password);
            if(guardianModel != null) {
                guardianModel.setGuadrPwd(newPassword);
                guardDao.updGuardPwd(guardianModel);
            }
            return guardianModel;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public GuardianModel insGuardInfo(GuardianModel guardInfo, long shoesNo) throws Exception {
        guardDao.insGuardian(guardInfo);

//        GuardianModel masterGuard = new GuardianModel();
//        masterGuard.setMasterGuardNo(guardInfo.getGuardNo());
//        masterGuard.setGuardNo(guardInfo.getGuardNo());
//        userDao.updGuardian(masterGuard);
        if(guardInfo.getMasterGuardNo() == 0) {
            Map<String, Long> info = new HashMap<String, Long>();
            info.put("shoesNo", shoesNo);
            info.put("masterGuardNo", guardInfo.getGuardNo());
            guardDao.insShoesGuard(info);
        }
        return guardInfo;
    }

    @Override
    public List<GuardianModel> selGuardianList(long masterGuardNo) {

        return guardDao.selGuardianList(masterGuardNo);
    }


}
