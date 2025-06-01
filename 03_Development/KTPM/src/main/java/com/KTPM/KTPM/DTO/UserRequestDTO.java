package com.KTPM.KTPM.DTO;

public class UserRequestDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String fullname;
    private String roleName;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getRoleName() {
        return roleName;
        }
        public void setRoleName(String roleName) {
            this.roleName = roleName;
            }
}
