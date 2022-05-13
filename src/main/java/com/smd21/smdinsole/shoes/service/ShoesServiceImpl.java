package com.smd21.smdinsole.shoes.service;

import com.smd21.smdinsole.guard.service.GuardServiceImpl;
import com.smd21.smdinsole.shoes.dao.ShoesDao;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;
import com.smd21.smdinsole.app.security.provider.JwtTokenProvider;
import com.smd21.smdinsole.common.model.TokenUserModel;
import com.smd21.smdinsole.guard.model.GuardianModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoesServiceImpl implements ShoesService {
    final static Logger logger = LoggerFactory.getLogger(ShoesServiceImpl.class);

    @Autowired
    ShoesDao shoesDao;



    @Override
    public ShoesInfoModel insShoesInfo(ShoesInfoModel shoseInfo) {
        shoesDao.insShoesInfo(shoseInfo);
        return shoseInfo;
    }

    @Override
    public void updShoesInfo(ShoesInfoModel shoseInfo) {
        shoesDao.updShoesInfo(shoseInfo);
    }

    @Override
    public List<ShoesInfoModel> selShoesInfoListByGuard(long guardNo) {
        return shoesDao.selShoesInfoListByGuard(guardNo);
    }

}
