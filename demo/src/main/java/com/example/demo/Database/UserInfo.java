package com.example.demo.Database;



public class UserInfo {

    private Integer id;

    private String username;
    private String password;
    private String role;

    public UserInfo(Integer id, String username, String password, String role){
        this.id = id;
        this.password = password;
        this.username = username;
        this.role = role;
    }

    public UserInfo(){
        id = 31;
        password = "none";
        username = "none";
        role = "not set";
    }
    public Integer getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }
    public void setId(Integer Id) {
        this.id = Id;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
