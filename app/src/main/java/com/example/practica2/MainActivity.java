package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*Pantalla de inicio que permitira comenzar juego o configurar parametros o ver records.*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button testButton = findViewById(R.id.buttonStart);
        testButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                launch(v);
            }
        });
    }

    public void launch(View view){
        Intent i = new Intent(this, QuizActivity.class);
        startActivity(i);
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
