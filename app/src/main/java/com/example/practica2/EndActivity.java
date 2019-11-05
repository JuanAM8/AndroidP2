package com.example.practica2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/*Se mostraran las mejores puntuaciones y un boton de retroceso que cierre la actividad.*/
public class EndActivity extends AppCompatActivity {

    Button buttonContinue;
    TextView resultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        //Inicializacion de elementos del layout
        buttonContinue = findViewById(R.id.buttonContinue);
        resultsText = findViewById(R.id.resultsText);

        //Configura boton de continuar
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToTitle();
            }
        });

        //Recogida de parametros de la actividad anterior y modificacion del texto
        Bundle bundle = getIntent().getExtras();
        int nCorrect = bundle.getInt("nCorrect");
        int nWrong = bundle.getInt("nWrong");
        int total = bundle.getInt("total");
        int time = bundle.getInt("time");

        int score = nCorrect*100 - time;
        if(score < 0) score = 0;

        resultsText.setText("Correctas: "+nCorrect+"\nIncorrectas: "+nWrong+"\nTiempo: "+time+"\nPuntuación final: "+score);
        saveScore(score, getUsername());
    }

    public void goBackToTitle(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void saveScore(int currentScore, String currentUser){
        SharedPreferences preferences = getSharedPreferences("scores", Context.MODE_PRIVATE);
        int[] scores = new int[5];
        String[] names = new String[5];
        for (int i = 0; i < 5; i++) {
            scores[i] = preferences.getInt("score" + i, -1);
            names[i] = preferences.getString("name"+i, "None");
        }
        for (int i = 0; i < 5; i++) {
            if(currentScore > scores[i]){
                int aux = scores[i];
                String auxName = names[i];
                scores[i] = currentScore;
                names[i] = currentUser;
                for(int j = i+1;j < 5; j++){
                    int aux2 = scores[j];
                    String auxName2 = names[j];
                    scores[j] = aux;
                    names[j] = auxName;
                    aux = aux2;
                    auxName = auxName2;
                }
                break;
            }
        }
        SharedPreferences.Editor editor = preferences.edit();
        for(int i = 0; i < 5; i++){
            editor.putInt("score"+i, scores[i]);
            editor.putString("name"+i, names[i]);
        }
        editor.apply();
    }

    public String getUsername(){
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String name = preferences.getString("username", "");
        if(!name.equalsIgnoreCase("")){
            return name;
        }else{
            return "Anónimo";
        }
    }
}
