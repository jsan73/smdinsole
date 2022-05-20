package com.smd21.smdinsole.guard.service;

import com.smd21.smdinsole.guard.model.GuardianModel;

import java.util.List;

public interface GuardService {

    public String getToken(String phoneNumber, String pwd);
    public int changePassword(String password, String newPassword);
    public int insGuardInfo(GuardianModel guardInfo, long shoesNo)  throws Exception;
    public List<GuardianModel> selGuardianList();
}
