package com.example.practica2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
/*Se mostraran las mejores puntuaciones y un boton de retroceso que cierre la actividad.*/
public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        SharedPreferences preferences = getSharedPreferences("scores", Context.MODE_PRIVATE);
        int[] scores = new int[5];
        for (int i = 0; i < 5; i++) {
            scores[i] = preferences.getInt("score" + i, -1);
            Toast.makeText(this, "score "+(i+1)+": "+scores[i], Toast.LENGTH_SHORT).show();
        }
    }

}
