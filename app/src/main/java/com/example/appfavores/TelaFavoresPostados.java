package com.example.appfavores;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TelaFavoresPostados extends AppCompatActivity implements FavorAdapter.OnFavorClickListener {

    private RecyclerView recyclerView;
    private FavorAdapter adapter;
    private List<Favor> listaFavores = new ArrayList<>();
    private TextView textSemFavores;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_favores_postados);

        recyclerView = findViewById(R.id.recyclerViewFavores);
        textSemFavores = findViewById(R.id.textSemFavores);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavorAdapter(this, listaFavores, this);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        carregarFavoresPendentes();
    }

    private void carregarFavoresPendentes() {
        db.collection("favores")
                .whereEqualTo("status", "pendente")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaFavores.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Favor favor = doc.toObject(Favor.class);
                            String docId = doc.getId();
                            if (docId != null && !docId.isEmpty()) {
                                favor.setId(docId);
                                listaFavores.add(favor);
                            }
                        }

                        atualizarUI();
                    } else {
                        Toast.makeText(this, "Erro ao carregar favores.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void atualizarUI() {
        // Atualizar RecyclerView
        adapter.notifyDataSetChanged();
        
        // Mostrar mensagem se não há favores
        if (listaFavores.isEmpty()) {
            textSemFavores.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textSemFavores.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFavorDetalhesClick(Favor favor) {
        if (favor != null) {
            Log.d("DEBUG", "Favor clicado: " + favor.getTitulo() + " | ID: " + favor.getId());
        } else {
            Log.d("DEBUG", "Favor é null");
        }

        if (favor != null && favor.getId() != null) {
            Intent intent = new Intent(this, DetalhesFavor.class);
            intent.putExtra("favor", favor);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Erro: favor inválido.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavorContatoClick(Favor favor) {
        if (favor != null && favor.getTelefoneCriador() != null && !favor.getTelefoneCriador().isEmpty()) {
            String url = "https://wa.me/55" + favor.getTelefoneCriador();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Telefone não disponível.", Toast.LENGTH_SHORT).show();
        }
    }
}
