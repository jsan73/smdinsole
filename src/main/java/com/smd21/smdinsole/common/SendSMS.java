package com.smd21.smdinsole.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smd21.smdinsole.common.model.SMSModel;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

@Component
public class SendSMS {
    @Value("${sms.sendUrl}")
    public String smsUrl;

    @Value("${sms.key}")
    public String smsKey;

    @Value("${sms.userId}")
    public String smsId;

    @Value("${sms.sender}")
    public String smsSender;

    public void sendSms(SMSModel sms) {
        HttpUtil http = new HttpUtil();

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> params = objectMapper.convertValue(sms, Map.class);
        params.put("key", smsKey);
        params.put("user_id", smsId);
        params.put("sender", smsSender);
        params.put("testmode_yn","Y");  // 무료
        try {
            http.post(smsUrl, params);

            String res = http.getResponseBody();
            System.out.println(res);

//            HTTP/1.1 200 OK
//            Content-Type: application/json;charset=UTF-8
//            {
//                "result_code": 1
//                "message": ""
//                "msg_id": 123456789
//                "success_cnt": 2
//                "error_cnt": 0
//                "msg_type": "SMS"
//            }
//            {
//                "result_code": -101
//                "message": "인증오류입니다."
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
