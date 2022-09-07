package com.example.tp1_casino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Connexion extends AppCompatActivity {

    private EditText txtNom;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("Utilisateurs", MODE_PRIVATE);

        txtNom = findViewById(R.id.txtNom);
    }

    public void gererClick(View v){

        SharedPreferences.Editor editor = sp.edit();
        String stringNom = txtNom.toString();
        if (!sp.contains(stringNom)){
            editor.putInt(stringNom, 15);
        }

        editor.putString("session", stringNom);
        editor.apply();

        Intent intent = new Intent(this, Accueil.class);
        startActivity(intent);
    }
}