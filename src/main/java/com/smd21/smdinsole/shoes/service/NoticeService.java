package com.smd21.smdinsole.shoes.service;

import com.smd21.smdinsole.shoes.controller.ShoesRestController;
import com.smd21.smdinsole.shoes.model.ShoesInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    final static Logger logger = LoggerFactory.getLogger(NoticeService.class);

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
}
