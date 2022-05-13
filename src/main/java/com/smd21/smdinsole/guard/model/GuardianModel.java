package com.smd21.smdinsole.guard.model;

import lombok.Data;

@Data
public class GuardianModel {
    private long guardNo;
    private String guardName;
    private String guardPhone;
    private long masterGuardNo;
    private String guadrPwd;
    private char maketigAgreeYn;
    private String regDate;
}
