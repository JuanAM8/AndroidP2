package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    protected Button quizButton;
    protected Button settingsButton;
    protected Button scoreButton;

    protected boolean toastShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializacion de variables del layout
        quizButton = findViewById(R.id.buttonStart);
        settingsButton = findViewById(R.id.buttonConfig);
        scoreButton = findViewById(R.id.buttonRecords);

        //Definicion de Listeners de los botones que envian a la actividad correspondiente
        quizButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                launchGame(v);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                launchSettings(v);
            }
        });
        scoreButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                launchScores(v);
            }
        });
    }

    //Muestra un mensaje si no hay un nombre de usuario guardado y despues permite empezar anonimamente
    public void launchGame(View view){
        if(checkUserName() || toastShown){
            Intent i = new Intent(this, QuizActivity.class);
            startActivity(i);
        }else{
            Toast.makeText(this, "No has introducido nombre de usuario. Puedes hacerlo en configuración o comenzar como anónimo.", Toast.LENGTH_SHORT).show();
            toastShown = true;
        }
    }
    public void launchSettings(View view){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void launchScores(View view){
        Intent i = new Intent(this, ScoreActivity.class);
        startActivity(i);
    }

    //Comprueba si hay un usuario guardado en preferencias
    public boolean checkUserName(){
        String name;
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        name = preferences.getString("username", "");
        if(name.equalsIgnoreCase("")){
            return false;
        }else{
            return true;
        }
    }
}
