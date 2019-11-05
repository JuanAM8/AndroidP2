package com.example.practica2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

//TODO:Resto de pantallas y transiciones (puntuacion)
//TODO:Almacenamiento persistente (puntuaciones y usuario)
//TODO:Desafio oro
public class QuizActivity extends AppCompatActivity {

    //Variables layout
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
    protected TextView timerText;

    //Pila de preguntas
    protected Stack<String> currentQuestionStack = new Stack<>();

    //Variables de reproduccion de audio
    protected boolean audioInit = false;
    protected boolean audioPresent = false;

    //Variables del numero de preguntas y los aciertos/fallos
    protected int nCorrect = 0;
    protected int nWrong = 0;

    protected int currentCorrectId = -1;
    protected int currentAnswerId = -1;
    protected int currentQuestionCount = 1;
    protected int totalQuestions;

    //Elementos del timer y su hilo
    protected Thread timerThread;
    protected boolean timerOn;
    Handler timerUIHandler = new Handler();
    protected int timerSecs = 0;

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
        timerText = findViewById(R.id.timer);
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
                audioPresent = false;
                //Si quedan preguntas muestra la siguiente y si no para el timer y va a resultados
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
                    timerOn = false;
                    goToResults();
                }

            }
        });
        //Inicia el set de preguntas y muestra la primera
        //El set de preguntas se decide con lo que este guardado en sharedpreferences
        int modeId = getModePreference();
        totalQuestions = getNumberPreference();
        try {
            initQuestions(modeId, totalQuestions);
            showRandomQuestion();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //Inicia el hilo que controla el cronometro
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(timerOn){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        timerSecs++;
                        timerUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                timerText.setText("Tiempo: "+timerSecs);
                            }
                        });
                    }
                }
            }
        });
        timerOn = true;
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Pausa el audio si se pausa la actividad
        if(audioPresent && audioInit){
            mediaPlayer.pause();
            buttonAudio.setBackgroundResource(R.drawable.play_icon);
        }
    }

    //Crea un reproductor de audio con el correspondiente al id
    public void createAudio(int audioId){
        audioInit = true;
        audioPresent = true;
        mediaPlayer = MediaPlayer.create(QuizActivity.this, audioId);
        buttonAudio.setBackgroundResource(R.drawable.play_icon);

        //Listener que pausa/reproduce el audio y cambia la imagen del boton
        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    buttonAudio.setBackgroundResource(R.drawable.play_icon);
                }else {
                    mediaPlayer.start();
                    buttonAudio.setBackgroundResource(R.drawable.pause_icon);
                }
            }
        });
        buttonAudio.setVisibility(View.VISIBLE);
    }

    //Muestra el reproductor de video y le asocia el correspondiente al id
    public void createVideo(int videoId){
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+ videoId));
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.setVisibility(View.VISIBLE);
    }

    //Muestra la imagen y la cambia a la correspondiente al id
    public void createImage(int imageId){
        image.setImageResource(imageId);
        image.setVisibility(View.VISIBLE);
    }

    //Recoje las preguntas y respuestas del grupo correspondiente del xml y las randomiza
    //antes de guardar el numero elegido en la pila
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

    //Muestra la siguiente pregunta, sus respuestas y su elemento multimedia asociado
    public void showRandomQuestion() throws NoSuchFieldException, IllegalAccessException {
        //Oculta todos los botones
        buttonConfirm.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        buttonAudio.setVisibility(View.GONE);
        image.setVisibility(View.GONE);

        //Saca una pregunta de la pila y separa sus elementos
        String currentQuestion = currentQuestionStack.pop();
        String[] parts = currentQuestion.split(";");

        //Muestra la pregunta
        questionText.setText(parts[2]);

        //Comprueba y guarda la posicion de la respuesta correcta segun donde este el *
        String[] answerText = new String[4];
        for(int i = 0; i < 4; i++){
            answerText[i] = parts[i + 3];
            if (answerText[i].charAt(0) == '*'){
                answerText[i] = answerText[i].substring(1);
                currentCorrectId = i;
            }
        }
        //Muestra las respuestas en los radio buttons
        button1.setText(answerText[0]);
        button2.setText(answerText[1]);
        button3.setText(answerText[2]);
        button4.setText(answerText[3]);
        //Comprueba el tipo de multimedia y lo muestra segun el id de la pregunta
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
        //Muestra la puntuacion actual
        scoreText.setText("Pregunta "+currentQuestionCount+"/"+totalQuestions+"  Correctas: "+nCorrect+"  Incorrectas: "+nWrong);
    }

    public void radioListener(RadioGroup group, int checkedId){
        buttonConfirm.setVisibility(View.VISIBLE);
    }

    //Comprueba si la respuesta es correcta y guarda el resultado
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

    //Inicia la actividad de resultados y envia el tiempo, los aciertos y los fallos
    public void goToResults(){
        Intent i = new Intent(this, EndActivity.class);
        i.putExtra("nCorrect", nCorrect);
        i.putExtra("nWrong", nWrong);
        i.putExtra("time", timerSecs);
        startActivity(i);
    }

    //Recoge el modo de sharedpreferences
    public int getModePreference(){
        int mode;
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        mode = preferences.getInt("mode", -1);
        if(mode == -1){
            mode = R.array.questionsMovies;
        }
        return mode;
    }

    //Recoge el numero de preguntas de sharedpreferences
    public int getNumberPreference(){
        int number;
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        number = preferences.getInt("number", -1);
        if(number == -1){
            number = 5;
        }
        return number;
    }
}
