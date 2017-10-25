package com.mycampus.rontikeky.myacademic.Config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Anggit on 19/09/2017.
 */

public class PrefHandler {
    public SharedPreferences.Editor editor;
    public SharedPreferences sharedPreferences;
    public Context context;

    int PRIVATE_MODE = 0;

    private final String SHAREDPREFNAME = "token";
    private final String IS_LOGIN = "isLoggin";

    public final String TOKEN_KEY = "token";
    public final String NAME_KEY = "nama";
    public final String EMAIL_KEY = "email";
    public final String FACULTY_KEY = "faculty";
    public final String FROM_DATE_KEY = "fromDate";
    public final String TO_DATE_KEY = "toDate";
    public final String IMAGE_PROFILE_KEY = "imageprofile";
    public final String AUTHORIZATION_EO_KEY = "eokeyauth";
    public final String EO_COUNT_KEY = "eocountkey";
    public final String EMAIL_GOOGLE_KEY = "emailgooglekey";
    public final String NAME_GOOGLE_KEY = "namegooglekey";
    public final String IS_LOGIN_WITH_GOOGLE = "isloginwithgoogle";

    public final String AUTH_TOKEN_GOOGLE = "auth_token_google";


    public PrefHandler(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHAREDPREFNAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public String getTOKEN_KEY(){
        return sharedPreferences.getString(TOKEN_KEY,"");
    }

    public boolean isToken_Key_Exist(){
        return sharedPreferences.contains(TOKEN_KEY);
    }

    public void setTOKEN_KEY(String token){
        editor.putString(TOKEN_KEY,token);
        editor.commit();
    }

    public boolean isName_User_Exist(){
        return sharedPreferences.contains(NAME_KEY);
    }

    public String getNAME_KEY(){
        return sharedPreferences.getString(NAME_KEY,"");
    }

    public void setNAME_KEY(String name){
        editor.putString(NAME_KEY,name);
        editor.commit();
    }

    public boolean isEmail_User_Exist(){
        return sharedPreferences.contains(EMAIL_KEY);
    }

    public String getEMAIL_KEY(){
        return sharedPreferences.getString(EMAIL_KEY,"");
    }

    public void setEMAIL_KEY(String email){
        editor.putString(EMAIL_KEY,email);
        editor.commit();
    }

    public String getFACULTY_KEY(){
        return sharedPreferences.getString(FACULTY_KEY,"");
    }

    public void setFACULTY_KEY(String faculty){
        editor.putString(FACULTY_KEY,faculty);
        editor.commit();
    }

    public String getFromDate(){
        return sharedPreferences.getString(FROM_DATE_KEY,"");
    }

    public void setFROM_DATE_KEY(String fromDate){
        editor.putString(FROM_DATE_KEY,fromDate);
        editor.commit();
    }

    public void clearFROM_DATE_KEY(){
        editor.remove(FROM_DATE_KEY);
        editor.commit();
    }


    public String getTO_DATE_KEY(){
        return sharedPreferences.getString(TO_DATE_KEY,"");
    }

    public void setTO_DATE_KEY(String toDate){
        editor.putString(TO_DATE_KEY,toDate);
        editor.commit();
    }

    public void clearTO_DATE_KEY(){
        editor.remove(TO_DATE_KEY);
        editor.commit();
    }

    public void setIMAGE_PROFILE_KEY(String URL_PROFIL_IMAGE){
        editor.putString(IMAGE_PROFILE_KEY,URL_PROFIL_IMAGE);
        editor.commit();
    }

    public String getIMAGE_PROFILE_KEY(){
        return sharedPreferences.getString(IMAGE_PROFILE_KEY,"");
    }


    public void setAUTHORIZATION_EO_KEY(String eo_auth){
        editor.putString(AUTHORIZATION_EO_KEY,eo_auth);
        editor.commit();
    }

    public String getAUTHORIZATION_EO_KEY() {
        return sharedPreferences.getString(AUTHORIZATION_EO_KEY,"");
    }

    public String getAUTH_TOKEN_GOOGLE() {
        return sharedPreferences.getString(AUTH_TOKEN_GOOGLE,"");
    }

    public void setAUTH_TOKEN_GOOGLE(String token){
        editor.putString(AUTH_TOKEN_GOOGLE,token);
        editor.commit();
    }

    public String getEO_COUNT_KEY(){
        return sharedPreferences.getString(EO_COUNT_KEY,"");
    }

    public void  setEO_COUNT_KEY(String eo_count_key){
        editor.putString(EO_COUNT_KEY,eo_count_key);
        editor.commit();
    }

    public void setEMAIL_GOOGLE_KEY(String email_google_key){
        editor.putString(EMAIL_GOOGLE_KEY,email_google_key);
        editor.commit();
    }

    public String getEMAIL_GOOGLE_KEY(){
        return sharedPreferences.getString(EMAIL_GOOGLE_KEY,"");
    }

    public void setNAME_GOOGLE_KEY(String name_google_key){
        editor.putString(NAME_GOOGLE_KEY,"");
        editor.commit();
    }

    public String getNAME_GOOGLE_KEY(){
        return sharedPreferences.getString(NAME_GOOGLE_KEY,"");
    }

    public boolean getIS_LOGIN_WITH_GOOGLE() {
        return sharedPreferences.contains(IS_LOGIN_WITH_GOOGLE);
    }

    public void setIS_LOGIN_WITH_GOOGLE(String is_login_with_google){
        editor.putString(IS_LOGIN_WITH_GOOGLE,is_login_with_google);
        editor.commit();
    }

    public void setLogout(){
        editor.clear();
        editor.commit();
    }

}
