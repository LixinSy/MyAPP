package com.lx.myapp;

/**
 * Created by 李莘 on 2018/4/5.
 */

public class User {

    private static int id ;
    private static String nickname;
    private static String password;
    private static String phone;
    private static String email;
    private static String figurePath;
    private static String sex;
    private static String note;

    public static void setId(int id) {
        User.id = id;
    }

    public static void setNickname(String nickname) {
        User.nickname = nickname;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static void setPhone(String phone) {
        User.phone = phone;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static void setFigurePath(String figurePath) {
        User.figurePath = figurePath;
    }

    public static void setSex(String sex) {
        User.sex = sex;
    }

    public static void setNote(String note) {
        User.note = note;
    }

    public static void setRemembered(String remembered) {
        User.remembered = remembered;
    }

    private static String remembered;


    public static String getName() {
        return nickname;
    }

    public static String getPassword() {
        return password;
    }

    public static String getRemembered() {
        return remembered;
    }

    public static int getId() {
        return id;
    }

    public static String getNickname() {
        return nickname;
    }

    public static String getPhone() {
        return phone;
    }

    public static String getEmail() {
        return email;
    }

    public static String getFigurePath() {
        return figurePath;
    }

    public static String getSex() {
        return sex;
    }

    public static String getNote() {
        return note;
    }
}
