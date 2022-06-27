package com.smd21.smdinsole.guard.dao;

import com.smd21.smdinsole.guard.model.GuardianModel;

import java.util.List;
import java.util.Map;

public interface GuardDao {
    public GuardianModel getGuardian(Map<String, String> loginInfo);
    public int insGuardian(GuardianModel guardInfo);
    public int updGuardian(GuardianModel guardInfo);
    public int delGuardian(long guardNo);
    public int insShoesGuard(Map<String, Long> info);
    public List<GuardianModel> selGuardianList(long masterGuardNo);

    public int updGuardPwd(GuardianModel guardInfo);

    public int regGuardian(GuardianModel guardInfo);
}
