package com.example.practica2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/*Pantalla de inicio que permitira comenzar juego o configurar parametros o ver records.*/

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toast.makeText(TestActivity.this,"Test", Toast.LENGTH_LONG).show();

    }

    protected void onResume(){
        super.onResume();
        try {
            showRandomQuestion(R.array.questionsMovies);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
    public Map<String, Object> createMediaMap(int setId){
        Map<String, Object> mediaMap = new HashMap<String, Object>();
        String[] setQuestions = getResources().getStringArray(setId);
        for(int i = 0; i < setQuestions.length;i++){
            String[] parts = setQuestions[i].split(";");
            if(parts[1] == "video"){
                int idVideo =
            }
        }


        return mediaMap;
    }

     */

    public void createAudio(int audioId){
        Button buttonAudio = findViewById(R.id.buttonSound);

        final MediaPlayer mediaPlayer = MediaPlayer.create(TestActivity.this, audioId);

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });
    }

    public void createVideo(int videoId){
        VideoView videoShrek = findViewById(R.id.videoShrek);
        videoShrek.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+ videoId));
        videoShrek.setMediaController(new MediaController(this));
        videoShrek.requestFocus();
    }

    public void createImage(int imageId){
        ImageView img = findViewById(R.id.image);
        img.setImageResource(imageId);
    }

    public void showRandomQuestion(int setId) throws NoSuchFieldException, IllegalAccessException {
        TextView textQuestion = findViewById(R.id.Question);
        String[] setQuestions = getResources().getStringArray(setId);
        int random = (int) Math.round(Math.random() * (setQuestions.length - 1));
        String randomQuestion = setQuestions[random];
        String[] parts = randomQuestion.split(";");
        textQuestion.setText(parts[2]);
        switch (parts[1]){
            case "video":
                Field videoField = R.raw.class.getDeclaredField(parts[0]);
                int videoId = videoField.getInt(videoField);
                createVideo(videoId);
                break;
            case "audio":
                Field audioField = R.raw.class.getDeclaredField(parts[0]);
                int audioId = audioField.getInt(audioField);
                createAudio(audioId);
                break;
            case "image":
                Field imageField = R.drawable.class.getDeclaredField(parts[0]);
                int imageId = imageField.getInt(imageField);
                createImage(imageId);
        }
    }
}
