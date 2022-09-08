package com.example.tp1_casino;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Roulette
        extends AppCompatActivity

        implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        RadioGroup.OnCheckedChangeListener

{

    private SharedPreferences sp;
    private EditText txtNumber;
    private EditText txtMise;
    private TextView txtResultat;
    private RadioGroup radioGroupCouleur;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch swToggle;
    private boolean choixUserEstPair = false;
    private int mise;
    private int jetons;
    private String utilisateur;
    private TextView txtNbJetonsRestants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        // Chargement des SharedPreferences
        sp = getSharedPreferences("Utilisateurs", MODE_PRIVATE);
        utilisateur = sp.getString("Session", "Utilisateur");
        jetons = sp.getInt(utilisateur, 0);

        // Gestion du solde inssufisant
        gererSoldeInsuffisant();

        // Affectation des widgets à des variables
        txtNumber = findViewById(R.id.txtNumber);
        txtMise = findViewById(R.id.txtMise);
        txtResultat = findViewById(R.id.txtResultat);
        radioGroupCouleur = findViewById(R.id.radioGroupCouleur);
        Button btnJouer = findViewById(R.id.btnJouer);
        swToggle = findViewById(R.id.swToggle);
        txtNbJetonsRestants = findViewById(R.id.txtNbJetonsRestants);

        // Affichage du nombre de jetons restants et désactivation du champ "number"
        txtNbJetonsRestants.setText(String.valueOf(jetons));
        txtNumber.setEnabled(false);

        // Déclaration des listeners
        btnJouer.setOnClickListener(this);
        radioGroupCouleur.setOnCheckedChangeListener(this);
        swToggle.setOnCheckedChangeListener(this);
    }

    // Action du bouton
    public void onClick(View v){
        mise = Integer.parseInt(txtMise.getText().toString());
        // Une roulette va de 0 à 36
        Random random = new Random();
        int maxRoulette = 36;
        int resultat = random.nextInt(maxRoulette);
        if (v.getId() == R.id.btnJouer) {
            // Si "pair/impair" est sélectionné, on valide si le joueur a gagné et applique ses gains
            if (!swToggle.isChecked() && joueurAGagne(resultat)) gererGainsEtAffichage(2, resultat);

            // Sinon si "Nombre" est sélectionné, on valide si son choix et le résultat sont pareils et on applique les gains
            else if (swToggle.isChecked() && resultat == Integer.parseInt(txtNumber.getText().toString())) gererGainsEtAffichage(36, resultat);

            // Sinon on multiplie la mise par 0 car le joueur a perdu
            else gererGainsEtAffichage(0, resultat);
        }
    }

    // Permet de sélectionner le mode de jeu avec un toggle
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int btnId = buttonView.getId();
        if (btnId == R.id.swToggle) {
            if (isChecked) {
                // Si le toggle est vers la droite (checked), on désactive les pair/impair et on active le nombre
                for (int i = 0; i < radioGroupCouleur.getChildCount(); i++)
                    radioGroupCouleur.getChildAt(i).setEnabled(false);
                txtNumber.setEnabled(true);
            } else {
                // Sinon, on active les pair/impair et on désactive le nombre
                for (int i = 0; i < radioGroupCouleur.getChildCount(); i++)
                    radioGroupCouleur.getChildAt(i).setEnabled(true);
                txtNumber.setEnabled(false);
            }
        }
    }

    // Met à jour le booléin choixUserEstPair en fonction du choix de l'utilisateur
    public void	onCheckedChanged(RadioGroup group, int checkedId){
        choixUserEstPair = checkedId == R.id.rdoPair;
    }

    // Détermine si le choix pair/impair du joueur coincide avec le nombre roulé
    private boolean joueurAGagne(int resultat) {
        return resultat % 2 == 0 && choixUserEstPair || resultat % 2 != 0 && !choixUserEstPair;
    }

    // Calcule les gains ou les pertes, met à jour les SharedPreferences et met l'affichage à jour
    public void gererGainsEtAffichage(int multiplicateur, int resultat){
        // Si le joueur a perdu, la mise est déduite de son solde de jetons
        if (multiplicateur == 0) jetons -= mise;

        jetons += mise * multiplicateur;

        //Modification des SharedPreferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(utilisateur, jetons);
        editor.apply();

        // Affichage du résultat et mise à jour du nombre de jetons du joueur
        txtNbJetonsRestants.setText(String.valueOf(jetons));
        txtResultat.setText(String.valueOf(resultat));

        // Gestion du solde inssufisant
        gererSoldeInsuffisant();
    }

    // Affiche un Toast et retourne à l'écran d'accueil si le joueur n'a plus de jetons
    private void gererSoldeInsuffisant() {
        if (jetons <= 0) {
            Toast.makeText(this, "Vous n'avez plus de jetons, redirection...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
