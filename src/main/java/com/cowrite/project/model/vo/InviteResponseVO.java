package com.cowrite.project.model.vo;

import java.io.Serializable;

public class InviteResponseVO implements Serializable {
    private String inviteCode;

    private String shortUrl;

    private String qrCodeUrl;

    public InviteResponseVO() {
    }

    public InviteResponseVO(String inviteCode, String shortUrl, String qrCodeUrl) {
        this.inviteCode = inviteCode;
        this.shortUrl = shortUrl;
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
