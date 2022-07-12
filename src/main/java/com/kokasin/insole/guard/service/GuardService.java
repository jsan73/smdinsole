package com.kokasin.insole.guard.service;

import com.kokasin.insole.guard.model.GuardLoginModel;
import com.kokasin.insole.guard.model.GuardianModel;

import java.util.List;
import java.util.Map;

public interface GuardService {

    public Map<String, Object> login(GuardLoginModel guard);
    public long getGuardCheck(String phoneNumber)  throws Exception ;

    public String getTokenByRefresh(String refreshToken);

    public int updGuardPhone(GuardianModel guard);
    public int changePassword(String password, String newPassword);
    public int insGuardInfo(GuardianModel guardInfo, long deviceNo);
    public int delGuardian(long guardNo);
    public List<GuardianModel> selGuardianList();
    public int regGuardian (GuardianModel guardInfo);
}
