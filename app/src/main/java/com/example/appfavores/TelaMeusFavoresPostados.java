package com.example.appfavores;

import android.content.Intent;
import android.os.Bundle;
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

public class TelaMeusFavoresPostados extends AppCompatActivity implements FavorPostadoAdapter.OnFavorPostadoClickListener {

    private RecyclerView recyclerView;
    private FavorPostadoAdapter adapter;
    private List<Favor> listaFavores = new ArrayList<>();
    private TextView textSemFavores;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_meus_favores_postados);

        recyclerView = findViewById(R.id.recyclerViewMeusFavores);
        textSemFavores = findViewById(R.id.textSemFavores);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavorPostadoAdapter(this, listaFavores, this);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        carregarMeusFavores();
    }

    private void carregarMeusFavores() {
        String uid = auth.getCurrentUser().getUid();

        db.collection("favores")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaFavores.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Favor favor = doc.toObject(Favor.class);
                            favor.setId(doc.getId());

                            if (favor.getIdCriador() != null && favor.getIdCriador().equals(uid)) {
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
        Intent intent = new Intent(this, DetalhesFavor.class);
        intent.putExtra("favor", favor);
        startActivity(intent);
    }

    @Override
    public void onFavorAceitarClick(Favor favor) {

    }

    @Override
    public void onDetalhesClick(Favor favor) {
        Intent intent = new Intent(this, DetalhesFavor.class);
        intent.putExtra("favor", favor);
        startActivity(intent);
    }

    @Override
    public void onExcluirClick(Favor favor) {
        db.collection("favores").document(favor.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Favor excluído!", Toast.LENGTH_SHORT).show();
                    listaFavores.remove(favor);
                    adapter.notifyDataSetChanged();
                    atualizarUI();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao excluir favor.", Toast.LENGTH_SHORT).show();
                });
    }
}
