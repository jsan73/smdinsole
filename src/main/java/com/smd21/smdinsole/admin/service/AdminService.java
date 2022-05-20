package com.smd21.smdinsole.admin.service;

import com.smd21.smdinsole.shoes.model.ShoesInfoModel;

import java.util.List;

public interface AdminService {

    public String getToken(String phoneNumber, String pwd);
    public ShoesInfoModel insShoesInfo(ShoesInfoModel shoseInfo);
    public void updShoesInfo(ShoesInfoModel shoseInfo);
    public List<ShoesInfoModel> selShoesInfoListByGuard(long guardNo);


}
