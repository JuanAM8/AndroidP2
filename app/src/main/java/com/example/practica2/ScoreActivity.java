package com.example.practica2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
/*Se mostraran las mejores puntuaciones y un boton de retroceso que cierre la actividad.*/
public class ScoreActivity extends AppCompatActivity {

    protected TextView[] textNames;
    protected TextView[] textScores;
    protected Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //Inicializacion de variables de layout
        textNames = new TextView[5];
        textScores = new TextView[5];

        textNames[0] = findViewById(R.id.name1);
        textNames[1] = findViewById(R.id.name2);
        textNames[2] = findViewById(R.id.name3);
        textNames[3] = findViewById(R.id.name4);
        textNames[4] = findViewById(R.id.name5);

        textScores[0] = findViewById(R.id.score1);
        textScores[1] = findViewById(R.id.score2);
        textScores[2] = findViewById(R.id.score3);
        textScores[3] = findViewById(R.id.score4);
        textScores[4] = findViewById(R.id.score5);

        buttonBack = findViewById(R.id.buttonBack);

        //Inicializacion de variables de preferencias
        SharedPreferences preferences = getSharedPreferences("scores", Context.MODE_PRIVATE);
        int[] scores = new int[5];
        String[] names = new String[5];

        //Se recogen las puntuaciones y los nombres de preferencias y se muestran en la tabla
        for (int i = 0; i < 5; i++) {
            scores[i] = preferences.getInt("score" + i, -1);
            names[i] = preferences.getString("name" + i, "None");
            if(scores[i] != -1){
                textNames[i].setText(names[i]);
                textScores[i].setText(""+scores[i]);
            }else{
                textNames[i].setText("");
                textScores[i].setText("");
            }
        }

        //Transicion a la pantalla principal
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });
    }

    public void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
