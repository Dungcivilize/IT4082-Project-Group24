package com.KTPM.KTPM.DTO;

public class LoginResponse {
    private String message;
    private Long user_id;
    private String username;
    private Integer roleId;
    private String fullname;
    // Thêm các trường khác nếu cần

    // Constructor
    public LoginResponse(String message, Long user_id, String user_name, Integer role, String fullname) {
        this.message = message;
        this.user_id = user_id;
        this.username = user_name;
        this.roleId = role;
        this.fullname = fullname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return username;
    }

    public void setUser_name(String user_name) {
        this.username = user_name;
    }

    public Integer getRole() {
        return roleId;
    }

    public void setRole(Integer role) {
        this.roleId = role;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
