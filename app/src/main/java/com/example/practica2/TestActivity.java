package com.example.practica2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

/*Pantalla de inicio que permitira comenzar juego o configurar parametros o ver records.*/

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toast.makeText(TestActivity.this,"Test", Toast.LENGTH_LONG).show();

        createAudio();
        createVideo();
    }

    public void createAudio(){
        Button buttonAudio = findViewById(R.id.buttonSound);

        final MediaPlayer mediaPlayer = MediaPlayer.create(TestActivity.this, R.raw.undertale_megalovania);

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });
    }

    public void createVideo(){
        VideoView videoShrek = findViewById(R.id.videoShrek);
        videoShrek.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.shrek_cebollas));
        videoShrek.setMediaController(new MediaController(this));
        videoShrek.requestFocus();
    }

}
