package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        boolean firstLaunch=getSharedPreferences("firstLaunch",MODE_PRIVATE).getBoolean("firstLaunch",true);
        if(firstLaunch) {
            getSharedPreferences("firstLaunch",MODE_PRIVATE).edit().putBoolean("firstLaunch",false).commit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),SwipesAfterSplash.class));
                    finish();
                }
            },1500);

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),PreLogin.class));
                    finish();
                }
            },1500);
        }



    }
}
