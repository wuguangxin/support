package com.wuguangxin.simple.bean;

/**
 * Created by wuguangxin on 2020/8/20.
 */
public class UserBean {
    public String username;
    public String password;
    public String realName;
    public int age;
    public String sex;

    public UserBean() {
    }

    public UserBean(String username, String password, String realName, int age, String sex) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.age = age;
        this.sex = sex;
    }

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserBean{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", realName='").append(realName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}