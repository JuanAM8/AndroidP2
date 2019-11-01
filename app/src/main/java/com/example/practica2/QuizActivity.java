package com.example.practica2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
/*Se desarrollara toda la partida, mostrando las preguntas, respondiendolas y gestionando el fin de juego.*/

public class QuizActivity extends AppCompatActivity {

    protected VideoView videoView;
    protected Button buttonAudio;
    protected ImageView image;
    protected TextView questionText;
    protected RadioGroup questionRadio;
    protected RadioButton button1;
    protected RadioButton button2;
    protected RadioButton button3;
    protected RadioButton button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        videoView = findViewById(R.id.video);
        buttonAudio = findViewById(R.id.audioButton);
        image = findViewById(R.id.image);
        questionText = findViewById(R.id.questionText);
        questionRadio = findViewById(R.id.questionRadio);
        button1 = findViewById(R.id.radio_R1);
        button2 = findViewById(R.id.radio_R2);
        button3 = findViewById(R.id.radio_R3);
        button4 = findViewById(R.id.radio_R4);
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

    public void createAudio(int audioId){
        final MediaPlayer mediaPlayer = MediaPlayer.create(QuizActivity.this, audioId);

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });
        buttonAudio.setVisibility(View.VISIBLE);
    }

    public void createVideo(int videoId){
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+ videoId));
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.setVisibility(View.VISIBLE);
    }

    public void createImage(int imageId){
        image.setImageResource(imageId);
        image.setVisibility(View.VISIBLE);
    }

    public void showRandomQuestion(int setId) throws NoSuchFieldException, IllegalAccessException {
        videoView.setVisibility(View.GONE);
        buttonAudio.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        String[] setQuestions = getResources().getStringArray(setId);
        int random = (int) Math.round(Math.random() * (setQuestions.length - 1));
        String randomQuestion = setQuestions[random];
        String[] parts = randomQuestion.split(";");
        questionText.setText(parts[2]);
        button1.setText(parts[3]);
        button2.setText(parts[4]);
        button3.setText(parts[5]);
        button4.setText(parts[6]);
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
