package com.bl4k3.keno;

public class User {
    private String Username, Email, Password, Mobile;
    private int Val;

    public User(){

    }

    public User(String Username, String Email, String Password, String Mobile, int Val){
        this.Username = Username;
        this.Email = Email;
        this.Mobile = Mobile;
        this.Password = Password;
        this.Val = Val;
    }

    public String getUsername() {
        return Username;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }
    public String getMobile(){
        return Mobile;
    }
    public int getVal(){return Val;}
}
