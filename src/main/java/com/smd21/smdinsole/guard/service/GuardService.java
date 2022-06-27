package com.smd21.smdinsole.guard.service;

import com.smd21.smdinsole.guard.model.GuardianModel;

import java.util.List;

public interface GuardService {

    public String getToken(String phoneNumber, String pwd);
    public long getGuardCheck(String phoneNumber)  throws Exception ;
    public int changePassword(String password, String newPassword);
    public int insGuardInfo(GuardianModel guardInfo, long shoesNo);
    public int delGuardian(long guardNo);
    public List<GuardianModel> selGuardianList();
    public int regGuardian (GuardianModel guardInfo);
}
