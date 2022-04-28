package com.smd21.smdinsole.user.model;

import lombok.Data;

@Data
public class GuardianModel {
    private long guardNo;
    private String guardName;
    private String guardPhone;
    private long masterGuardNo;
    private String guadrPwd;
    private char maketigAgreeYN;
    private String regDate;
}
