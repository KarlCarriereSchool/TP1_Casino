package com.example.tp1_casino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Accueil extends AppCompatActivity {

    private SharedPreferences sp;
    private TextView txtNomUser;
    private TextView txtNbJetons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        sp = getSharedPreferences("Utilisateurs", MODE_PRIVATE);

        String utilisateur = sp.getString("Session", "Utilisateur");
        int jetons = sp.getInt(utilisateur, 0);

        txtNomUser = findViewById(R.id.txtNomUser);
        txtNbJetons = findViewById(R.id.txtNbJetons);

        txtNomUser.setText(utilisateur);
        txtNbJetons.setText(String.valueOf(jetons));

    }
}