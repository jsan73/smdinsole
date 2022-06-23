package com.smd21.smdinsole.shoes.service;

import com.smd21.smdinsole.common.SendSMS;
import com.smd21.smdinsole.common.model.SMSModel;
import com.smd21.smdinsole.shoes.controller.ShoesRestController;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class NoticeService {
    final static Logger logger = LoggerFactory.getLogger(NoticeService.class);

    final
    SendSMS sendSMS;

    public NoticeService(SendSMS sendSMS) {
        this.sendSMS = sendSMS;
    }

    @Async
    public void checkDanger(ShoesInfoModel shoes) {
        try {
            Thread.sleep(5000);
            // 알림 제외 처리 유무 확인

            // 알림 발송
            logger.info("알림 발송");
            // 현재 위치 전송 문자 발송
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public String smsFormat(){
        String header = "YTS";      // 일반정보 Command header
        String sendFr = "0";        // 전송주기(초) 0일경우 해제 : FR
        String url = "www.kokasin.com";            // url : IP:포트
        String smsNumber1 = "01064089967";     // 긴급 SMS 수신 번호 : N1
        String smsNumber2 = "";     // 테스트 전화번호 : N2
        String mode = "0";            // Sleep 시 3G on(1)/off(0) : 3G

        Map<String, String> mapSms = new LinkedHashMap<>();
        mapSms.put("FR", sendFr);
        mapSms.put("IP", url);
        mapSms.put("N1", smsNumber1);
        mapSms.put("N2", smsNumber2);
        mapSms.put("3G", mode);

        Set<String> keys = mapSms.keySet();

        String msg = "";
        for (String key : keys) {
            msg += "," + key + ":" + mapSms.get(key);
        }

        return header + msg;

    }

    public void alram() {
        String msg = smsFormat();
        SMSModel sms = new SMSModel();

        sms.setReceiver("01052490965");
        sms.setMsg(msg);



        sendSMS.sendSms(sms);
    }
}
