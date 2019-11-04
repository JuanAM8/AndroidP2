package com.example.practica2;

import android.content.Intent;
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

        int score = nCorrect*10 - time;
        if(score < 0) score = 0;

        resultsText.setText("Correctas: "+nCorrect+"\nIncorrectas: "+nWrong+"\nTiempo: "+time+"\nPuntuación final: "+score);
    }

    public void goBackToTitle(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
