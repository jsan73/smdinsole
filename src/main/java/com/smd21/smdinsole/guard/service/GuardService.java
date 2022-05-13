package com.smd21.smdinsole.guard.service;

import com.smd21.smdinsole.guard.model.GuardianModel;

public interface GuardService {
    public GuardianModel loadUserByUserNo(String guardPhone);

    public String getToken(String phoneNumber, String pwd);

    public GuardianModel insGuardInfo(GuardianModel guardInfo, long shoesNo)  throws Exception;
}
