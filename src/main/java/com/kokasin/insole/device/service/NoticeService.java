package com.kokasin.insole.device.service;

import com.kokasin.insole.common.RootService;
import com.kokasin.insole.common.sms.service.SMSService;
import com.kokasin.insole.common.sms.model.SMSModel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NoticeService {
    final static Logger logger = LoggerFactory.getLogger(NoticeService.class);

    @Value("${server.url}")
    public String serverUrl;

    final
    SMSService smsService;

    public int sendSmsToGuard(String phoneNumber, String msg) {
        SMSModel sms = new SMSModel();

        sms.setReceiver(phoneNumber);
        sms.setMsg(msg);
        return smsService.sendSms(sms, "GUARD");
    }

    public int sendSmsToDevice(String devicePhone, String msg) {
        SMSModel sms = new SMSModel();

        msg = smsFormat(msg);
        sms.setReceiver(devicePhone);
        sms.setMsg(msg);
        return smsService.sendSms(sms, "DEVICE");
    }


    public String smsFormat(String msg){

        String header = "YTS";      // 일반정보 Command header
        String sendFr = msg;        // 전송주기(초) 0일경우 해제 : FR
        String url = serverUrl;            // url : IP:포트
        String smsNumber1 = "";     // 긴급 SMS 수신 번호 : N1
        String smsNumber2 = "";     // 테스트 전화번호 : N2
        String mode = "1";            // Sleep 시 3G on(1)/off(0) : 3G

        Map<String, String> mapSms = new LinkedHashMap<>();
        mapSms.put("FR", sendFr);
        mapSms.put("IP", url);
        mapSms.put("N1", smsNumber1);
        mapSms.put("N2", smsNumber2);
        mapSms.put("3G", mode);

        Set<String> keys = mapSms.keySet();

        String retMmsg = "";
        for (String key : keys) {
            retMmsg += "," + key + ":" + mapSms.get(key);
        }

        return header + retMmsg;

    }

}
