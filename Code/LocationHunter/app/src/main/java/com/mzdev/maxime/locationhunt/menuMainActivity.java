package com.mzdev.maxime.locationhunt;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Locale;


public class menuMainActivity extends AppCompatActivity {


    public String urlWebsite;
    public Button playButton;
    public SharedPreferences myData;
    public SharedPreferences.Editor myDataEdit;
    public String Email;
    public Intent Settings;
    public Intent MyAccount;
    public Intent Play;
    public String Version;
    public String hostAPI = "http://217.182.68.69:8080/";
    public String langue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyLangue", MODE_PRIVATE);
        langue = pref.getString("lang_code","--");

        // Load user language for whole app
        if (!langue.equals("--")){
        Locale locale = new Locale(langue);
        Locale.setDefault(locale);
        Configuration conf = getBaseContext().getResources().getConfiguration();
        conf.locale= locale;
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        }
        // End of language modif

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        urlWebsite = getString(R.string.websiteURL);
        playButton = findViewById(R.id.bPlay);
        myData = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        myDataEdit = myData.edit();
        Email = myData.getString("Email","--");
        MyAccount = new Intent(this, myAccount.class);
        Play = new Intent(this, playActivity.class);
        Settings = new Intent(this, Settings.class);
        Version = getString(R.string.version);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            playButton.setEnabled(false);
            askPermission();
        }
        new menuMainActivity.VersionCheck().execute(); // Version Checker + If connected to service


    }


    public void askPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }

    public void showWarningBoxPerm(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.warningTitle);
        alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(R.string.warningText);
        alertDialogBuilder.setPositiveButton(R.string.tryAgain,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("<ALERT>","Try Again");
                askPermission();
            }
        });
        alertDialogBuilder.setNeutralButton(R.string.website,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("<ALERT>","Website");
                askPermission();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlWebsite));
                startActivity(browserIntent);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.quit,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("<ALERT>","Quit");
                finish();
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("<PERMISSION>","GRANTED");
                    playButton.setEnabled(true);
                } else {
                    Log.i("<PERMISSION>","VERBOTTEN");
                    showWarningBoxPerm();
                }
                return;
            }
        }
    }


    public void goMyAccountIntent(View view) {
        startActivity(MyAccount);
        finish();
    }

    public void startPlay(View view) {
        if (Email.equals("--")){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.warningTitle);
            alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage(R.string.textYouShouldConnect);
            alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("<ALERT>","ClosedWarningAlertBox");
                }
            });
            alertDialogBuilder.show();

        }
        else
        {
            finish();
            startActivity(Play);
        }
    }

    public void goSettings(View view) {
        finish();
        startActivity(Settings);
    }


    private class VersionCheck extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(menuMainActivity.this);
        public String entityContents;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            pdLoading.setCancelable(false);
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String urlBuild = hostAPI+"api.py?command=getVersion";
                HttpGet request = new HttpGet(urlBuild);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                entityContents = EntityUtils.toString(entity);
                entityContents = entityContents.replaceAll("\\s+","");
            }catch(Exception e)
                {Log.e("<ALERT>",e.toString());
                entityContents ="Connection Error";
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            if(entityContents.equals("Connection Error")) //Erreur de connexion
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(menuMainActivity.this);
                alertDialogBuilder.setTitle(R.string.Error);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(R.string.textNetworkError);
                alertDialogBuilder.setNeutralButton(R.string.quit,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("<ALERT>","ClosedErrorAlertBox");
                        finish();
                    }
                });
                alertDialogBuilder.show();
            }
            else  if (entityContents.equals("-[ServerApp]-"+Version)){
                Log.i("<VERSION>","A Jour: " + Version); // mis a jour
            }
            else{
                Log.i("<VERSION>","Vieille Version: " + Version); //Pas A jour

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(menuMainActivity.this);
                alertDialogBuilder.setTitle(R.string.Error);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(R.string.versionError);
                alertDialogBuilder.setNeutralButton(R.string.quit,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("<ALERT>","ClosedErrorAlertBox");
                        finish();
                    }
                });
                alertDialogBuilder.show();

            }

        }
    }



}
