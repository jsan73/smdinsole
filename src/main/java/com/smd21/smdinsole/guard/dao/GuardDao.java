package com.smd21.smdinsole.guard.dao;

import com.smd21.smdinsole.guard.model.GuardianModel;

import java.util.Map;

public interface GuardDao {
    public void insGuardian(GuardianModel guardInfo);
    public void updGuardian(GuardianModel guardInfo);
    public void insShoesGuard(Map<String, Long> info);
}
