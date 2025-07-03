package com.example.appfavores;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetalhesFavor extends AppCompatActivity {

    private TextView textTitulo, textDescricao, textRecompensa, textData, textHora;
    private MaterialButton btnQueroAjudar, btnConcluir, btnExcluir;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Favor favorRecebido;
    private String uid;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_favor);

        // Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = auth.getCurrentUser().getUid();

        // Componentes visuais
        textTitulo = findViewById(R.id.textTitulo);
        textDescricao = findViewById(R.id.textDescricaoDetalhe);
        textRecompensa = findViewById(R.id.textRecompensaDetalhe);
        textData = findViewById(R.id.textDataDetalhe);
        textHora = findViewById(R.id.textHoraDetalhe);
        btnQueroAjudar = findViewById(R.id.btnQueroAjudar);
        btnConcluir = findViewById(R.id.btnConcluir);
        btnExcluir = findViewById(R.id.btnExcluir);

        // Receber objeto
        favorRecebido = getIntent().getParcelableExtra("favor");

        if (favorRecebido == null || favorRecebido.getId() == null) {
            Toast.makeText(this, "Erro: favor inválido ou sem ID.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Preencher dados
        textTitulo.setText(favorRecebido.getTitulo());
        textDescricao.setText(favorRecebido.getDescricao());
        textData.setText("Data: " + favorRecebido.getData());
        textHora.setText("Hora: " + favorRecebido.getHora());
        textRecompensa.setText("Recompensa: " + favorRecebido.getRecompensa());

        btnExcluir.setVisibility(View.GONE);
        btnConcluir.setVisibility(View.GONE);
        btnQueroAjudar.setVisibility(View.GONE);

        if (uid.equals(favorRecebido.getIdCriador())) {
            btnExcluir.setVisibility(View.VISIBLE);

            if (uid.equals(favorRecebido.getIdExecutor())) {
                btnConcluir.setVisibility(View.VISIBLE);
            }

        } else if (favorRecebido.getIdExecutor() == null || favorRecebido.getIdExecutor().isEmpty()) {

            btnQueroAjudar.setVisibility(View.VISIBLE);
        } else if (uid.equals(favorRecebido.getIdExecutor())) {

            btnConcluir.setVisibility(View.VISIBLE);
        }


        btnQueroAjudar.setOnClickListener(v -> {
            db.collection("favores")
                    .document(favorRecebido.getId())
                    .update("idExecutor", uid)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Você aceitou o favor!", Toast.LENGTH_SHORT).show();
                        String url = "https://wa.me/55"; // Adicione telefone aqui se necessário
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao aceitar o favor.", Toast.LENGTH_SHORT).show();
                    });
        });

        btnConcluir.setOnClickListener(v -> {
            db.collection("favores")
                    .document(favorRecebido.getId())
                    .update("status", "concluído")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Favor marcado como concluído!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao concluir o favor.", Toast.LENGTH_SHORT).show();
                    });
        });


        btnExcluir.setOnClickListener(v -> {
            db.collection("favores")
                    .document(favorRecebido.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Favor excluído com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao excluir o favor.", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
