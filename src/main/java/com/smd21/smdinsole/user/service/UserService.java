package com.smd21.smdinsole.user.service;

import com.smd21.smdinsole.user.model.GuardianModel;

public interface UserService {
    public GuardianModel loadUserByUserNo(String guardPhone);

    public String getToken(String phoneNumber, String pwd);
}
