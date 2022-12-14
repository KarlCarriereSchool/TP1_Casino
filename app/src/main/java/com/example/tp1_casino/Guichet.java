package com.example.tp1_casino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Guichet extends AppCompatActivity implements View.OnClickListener{

    public static final String JETONS = "Jetons";
    private EditText txtQuantite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guichet);

        // Affectation des widgets à des variables
        txtQuantite = findViewById(R.id.txtQuantite);
        Button btnConfirmer = findViewById(R.id.btnConfirmer);

        // Déclaration des listeners
        btnConfirmer.setOnClickListener(this);
    }

    // Gestion du clic du bouton
    public void onClick(View v) {
        if (v.getId() == R.id.btnConfirmer) {
            int qteJetons = Integer.parseInt(txtQuantite.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(JETONS, qteJetons);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}