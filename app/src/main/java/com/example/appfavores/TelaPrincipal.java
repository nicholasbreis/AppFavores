package com.example.appfavores;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class TelaPrincipal extends AppCompatActivity {

    private Button btnVisualizarFavores;
    private Button btnCadastrarFavor;
    private Button btnLogout;
    private Button btnMeusFavores;
    private Button btnFavoresAceitos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        btnVisualizarFavores = findViewById(R.id.btnVisualizarFavores);
        btnCadastrarFavor = findViewById(R.id.btnCadastrarFavor);
        btnLogout = findViewById(R.id.btnLogout);
        btnMeusFavores = findViewById(R.id.btnMeusFavores);
        btnFavoresAceitos = findViewById(R.id.btnFavoresAceitos);

        btnVisualizarFavores.setOnClickListener(v -> startActivity(new Intent(this, TelaFavoresPostados.class)));

        btnCadastrarFavor.setOnClickListener(v -> startActivity(new Intent(this, CadastroDeFavor.class)));

        btnMeusFavores.setOnClickListener(v -> startActivity(new Intent(this, TelaMeusFavoresPostados.class)));

        btnFavoresAceitos.setOnClickListener(v -> startActivity(new Intent(this, TelaFavoresAceitos.class)));

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(TelaPrincipal.this, MainActivity.class); // Substitua LoginActivity pelo nome correto da sua tela de login
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Evita voltar com o botão "Voltar"
            startActivity(intent);
            finish();
        });
    }
}