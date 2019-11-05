package com.example.practica2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*Pantalla de configuracion*/
public class SettingsActivity extends AppCompatActivity {

    Button backButton;
    Spinner themeSpinner;
    Spinner numberSpinner;
    EditText nameText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Inicializacion de variables del layout
        backButton = findViewById(R.id.buttonBack);
        themeSpinner = findViewById(R.id.themeSpinner);
        numberSpinner = findViewById(R.id.numberSpinner);
        nameText = findViewById(R.id.nameText);

        //Listeners de boton y spinners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Se escoge el tema de las preguntas segun el spinner
                int modeId;
                switch(position){
                    case 0:
                        modeId = R.array.questionsMovies;
                        break;
                    case 1:
                        modeId = R.array.questionsVideogames;
                        break;
                    case 2:
                        modeId = R.array.questionsMusic;
                        break;
                    default:
                        modeId = R.array.questionsMovies;
                        break;
                }
                savePreference("mode", modeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        numberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Se escoge el numero de preguntas segun el spinner
                int number;
                switch(position){
                    case 0:
                        number = 5;
                        break;
                    case 1:
                        number = 10;
                        break;
                    case 2:
                        number = 15;
                        break;
                    case 3:
                        number = 20;
                        break;
                    default:
                        number = 5;
                        break;
                }
                savePreference("number", number);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void goToMain(){
        saveUserName();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    //Guarda el tema y el numero de preguntas como preferencias
    public void savePreference(String key, int value){
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    //Guarda el nombre de usuario que haya en el texto a no ser que no cambie el que hay por defecto
    public void saveUserName(){
        String name = nameText.getText().toString();
        if(!name.equalsIgnoreCase("Nombre de Usuario")){
            SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", name);
            editor.apply();
        }

    }
}
