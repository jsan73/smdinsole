package com.kokasin.insole.common;

import com.kokasin.insole.common.model.TokenUserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RootService {
    final static Logger logger = LoggerFactory.getLogger(RootService.class);

    public long getMasterGuardNo() {
        TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        long masterGuardNo = user.getMasterGuardNo();
        if(masterGuardNo == 0) masterGuardNo = user.getGuardNo();

        return masterGuardNo;
    }

    public long getGuardNo() {
        TokenUserModel user = (TokenUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getGuardNo();
    }
}
