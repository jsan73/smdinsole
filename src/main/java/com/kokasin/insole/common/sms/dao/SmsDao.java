package com.kokasin.insole.common.sms.dao;

import com.kokasin.insole.common.sms.model.SmsLogModel;

public interface SmsDao {
    public int insSmsLog(SmsLogModel log);
}
