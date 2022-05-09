package com.smd21.smdinsole.api.service;

import com.smd21.smdinsole.api.dao.UserDao;
import com.smd21.smdinsole.api.model.ShoesInfoModel;
import com.smd21.smdinsole.app.security.provider.JwtTokenProvider;
import com.smd21.smdinsole.model.TokenUserModel;
import com.smd21.smdinsole.api.model.GuardianModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserDao userDao;

    @Override
    public GuardianModel loadUserByUserNo(String guardPhone) throws UsernameNotFoundException {
        GuardianModel guardModel = new GuardianModel();
        guardModel.setGuardNo(1000);
        guardModel.setGuardPhone("01052490965");
        guardModel.setGuardName("보호자1");
        guardModel.setMasterGuardNo(1000);

        if(guardModel != null) return guardModel;
        else throw new UsernameNotFoundException("FAIL");
    }

    @Override
    public String getToken(String phoneNumber, String pwd) {

        TokenUserModel tokenUserModel = new TokenUserModel();
        GuardianModel guardianModel = this.loadUserByUserNo(phoneNumber);
        tokenUserModel.setUserNo(0);
        tokenUserModel.setGuardPhone(phoneNumber);
        tokenUserModel.setShoseNo(1000);
        tokenUserModel.setMasterGuardNo(guardianModel.getMasterGuardNo());
        tokenUserModel.setGuardNo(guardianModel.getGuardNo());
        String[] arrRoles = {"ROLE_ACCESS"};
        List<String> roles = Arrays.asList(arrRoles);
        tokenUserModel.setRoles(roles);
        String token = jwtTokenProvider.createToken(tokenUserModel);

        return token;
    }

    @Override
    public long insShoesInfo(ShoesInfoModel shoseInfo) {
        return userDao.insShoesInfo(shoseInfo);
    }
}
