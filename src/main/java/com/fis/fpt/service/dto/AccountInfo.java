package com.fis.fpt.service.dto;

import lombok.Data;

@Data
public class AccountInfo {
    public String email;
    public String fullName;
    public String avatar = null;
    public String orgIn;

    public AccountInfo(String email, String fullName, String avatar, String orgIn) {
        this.email = email;
        this.fullName = fullName;
        this.avatar = avatar;
        this.orgIn = orgIn;
    }

    public AccountInfo() {
    }
}
