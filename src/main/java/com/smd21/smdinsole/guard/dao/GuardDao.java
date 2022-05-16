package com.smd21.smdinsole.guard.dao;

import com.smd21.smdinsole.guard.model.GuardianModel;

import java.util.List;
import java.util.Map;

public interface GuardDao {
    public GuardianModel getGuardian(Map<String, String> loginInfo);
    public void insGuardian(GuardianModel guardInfo);
    public void updGuardian(GuardianModel guardInfo);
    public void insShoesGuard(Map<String, Long> info);
    public List<GuardianModel> selGuardianList(long masterGuardNo);

    public void updGuardPwd(GuardianModel guardInfo);
}
