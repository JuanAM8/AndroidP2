package com.example.practica2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
/*Se mostraran los diferentes parametros de configuracion.*/

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
/*
    void testingReading() {
        try {
            File root = Environment.getExternalStorageDirectory();
            FileInputStream testFile = new FileInputStream(root + "/questions.txt");
            Toast.makeText(MainActivity.this,"Fichero leido", Toast.LENGTH_LONG).show();
            DataInputStream content = new DataInputStream(testFile);
            String input = content.readLine();
            // Hacer algo con la cadena
            Toast.makeText(MainActivity.this,input, Toast.LENGTH_LONG).show();
            //fin.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.this,"No fichero", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,"Otra cosa", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    */
}
