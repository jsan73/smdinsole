package com.smd21.smdinsole.admin.service;

import com.smd21.smdinsole.admin.dao.AdminDao;
import com.smd21.smdinsole.admin.model.AdminUserModel;
import com.smd21.smdinsole.admin.model.CodeModel;
import com.smd21.smdinsole.app.security.provider.JwtTokenProvider;
import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.guard.model.GuardianModel;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    final static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AdminDao adminDao;

    @Override
    public String getToken(String loginId, String pwd) {
        TokenUserModel tokenUserModel = new TokenUserModel();
        AdminUserModel adminModel = new AdminUserModel();
        adminModel.setLoginId(loginId);

        tokenUserModel.setLoingId(loginId);
        String[] arrRoles = {"ROLE_ADMIN"};
        List<String> roles = Arrays.asList(arrRoles);
        tokenUserModel.setRoles(roles);
        String token = jwtTokenProvider.createToken(tokenUserModel);

        return token;
    }

    @Override
    public ShoesInfoModel insShoesInfo(ShoesInfoModel shoseInfo) {
        adminDao.insShoesInfo(shoseInfo);
        return shoseInfo;
    }

    @Override
    public void updShoesInfo(ShoesInfoModel shoseInfo) {
        adminDao.updShoesInfo(shoseInfo);
    }

    @Override
    public List<ShoesInfoModel> selShoesInfoListByGuard(long guardNo) {
        return adminDao.selShoesInfoList(guardNo);
    }

    @Override
    public List<CodeModel> selCodeList(String grupCd) {
        return adminDao.selCodeList(grupCd);
    }

}
