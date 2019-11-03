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
//TODO:Resto de pantallas y transiciones (seleccionar set)
//TODO:Almacenamiento persistente
//TODO:Estetica y cronometro
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
    protected TextView scoreText;

    protected Stack<String> currentQuestionStack = new Stack<>();

    protected boolean audioInit = false;

    protected int nCorrect = 0;
    protected int nWrong = 0;

    protected int currentCorrectId = -1;
    protected int currentAnswerId = -1;
    protected int currentQuestionCount = 1;
    protected int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //Inicializacion de elementos del layout
        videoView = findViewById(R.id.video);
        buttonAudio = findViewById(R.id.audioButton);
        image = findViewById(R.id.image);
        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);
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
                if(!currentQuestionStack.isEmpty()){
                    try {
                        currentQuestionCount++;
                        showRandomQuestion();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    questionText.setText("PUNTUACION FINAL: Acertadas: "+nCorrect+"  Incorrectas: "+nWrong);
                }

            }
        });
        //Inicia el set de preguntas y muestra la primera
        totalQuestions = 5;
        try {
            initQuestions(R.array.questionsMovies, totalQuestions);
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

    public void initQuestions(int setId, int numQuestions){
        String[] setQuestions = getResources().getStringArray(setId);
        for(int i = 0; i < setQuestions.length; i++){
            String tmp = setQuestions[i];
            int rnd = (int) Math.round(Math.random()*((setQuestions.length - 1) - i) + i);
            setQuestions[i] = setQuestions[rnd];
            setQuestions[rnd] = tmp;
        }
        for (int i = 0; i < numQuestions; i++) {
            currentQuestionStack.push(setQuestions[i]);
        }
    }

    public void showRandomQuestion() throws NoSuchFieldException, IllegalAccessException {
        buttonConfirm.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        buttonAudio.setVisibility(View.GONE);
        image.setVisibility(View.GONE);

        String currentQuestion = currentQuestionStack.pop();
        String[] parts = currentQuestion.split(";");

        questionText.setText(parts[2]);

        String[] answerText = new String[4];
        for(int i = 0; i < 4; i++){
            answerText[i] = parts[i + 3];
            if (answerText[i].charAt(0) == '*'){
                answerText[i] = answerText[i].substring(1);
                currentCorrectId = i;
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
        scoreText.setText("Pregunta "+currentQuestionCount+"/"+totalQuestions+"  Correctas: "+nCorrect+"  Incorrectas: "+nWrong);
    }

    public void radioListener(RadioGroup group, int checkedId){
        buttonConfirm.setVisibility(View.VISIBLE);
    }

    public void checkAnswerResult(){
        currentAnswerId = questionRadio.getCheckedRadioButtonId();
        switch(questionRadio.getCheckedRadioButtonId()){
            case R.id.radio_R1:
                currentAnswerId = 0;
                break;
            case R.id.radio_R2:
                currentAnswerId = 1;
                break;
            case R.id.radio_R3:
                currentAnswerId = 2;
                break;
            case R.id.radio_R4:
                currentAnswerId = 3;
                break;
        }
        if(currentAnswerId == currentCorrectId){
            nCorrect++;
        }else{
            nWrong++;
        }
    }
}
