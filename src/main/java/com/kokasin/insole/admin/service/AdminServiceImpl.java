package com.kokasin.insole.admin.service;

import com.kokasin.insole.device.model.DeviceInfoModel;
import com.kokasin.insole.admin.dao.AdminDao;
import com.kokasin.insole.admin.model.AdminUserModel;
import com.kokasin.insole.admin.model.CodeModel;
import com.kokasin.insole.app.security.provider.JwtTokenProvider;
import com.kokasin.insole.common.model.TokenUserModel;
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
        String token = jwtTokenProvider.createToken(tokenUserModel,1);

        return token;
    }

    @Override
    public DeviceInfoModel insDeviceInfo(DeviceInfoModel deviceInfo) {
        adminDao.insDeviceInfo(deviceInfo);
        return deviceInfo;
    }

    @Override
    public void updDeviceInfo(DeviceInfoModel deviceInfo) {
        adminDao.updDeviceInfo(deviceInfo);
    }

    @Override
    public List<DeviceInfoModel> selDeviceInfoListByGuard(long guardNo) {
        return adminDao.selDeviceInfoList(guardNo);
    }

    @Override
    public List<CodeModel> selCodeList(String grupCd) {
        return adminDao.selCodeList(grupCd);
    }

}
