package com.example.tp1_casino;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class Accueil extends AppCompatActivity
        implements View.OnClickListener
    {

    private SharedPreferences sp;
    private TextView txtNomUser;
    private TextView txtNbJetons;
    private Button btnRoulette;
    private int jetons;
    private String utilisateur;

    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        chargerSharedPreferences();

        // Affectation des widgets à des variables
        txtNomUser = findViewById(R.id.txtNomUser);
        txtNbJetons = findViewById(R.id.txtNbJetons);
        btnRoulette = findViewById(R.id.btnRoulette);
        Button btnGuichet = findViewById(R.id.btnGuichet);

        // Déclaration des listeners
        btnRoulette.setOnClickListener(this);
        btnGuichet.setOnClickListener(this);

        // Affichage
        gestionAffichage();

        // Résultat du retour du launcher
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            // Si on reviens du guichet, on ajoute les jetons
            if (result.getResultCode() == Activity.RESULT_OK)
            {
                //Manipulation du data
                Intent data = result.getData();
                assert data != null;
                int newJetons = data.getIntExtra(Guichet.JETONS, 0);
                jetons += newJetons;

                //Modification des SharedPreferences
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(utilisateur, jetons);
                editor.apply();
            }
            // Mise à jour de l'affichage
            gestionAffichage();
        });
    }

    // Chargement des SharedPreferences
    private void chargerSharedPreferences() {
        sp = getSharedPreferences("Utilisateurs", MODE_PRIVATE);
        utilisateur = sp.getString("Session", "Utilisateur");
        jetons = sp.getInt(utilisateur, 0);
    }

    // Gestion de l'affichage du texte de bienvenue et du bouton roulette
    private void gestionAffichage() {
        chargerSharedPreferences();

        txtNomUser.setText(utilisateur);
        txtNbJetons.setText(String.valueOf(jetons));

        // Affichage du bouton activé ou non
        btnRoulette.setEnabled(jetons > 0);
    }

    // Gestion du clic des boutons
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
    Intent intent;
    switch (v.getId()){
        case R.id.btnRoulette:
            intent = new Intent(Accueil.this, Roulette.class);
            launcher.launch(intent);
            break;
        case R.id.btnGuichet:
            intent = new Intent(getApplicationContext(), Guichet.class);
            launcher.launch(intent);
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}