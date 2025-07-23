package com.cowrite.project.model.vo;


import com.cowrite.project.model.entity.User;

public class UserVO{

    private String username;

    private String email;

    private String avatarUrl;

    private String status;

    private Boolean themeDark;

    private Boolean emailNotifications;

    private String language;

    private String bio;

    public UserVO() {
    }

    public UserVO(String username, String email, String avatarUrl, String status) {
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.status = status;
    }

    public UserVO(String username, String email, String avatarUrl, String status, Boolean themeDark, Boolean emailNotifications, String language, String bio) {
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.themeDark = themeDark;
        this.emailNotifications = emailNotifications;
        this.language = language;
        this.bio = bio;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getThemeDark() {
        return themeDark;
    }

    public void setThemeDark(Boolean themeDark) {
        this.themeDark = themeDark;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    public static UserVO convertToUserVO(User user){
        UserVO userVO = new UserVO();
        userVO.setBio(user.getBio());
        userVO.setEmail(user.getEmail());
        userVO.setAvatarUrl(user.getAvatarUrl());
        userVO.setStatus(user.getStatus());
        userVO.setEmailNotifications(user.getEmailNotifications());
        userVO.setThemeDark(user.getThemeDark());
        userVO.setLanguage(user.getLanguage());
        return userVO;
    }
}
