package com.dexian.robinhood;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreffClass {

    Context context;

    public SharedPreffClass(Context context) {
        this.context = context;
    }


    public void saveUserID(String id){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("USER_ID", id);

        mEditor.apply();
    }


    public String getIPAddress() {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String s = mSharedPreferences.getString("USER_ID", "-1");

        return s;
    }



    public void saveUserName(String P) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("USER_NAME", P);

        mEditor.apply();
    }


    public String getUserName() {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String s = mSharedPreferences.getString("USER_NAME", "Null");

        return s;
    }



}
