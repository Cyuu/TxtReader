package com.cyun.sreader.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MyApplication extends Application {

    private static SharedPreferences mSpSetting;

//    /**
//     * 单例模式中用volatile和synchronized来满足双重检查锁机制
//     */
//    private static volatile MyApplication instance;
//
//    public static MyApplication getInstance(){
//        if(instance == null){
//            synchronized(MyApplication.class){
//                if(instance == null){
//                    instance = new MyApplication();
//                }
//            }
//        }
//        return instance;
//    }

    private static MyApplication instance;

    public static Application getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = new MyApplication();
        initSharedPreferences(getApplicationContext());
    }


    //初始化SharePreference
    public static void initSharedPreferences(Context context) {
        mSpSetting = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //初次打开
    public static void setisFirst(boolean is) {
        SharedPreferences.Editor editor = mSpSetting.edit();
        editor.putBoolean("ISFIRST", is).commit();
    }

    //判断是不是第一次打开
    public static boolean isFirst() {
        return mSpSetting.getBoolean("ISFIRST", true);
    }
}
