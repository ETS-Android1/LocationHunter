package com.mzdev.maxime.locationhunt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    public Intent mainActivity;
    public Intent settingsActivity;
    public String langue;
    public RadioButton rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyLangue", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        langue = pref.getString("lang_code","--");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mainActivity = new Intent(this, menuMainActivity.class);
        settingsActivity = new Intent(this, Settings.class);
        if (langue.equals("--")){
            rb = findViewById(R.id.phoneDefault);
            rb.setChecked(true);
        }
        else if (langue.equals("fr")){
            rb = findViewById(R.id.fr);
            rb.setChecked(true);
        }
        else if (langue.equals("en")){
            rb = findViewById(R.id.en);
            rb.setChecked(true);
        }


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rbGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String langueDefault = Locale.getDefault().getDisplayLanguage();
                String langueCode = "";
                if (langueDefault.equals("français")){
                    langueCode = "fr";
                }else{
                    langueCode = "en";
                }

                if (i == 2131165294){
                    editor.clear();
                    editor.commit();
                    Locale locale = new Locale(langueCode);
                    Locale.setDefault(locale);
                    Configuration conf = getBaseContext().getResources().getConfiguration();
                    conf.locale= locale;
                    getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
                    finish();
                    startActivity(settingsActivity);
                }
                else if (i == 2131165257) {
                    editor.putString("lang_code", "fr");
                    editor.commit();
                    Locale locale = new Locale("fr");
                    Locale.setDefault(locale);
                    Configuration conf = getBaseContext().getResources().getConfiguration();
                    conf.locale= locale;
                    getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
                    finish();
                    startActivity(settingsActivity);
                }
                else if (i == 2131165247){
                    editor.putString("lang_code", "en");
                    editor.commit();
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration conf = getBaseContext().getResources().getConfiguration();
                    conf.locale= locale;
                    getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
                    finish();
                    startActivity(settingsActivity);
                }
            }
        });

    }

    public void returnMain(View view) {
        finish();
        startActivity(mainActivity);
    }



}
