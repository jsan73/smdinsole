package com.smd21.smdinsole.common;

import com.smd21.smdinsole.common.model.TokenUserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RootService {
    final static Logger logger = LoggerFactory.getLogger(RootService.class);

    public long getMasterGusrdNo() {
        TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        long masterGuardNo = user.getMasterGuardNo();
        if(masterGuardNo == 0) masterGuardNo = user.getGuardNo();

        return masterGuardNo;
    }

    public long getGusrdNo() {
        TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getGuardNo();
    }
}
