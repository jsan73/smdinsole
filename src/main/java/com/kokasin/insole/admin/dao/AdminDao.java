package com.kokasin.insole.admin.dao;

import com.kokasin.insole.admin.model.CodeModel;
import com.kokasin.insole.device.model.DeviceInfoModel;

import java.util.List;

public interface AdminDao {
    public long insDeviceInfo(DeviceInfoModel deviceInfo);
    public List<DeviceInfoModel> selDeviceInfoList(long guardNo);
    public void updDeviceInfo(DeviceInfoModel deviceInfo);
    public List<CodeModel> selCodeList(String grupCd);
}
