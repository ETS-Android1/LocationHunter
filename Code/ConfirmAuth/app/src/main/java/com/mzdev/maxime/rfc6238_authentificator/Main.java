package com.mzdev.maxime.rfc6238_authentificator;


import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;



import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Main extends AppCompatActivity {

    int statu = 0;
    String keyList;
    public String tempArray[];
    public String show ="";
    public CountDownTimer timer;
    public TextView tV;
    public ProgressBar pB;



    private static byte[] convertToBytes(String hex) {
        byte[] bArray = new BigInteger("10" +  hex,16).toByteArray();
        byte[] res = new byte[bArray.length - 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = bArray[i+1];
        }
        return res;
    }


    private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text){
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey =
                    new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }


    private static String sixDigitsGen(String key) {
        int timeSec = (int)(System.currentTimeMillis()/1000)/30;



        key = key.replace(" ", "").toUpperCase();



        try {

            //PREP
            String timeS = Integer.toString(timeSec);
            while(timeS.length()<16) {
                timeS = "0"+timeS;
            }


            Base32 b32 = new Base32();
            byte[] keyB = b32.decode(key);
            String hexKey = new String(Hex.encodeHex(keyB));
            byte[] keyBytes = convertToBytes(hexKey);

            byte[] data = new byte[8];
            long value = timeSec;
            for (int i = 8; i-- > 0; value >>>= 8) {
                data[i] = (byte) value;
            }

            //CRYPT


            byte[] hash = hmac_sha("HmacSHA1", keyBytes, data);
            final int offset = hash[hash.length - 1] & 0xF;

            int binary =
                    ((hash[offset] & 0x7f) << 24) |
                            ((hash[offset + 1] & 0xff) << 16) |
                            ((hash[offset + 2] & 0xff) << 8) |
                            (hash[offset + 3] & 0xff);

            int hotp = binary%1000000; //var final contenant le code 6 chiffre
            String hotpS = Integer.toString(hotp);
            while(hotpS.length()<6)
            {
                hotpS = "0"+hotpS;
            }
            return hotpS;
        } catch (Exception e) {
            return "ERROR";
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pB = findViewById(R.id.progressBar);
        pB.setMax(100);
        pB.setProgress(100);


        try {
            FileInputStream fileIn=openFileInput("rfc6238authentificatorconfig.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[9999];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            keyList = s;


        } catch (Exception e) {
            e.printStackTrace();
        }



        tV = (TextView) findViewById(R.id.textView);
        tV.setTextColor(Color.BLACK);
        tV.setText(show);
        tV.setMovementMethod(new ScrollingMovementMethod());

       //Try to get all the key



        timer = new CountDownTimer(1000*60*1, 1000) {

            public void onTick(long millisUntilFinished) {
                show = "";
                int timeSecBar = (int)(((System.currentTimeMillis()/1000)%30)*100)/30;

                try {
                    tempArray = (keyList.split("\\,", -1));
                    for (String sVal : tempArray) {
                        String[] deltaVal = sVal.split("\\:", -1);
                        String modif = sixDigitsGen(deltaVal[1]);
                        show = show+"<< " + deltaVal[0] + " >> --> " + modif+"\n";
                        tV.setText(show);
                        pB.setProgress(100-timeSecBar);

                    }
                }catch(Exception e){
                }

            }

            public void onFinish() {
                timer.start();
            }
        }.start();


    }


    public void modifActivity(View view){
            Intent intent = new Intent(this, editKey.class );
            startActivity(intent);
    }


}
