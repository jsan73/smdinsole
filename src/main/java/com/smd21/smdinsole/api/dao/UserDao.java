package com.smd21.smdinsole.api.dao;

import com.smd21.smdinsole.api.model.GuardianModel;
import com.smd21.smdinsole.api.model.ShoesInfoModel;

public interface UserDao {
    public GuardianModel getGuardian(String guardPhone);

    public long insShoesInfo(ShoesInfoModel shoseInfo);
}
