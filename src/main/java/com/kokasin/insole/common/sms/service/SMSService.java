package com.kokasin.insole.common.sms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kokasin.insole.common.HttpUtil;
import com.kokasin.insole.common.sms.dao.SmsDao;
import com.kokasin.insole.common.sms.model.SMSModel;
import com.kokasin.insole.common.sms.model.SmsLogModel;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SMSService {
    @Value("${sms.sendUrl}")
    public String smsUrl;

    @Value("${sms.key}")
    public String smsKey;

    @Value("${sms.userId}")
    public String smsId;

    @Value("${sms.sender}")
    public String smsSender;

    final SmsDao smsDao;

    public int sendSms(SMSModel sms, String sendTo) {
        HttpUtil http = new HttpUtil();

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> params = objectMapper.convertValue(sms, Map.class);
        params.put("key", smsKey);
        params.put("user_id", smsId);
        params.put("sender", smsSender);
        params.put("testmode_yn","Y");  // 무료
        try {
            int responseCode = http.post(smsUrl, params);
            int successCnt = 0;
            SmsLogModel log = new SmsLogModel();
            log.setReceiver(sms.getReceiver());
            log.setSendMsg(sms.getMsg());
            log.setSendTo(sendTo);
            log.setResCode(responseCode);

            if(responseCode == 200) {
                String res = http.getResponseBody();

                JSONParser parser = new JSONParser();
                Object result = parser.parse(res);
                JSONObject resultObj = (JSONObject) result;

                int resultCode = Integer.parseInt(resultObj.get("result_code").toString());
                log.setResultCode(resultCode);
                log.setResultMsg(resultObj.get("message").toString());


                if (resultCode == 1) {
                    long msgId = Long.parseLong(resultObj.get("msg_id").toString());
                    successCnt = Integer.parseInt(resultObj.get("success_cnt").toString());
                    int errorCnt = Integer.parseInt(resultObj.get("error_cnt").toString());
                    log.setMsgId(msgId);
                    log.setSuccessCnt(successCnt);
                    log.setErrorCnt(errorCnt);
                }
            }
            smsDao.insSmsLog(log);
            return successCnt;

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
            return 0;
        }
    }



}
