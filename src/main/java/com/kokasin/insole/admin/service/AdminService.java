package com.kokasin.insole.admin.service;

import com.kokasin.insole.device.model.DeviceInfoModel;
import com.kokasin.insole.admin.model.CodeModel;

import java.util.List;

public interface AdminService {

    public String getToken(String phoneNumber, String pwd);
    public DeviceInfoModel insDeviceInfo(DeviceInfoModel deviceInfo);
    public void updDeviceInfo(DeviceInfoModel deviceInfo);
    public List<DeviceInfoModel> selDeviceInfoListByGuard(long guardNo);
    public List<CodeModel> selCodeList(String grupCd);

}
