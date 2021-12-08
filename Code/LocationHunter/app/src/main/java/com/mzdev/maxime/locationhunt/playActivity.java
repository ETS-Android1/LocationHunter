package com.mzdev.maxime.locationhunt;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static java.lang.Math.abs;

public class playActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private String provider;
    public SharedPreferences myData;
    public SharedPreferences.Editor myDataEdit;
    public TextView toBeFind;
    public String Email;
    public String hostAPI = "http://217.182.68.69:8080/";
    public String Password;
    public EditText emailInput;
    public EditText passwordInput;
    public Button bConnexion;
    public Intent returnMenuMain;
    public String[] dataToFind;
    public String[] coordS;
    public Double latTF;
    public Double lonTF;
    public Double latC;
    public Double lonC;
    public int providerStatus=1;
    public String nameLoc;
    private MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoibG9jYXRpb25odW50ZXIiLCJhIjoiY2pqcHdsOXRpMW40OTNrcnN5b2syNHF0ayJ9.ONBze7V9aNEeGM9IAPeQ1w");
        setContentView(R.layout.activity_play);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        latC = 0.0;
        lonC = 0.0;
        new playActivity.GetToday().execute();
        returnMenuMain = new Intent(this, menuMainActivity.class);
        toBeFind = findViewById(R.id.toBeFind);
        //Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("<PERMISSION>","Erreur, retour main activity");
            startActivity(returnMenuMain);
            finish();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        myData = getSharedPreferences("MyData",Context.MODE_PRIVATE); // get account data
        myDataEdit = myData.edit();
        Email = myData.getString("Email","--");
        Password = myData.getString("Password","--");

        if (Email.equals("--")) // check if connected
        {
            startActivity(returnMenuMain);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("<PERMISSION>","Erreur, retour main activity");
            startActivity(returnMenuMain);
            finish();
            return;
        }
        locationManager.requestLocationUpdates(provider, 100, 1, this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onLocationChanged(Location location) {
        latC = location.getLatitude();
        lonC = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Log.i("<ALERT>","provider Enabled");
        providerStatus = 1;
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.i("<ALERT>","provider Disabled");
        providerStatus = 0;
        CheckProvider();
    }

    public void returnMain(View view) {
        startActivity(returnMenuMain);
        finish();
    }

    public Double gpsDist(Double lat1, Double lon1, Double lat2, Double lon2){
        lat1 = (lat1*Math.PI)/180;
        lat2 = (lat2*Math.PI)/180;
        lon1 = (lon1*Math.PI)/180;
        lon2 = (lon2*Math.PI)/180;
        Double dLat = lat2-lat1;
        Double dLon = lon2-lon1;
        Double Va = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2)*Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        Double Vc = 2*Math.atan((Math.sqrt(Va))/(Math.sqrt(1-Va)));
        return Vc * 6371008;
    }

    public void CheckProvider(){
        if (providerStatus == 0)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.warningTitle);
            alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage(R.string.warningProvider);
            alertDialogBuilder.setPositiveButton(R.string.tryAgain,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("<ALERT>","Try Again");
                    CheckProvider();
                }
            });
            alertDialogBuilder.setNegativeButton(R.string.Back,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("<ALERT>","Quit");
                    startActivity(returnMenuMain);
                    finish();
                }
            });
            alertDialogBuilder.show();
        }
    }


    public void ImHere(View view) {
        if (latC==0.0 && lonC == 0.0)
        {
            Log.i("<ALERT>","Not Updated");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(playActivity.this);
            alertDialogBuilder.setTitle(R.string.Error);
            alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage(R.string.warningNoSatelite); //TODO: change message
            alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("<ALERT>","ClosedErrorAlertBox");
                }
            });
            alertDialogBuilder.show();
        }
        else
        {
            new playActivity.Verification().execute();
        }
    }




    private class GetToday extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(playActivity.this);
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
                Log.i(">>", "STARTED");
                String urlBuild = hostAPI + "api.py?command=getToday";
                Log.i(">>", urlBuild);
                HttpGet request = new HttpGet(urlBuild);
                Log.i(">>", "1");
                HttpClient httpClient = new DefaultHttpClient();
                Log.i(">>", "2");
                HttpResponse response = httpClient.execute(request);
                Log.i(">>", "3");
                HttpEntity entity = response.getEntity();
                entityContents = EntityUtils.toString(entity);
                //entityContents = entityContents.replaceAll("\\s+","");
                //Log.i(">>",entityContents+"<<");
            } catch (Exception e) {
                Log.e(">>", e.toString());
                entityContents = "Connection Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();

            if (entityContents.equals("Connection Error")) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(playActivity.this);
                alertDialogBuilder.setTitle(R.string.Error);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(R.string.textNetworkError);
                alertDialogBuilder.setNeutralButton(R.string.Back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(">>", "ClosedErrorAlertBox");
                        startActivity(returnMenuMain);
                        finish();
                    }
                });
                alertDialogBuilder.show();


            } else {
                dataToFind = entityContents.split(";");
                nameLoc = dataToFind[3];
                String[] nameLocArray = nameLoc.split("/");
                int lenNameLoc = nameLocArray.length;
                int countNameLoc = 0;
                nameLoc = "";
                while (countNameLoc < lenNameLoc) { // accent System
                    if (nameLocArray[countNameLoc].equals("ecirc")) {
                        nameLoc = nameLoc + "ê";
                    } else if (nameLocArray[countNameLoc].equals("eacute")) {
                        nameLoc = nameLoc + "é";
                    } else if (nameLocArray[countNameLoc].equals("egrave")) {
                        nameLoc = nameLoc + "è";
                    } else if (nameLocArray[countNameLoc].equals("agrave")) {
                        nameLoc = nameLoc + "à";
                    } else if (nameLocArray[countNameLoc].equals("acirc")) {
                        nameLoc = nameLoc + "â";
                    } else if (nameLocArray[countNameLoc].equals("ccedil")) {
                        nameLoc = nameLoc + "ç";
                    } else if (nameLocArray[countNameLoc].equals("*")) {
                        nameLoc = nameLoc + "'";
                    } else {
                        nameLoc = nameLoc + nameLocArray[countNameLoc];
                    }
                    countNameLoc += 1;
                }
                toBeFind.setText(">> " + nameLoc);
                coordS = dataToFind[2].split(",");
                latTF = Double.parseDouble(coordS[0]);
                lonTF = Double.parseDouble(coordS[1]);

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        CameraUpdate cU = new CameraUpdate() {
                            @Nullable
                            @Override
                            public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
                                CameraPosition position = new CameraPosition.Builder()
                                        .target(new LatLng(latTF, lonTF)) // Sets the new camera position
                                        .zoom(14) // Sets the zoom to level 10
                                        .tilt(20) // Set the camera tilt to 20 degrees
                                        .build();
                                return position;
                            }
                        };
                        Marker dest = mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latTF, lonTF))
                                .title(nameLoc)
                                .snippet( getString(R.string.text_YouHavetoReach) )); // to be modify
                        mapboxMap.moveCamera(cU);
                        mapboxMap.getUiSettings().setAllGesturesEnabled(false);


                    }
                });

            }
        }
    }

    private class Verification extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(playActivity.this);
        public String entityContents;
        public Double distcal = gpsDist(latC,lonC,latTF,lonTF);
        public String[] entityContentsA;


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
                Log.i(">>","STARTED");

                String urlBuild = hostAPI+"api.py?command=sendIt&latS="+latC+"&longS="+lonC+"&dist="+distcal+"&email="+Email+"&password="+Password;
                Log.i(">>",urlBuild);
                HttpGet request = new HttpGet(urlBuild);
                Log.i(">>","1");
                HttpClient httpClient = new DefaultHttpClient();
                Log.i(">>","2");

                HttpResponse response = httpClient.execute(request);
                Log.i(">>", "3");
                HttpEntity entity = response.getEntity();
                entityContents = EntityUtils.toString(entity);
                entityContents = entityContents.replaceAll("\\s+", "");
                entityContentsA = entityContents.split(";");


                Log.i(">>",entityContents+"<<");
            }catch(Exception e){
                Log.e(">>",e.toString());
                entityContents = "Connection Error";
                entityContentsA = entityContents.split(";");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            if (entityContentsA[0].equals("-[ServerApp]-oui")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(playActivity.this);
                alertDialogBuilder.setTitle(R.string.Success);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(getString(R.string.warningYes) + " " + entityContentsA[1] + " points");
                alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(">>","ClosedSuccessAlertBox");
                    }
                });
                alertDialogBuilder.show();


                Log.i(">>","Connected");
            }
            else if(entityContentsA[0].equals("-[ServerApp]-non")){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(playActivity.this);
                alertDialogBuilder.setTitle(R.string.Error);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(getString(R.string.warningNo)+Integer.toString(abs(distcal.intValue()-50))+" m");
                alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(">>","ClosedErrorAlertBox");
                    }
                });
                alertDialogBuilder.show();

            }else if(entityContentsA[0].equals("-[ServerApp]-Youhavealreadyplayedtoday")){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(playActivity.this);
                alertDialogBuilder.setTitle(R.string.Error);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(R.string.warningARP);
                alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(">>","ClosedErrorAlertBox");
                    }
                });
                alertDialogBuilder.show();

            }
            else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(playActivity.this);
                alertDialogBuilder.setTitle(R.string.Error);
                alertDialogBuilder.setIcon(R.drawable.location_logo_hunter);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(R.string.textNetworkError);
                alertDialogBuilder.setNeutralButton(R.string.Back,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(">>","ClosedErrorAlertBox");
                    }
                });
                alertDialogBuilder.show();

            }
        }

    }


}
