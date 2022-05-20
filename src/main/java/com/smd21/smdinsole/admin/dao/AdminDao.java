package com.smd21.smdinsole.admin.dao;

import com.smd21.smdinsole.shoes.model.ShoesInfoModel;

import java.util.List;

public interface AdminDao {
    public long insShoesInfo(ShoesInfoModel shoseInfo);
    public List<ShoesInfoModel> selShoesInfoList(long guardNo);
    public void updShoesInfo(ShoesInfoModel shoseInfo);
}
