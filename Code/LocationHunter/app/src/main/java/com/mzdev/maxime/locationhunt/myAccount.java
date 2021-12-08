package com.mzdev.maxime.locationhunt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class myAccount extends AppCompatActivity  {

    public SharedPreferences myData;
    public SharedPreferences.Editor myDataEdit;
    public TextView status;
    public String Email;
    public String hostAPI = "http://217.182.68.69:8080/";
    public String emailInputTxt="";
    public String passowrdInputTxt="";
    public EditText emailInput;
    public EditText passwordInput;
    public Button bConnexion;
    public Button bDeconnexionInsc;
    public String urlWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        myData = getSharedPreferences("MyData",Context.MODE_PRIVATE);
        myDataEdit = myData.edit();
        urlWebsite = getString(R.string.websiteURL);
        status = findViewById(R.id.tvStatus);
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        bConnexion = findViewById(R.id.bConnexion);
        bDeconnexionInsc = findViewById(R.id.bDeconnexionetInsc);
        Email = myData.getString("Email","--");
        Log.i("<ACCOUNT>",Email);
        if (Email.equals("--")){
            status.setText(getString(R.string.textYS)+" "+getString(R.string.textDisconnected));
            bDeconnexionInsc.setText(R.string.bCreateAccount);
        }
        else
        {
            status.setText(getString(R.string.textYS)+" "+getString(R.string.textConnected));
            bConnexion.setEnabled(false);
            emailInput.setHint(Email);
            passwordInput.setHint("xxxxx");
            emailInput.setEnabled(false);
            passwordInput.setEnabled(false);
            bDeconnexionInsc.setText(R.string.bDeconnection);
        }

    }

    public void returnMain(View view) {
        Intent mainReturn = new Intent(this, menuMainActivity.class);
        startActivity(mainReturn);
        finish();
    }

    public void connexion(View view) {
        Log.i("<ACCOUNT>","CONNEXION");
        emailInputTxt = emailInput.getText().toString();
        passowrdInputTxt = passwordInput.getText().toString();
        Log.i("<ACCOUNT>",emailInputTxt);
        Log.i("<ACCOUNT>","WORKS");
        new myAccount.identifVerif().execute();
    }

    public void deconnexionIns(View view) {
       if (bDeconnexionInsc.getText().toString().equals(getString(R.string.bDeconnection)))
       {
           myDataEdit.clear();
           myDataEdit.commit();
           status.setText(getString(R.string.textYS)+" "+getString(R.string.textDisconnected));
           bConnexion.setEnabled(true);
           emailInput.setHint(R.string.textYE);
           passwordInput.setHint(R.string.textYP);
           emailInput.setEnabled(true);
           passwordInput.setEnabled(true);
           bDeconnexionInsc.setText(R.string.bCreateAccount);
       }
       else if (bDeconnexionInsc.getText().toString().equals(getString(R.string.bCreateAccount)))
       {
           Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlWebsite+"/account.php?lang="+getText(R.string.Langue))); // to change with app language
           startActivity(browserIntent);
       }
       else {
           Log.i("<ACCOUNT>",bDeconnexionInsc.getText().toString());
           Log.i("<ACCOUNT>", getString(R.string.bCreateAccount));
       }
    }


    private class identifVerif extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(myAccount.this);
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

            try
            {
                Log.i("<identifVERIF>","STARTED");
                passowrdInputTxt = SHA1(passowrdInputTxt);
                String urlBuild = hostAPI+"api.py?command=connexion&email="+emailInputTxt+"&password="+passowrdInputTxt;
                HttpGet request = new HttpGet(urlBuild);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                entityContents = EntityUtils.toString(entity);
                entityContents = entityContents.replaceAll("\\s+", "");
                Log.i("<identifVERIF>",entityContents);
            }catch(Exception e)
            {
                Log.e("<identifVERIF>",e.toString());
                entityContents = "Connection Error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            if (entityContents.equals("-[ServerApp]-Match")){
                myDataEdit.putString("Email",emailInputTxt);
                myDataEdit.putString("Password",passowrdInputTxt);
                myDataEdit.commit();
                bConnexion.setEnabled(false);
                bDeconnexionInsc.setText(R.string.bDeconnection);
                emailInput.setEnabled(false);
                passwordInput.setEnabled(false);
                status.setText(getString(R.string.textYS)+" "+getString(R.string.textConnected));
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myAccount.this);
                alertDialogBuilder.setTitle(R.string.Success);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(R.string.textSuccessConnection);
                alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("<ALERT>","ClosedSuccessAlertBox");
                    }
                });
                alertDialogBuilder.show();


                Log.i("<identifVERIF>","Connected");
            }
            else if(entityContents.equals("Connection Error")){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myAccount.this);
                alertDialogBuilder.setTitle(R.string.Error);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(R.string.textNetworkError);
                alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("<ALERT>","ClosedErrorAlertBox");
                    }
                });
                alertDialogBuilder.show();

            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myAccount.this);
                alertDialogBuilder.setTitle(R.string.Error);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(R.string.textErrorConnection);
                alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("<ALERT>","ClosedErrorAlertBox");
                    }
                });
                alertDialogBuilder.show();

            }
        }

    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

}


