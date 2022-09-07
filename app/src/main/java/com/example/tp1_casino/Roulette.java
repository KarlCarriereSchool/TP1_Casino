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

        sp = getSharedPreferences("Utilisateurs", MODE_PRIVATE);

        utilisateur = sp.getString("Session", "Utilisateur");
        jetons = sp.getInt(utilisateur, 0);



        txtNumber = findViewById(R.id.txtNumber);
        txtMise = findViewById(R.id.txtMise);
        txtResultat = findViewById(R.id.txtResultat);
        radioGroupCouleur = findViewById(R.id.radioGroupCouleur);
        Button btnJouer = findViewById(R.id.btnJouer);
        swToggle = findViewById(R.id.swToggle);
        txtNbJetonsRestants = findViewById(R.id.txtNbJetonsRestants);

        txtNbJetonsRestants.setText(String.valueOf(jetons));

        btnJouer.setOnClickListener(this);
        radioGroupCouleur.setOnCheckedChangeListener(this);
        swToggle.setOnCheckedChangeListener(this);


    }

    public void onClick(View v){
        mise = Integer.parseInt(txtMise.getText().toString());
        Random random = new Random();
        int maxRoulette = 36;
        int resultat = random.nextInt(maxRoulette);
        if (v.getId() == R.id.btnJouer) {
            // Si pair/impair est sélectionné, on valide si le joueur a gagné et applique ses gains
            if (!swToggle.isChecked() && joueurAGagne(resultat)) gererGainsEtAffichage(2, resultat);
            // Sinon si Nombre est sélectionné, on valide si son choix et le résultat sont pareils et on applique les gains
            else if (resultat == Integer.parseInt(txtNumber.getText().toString())) gererGainsEtAffichage(36, resultat);
            // Sinon on multiplie la mise par 0
            else gererGainsEtAffichage(0, resultat);
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int btnId = buttonView.getId();
        if (btnId == R.id.swToggle) {
            if (isChecked) {
                // Si le toggle est vers la droite (checked), on désactive les couleurs et on active le nombre
                for (int i = 0; i < radioGroupCouleur.getChildCount(); i++)
                    radioGroupCouleur.getChildAt(i).setEnabled(false);
                txtNumber.setEnabled(true);
            } else {
                // Sinon, on active les couleurs et on désactive le nombre
                for (int i = 0; i < radioGroupCouleur.getChildCount(); i++)
                    radioGroupCouleur.getChildAt(i).setEnabled(true);
                txtNumber.setEnabled(false);
            }
        }
    }

    public void	onCheckedChanged(RadioGroup group, int checkedId){
        choixUserEstPair = checkedId == R.id.rdoPair;
    }

    private boolean joueurAGagne(int resultat) {
        return resultat % 2 == 0 && choixUserEstPair || resultat % 2 != 0 && !choixUserEstPair;
    }

    public void gererGainsEtAffichage(int multiplicateur, int resultat){

        if (multiplicateur == 0)
            jetons -= mise;

        jetons += mise * multiplicateur;

        //Modification des SharedPreferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(utilisateur, jetons);
        editor.apply();

        txtNbJetonsRestants.setText(String.valueOf(jetons));
        txtResultat.setText(String.valueOf(resultat));
    }
}
