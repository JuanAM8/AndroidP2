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
import java.util.Stack;
/*Se desarrollara toda la partida, mostrando las preguntas, respondiendolas y gestionando el fin de juego.*/

//TODO:Arreglar peso videos y musica
//TODO:Seleccionar numero especifico de preguntas
//TODO:Mostrar errores y aciertos
//TODO:Finalizar una partida
//TODO:Resto de pantallas y transiciones (seleccionar set)
//TODO:Estetica
//TODO:Desafios plata y oro
//TODO:Cambiar xml a base de datos
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
    protected Button buttonConfirm;
    protected MediaPlayer mediaPlayer;

    protected Stack<String> currentQuestionStack = new Stack<String>();

    protected boolean audioInit = false;

    protected int nCorrect = 0;
    protected int nWrong = 0;

    protected int currentCorrectId = -1;
    protected int currentAnswerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //Inicializacion de elementos del layout
        videoView = findViewById(R.id.video);
        buttonAudio = findViewById(R.id.audioButton);
        image = findViewById(R.id.image);
        questionText = findViewById(R.id.questionText);
        questionRadio = findViewById(R.id.questionRadio);
        button1 = findViewById(R.id.radio_R1);
        button2 = findViewById(R.id.radio_R2);
        button3 = findViewById(R.id.radio_R3);
        button4 = findViewById(R.id.radio_R4);
        //Listener del RadioButton
        questionRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioListener(group, checkedId);
            }
        });
        buttonConfirm = findViewById(R.id.buttonConfirm);
        //Listener del boton de confirmar
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerResult();
                questionRadio.clearCheck();
                if(audioInit){
                    mediaPlayer.release();
                }
                try {
                    showRandomQuestion();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        //Inicia primera pregunta
        try {
            initQuestions(R.array.questionsMovies);
            showRandomQuestion();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void createAudio(int audioId){
        audioInit = true;
        mediaPlayer = MediaPlayer.create(QuizActivity.this, audioId);

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }else {
                    mediaPlayer.start();
                }
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

    public void initQuestions(int setId){
        String[] setQuestions = getResources().getStringArray(setId);
        for(int i = 0; i < setQuestions.length; i++){
            String tmp = setQuestions[i];
            int rnd = (int) Math.round(Math.random()*(setQuestions.length - i) + i);
            setQuestions[i] = setQuestions[rnd];
            setQuestions[rnd] = tmp;
        }
        for (String s : setQuestions) {
            currentQuestionStack.push(s);
        }
    }

    public void showRandomQuestion() throws NoSuchFieldException, IllegalAccessException {
        buttonConfirm.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        buttonAudio.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        /*
        String[] setQuestions = getResources().getStringArray(setId);

        int random = (int) Math.round(Math.random() * (setQuestions.length - 1));
        String randomQuestion = setQuestions[random];
        String[] parts = randomQuestion.split(";");*/
        String currentQuestion = currentQuestionStack.pop();
        String[] parts = currentQuestion.split(";");

        questionText.setText(parts[2]);
        //.substring(1)
        String[] answerText = new String[4];
        for(int i = 0; i < 4; i++){
            answerText[i] = parts[i + 3];
            if (answerText[i].charAt(0) == '*'){
                answerText[i] = answerText[i].substring(1);
                currentAnswerId = i;
            }
        }
        button1.setText(answerText[0]);
        button2.setText(answerText[1]);
        button3.setText(answerText[2]);
        button4.setText(answerText[3]);
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

    public void radioListener(RadioGroup group, int checkedId){
        buttonConfirm.setVisibility(View.VISIBLE);
    }

    public void checkAnswerResult(){
        currentAnswerId = questionRadio.getCheckedRadioButtonId();
        if(currentAnswerId == currentCorrectId){
            nCorrect++;
        }else{
            nWrong++;
        }
    }
}
