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

        // Chargement des SharedPreferences
        sp = getSharedPreferences("Utilisateurs", MODE_PRIVATE);

        // Affectation des widgets à des variables
        txtNom = findViewById(R.id.txtNom);
    }

    // Gestion du clic du bouton
    public void gererClick(View v){
        // Déclaration de l'editor de SharedPreferences et lecture du nom de l'utilisateur
        SharedPreferences.Editor editor = sp.edit();
        String stringNom = txtNom.getText().toString();

        // Si l'utilisateur n'existe pas, il est créé avec 15 jetons
        if (!sp.contains(stringNom)) editor.putInt(stringNom, 15);

        // Utilisateur est gardé en mémoire de session
        editor.putString("Session", stringNom);
        editor.apply();

        // Démarrage de l'activité accueil
        Intent intent = new Intent(Connexion.this, Accueil.class);
        startActivity(intent);
    }
}