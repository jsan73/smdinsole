package com.smd21.smdinsole.api.service;

import com.smd21.smdinsole.api.model.GuardianModel;
import com.smd21.smdinsole.api.model.ShoesInfoModel;

public interface UserService {
    public GuardianModel loadUserByUserNo(String guardPhone);

    public String getToken(String phoneNumber, String pwd);
    public long insShoesInfo(ShoesInfoModel shoseInfo);
}
