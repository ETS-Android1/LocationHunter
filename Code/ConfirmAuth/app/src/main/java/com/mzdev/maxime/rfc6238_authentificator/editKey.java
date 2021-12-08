package com.mzdev.maxime.rfc6238_authentificator;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class editKey extends AppCompatActivity {

    public String keyList = "";//setup la variable keyList qui va etre remplie avec les clefs stockee dans l application
    public File enviPathConf = Environment.getExternalStorageDirectory();
    public String tempArray[];
    public List<String> keyArray = new ArrayList<String>();;
    public int arrayLen = 0;
    public int arrayPos;
    public int countFill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_key);




//READ FILE STEP
        try {
            FileInputStream fileIn=openFileInput("rfc6238authentificatorconfig.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[9999];
            String s=""; //Initialisation de la reponse temporaire
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

        //init number picker
        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        // cree un Array qui va de 0 a 98
        String[] nums = new String[99];
        for(int i = 0; i<nums.length; i++){
            nums[i] = Integer.toString(i);
        }
        //fin creation Array + debut mise en place roulette choix
        np.setMinValue(1);
        np.setMaxValue(99);
        np.setWrapSelectorWheel(true);
        np.setDisplayedValues(nums);
        np.setValue(1);
        //Fin mise en place roulette choix


        //Transformation de keylist (type: String) en Array et affichage de la premiere valeur dans le EditText



       try {
           if (keyList.length() > 1) {
               tempArray = (keyList.split("\\,", -1));
               for (String sVal : tempArray) {
                   keyArray.add(sVal);
               }
               arrayPos = np.getValue() - 1;

               EditText eT = (EditText) findViewById(R.id.editText4);
               eT.setText(keyArray.get(arrayPos));
           }
       }catch (Exception e){
       }

        countFill = arrayLen;
        while(countFill<99){
            keyArray.add("NewKey");
            countFill += 1;
        }

// Initialisation Listener qui va detecter changement roulette choix
    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            EditText eT = (EditText) findViewById(R.id.editText4);
            keyArray.set(oldVal - 1, eT.getText().toString());
            try{

            eT.setText(keyArray.get(newVal - 1));
            }catch(Exception e){
                eT.setText("NewKey");
            }


        }
    });
    }

    public void Save(View view)
    {
        String toBeSave = "";
        EditText eT = (EditText) findViewById(R.id.editText4);
        String lastEdit = eT.getText().toString();

        int count = 0;
        while(count<99){
            try {
                if (!keyArray.get(count).equals("NewKey")) {
                    if (toBeSave.equals("")){
                        toBeSave = toBeSave+keyArray.get(count);
                    }
                    else
                    {
                        toBeSave = toBeSave + "," + keyArray.get(count);
                    }

                }
            }
            catch (Exception e)
            {
            }
            count += 1;
        }

        if (!lastEdit.equals("NewKey") && !lastEdit.equals("") && !keyArray.contains(lastEdit)){
            if (toBeSave.equals("")){
                toBeSave = toBeSave +lastEdit;
            }
            else{
                toBeSave = toBeSave + ","+lastEdit;
            }


        }
        try {
            FileOutputStream fos = openFileOutput("rfc6238authentificatorconfig.txt", Context.MODE_PRIVATE);
            fos.write(toBeSave.toString().getBytes());
            fos.close();
        }catch (IOException e) {
        }

        Intent intent = new Intent(this, Main.class );
        startActivity(intent);
    }
}
