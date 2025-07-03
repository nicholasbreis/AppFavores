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
public class TelaFavoresAceitos extends AppCompatActivity implements
        FavorAdapter.OnFavorClickListener {
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
        carregarFavoresAceitos();
    }

    private void carregarFavoresAceitos() {
        String uid = auth.getCurrentUser() != null ?
                auth.getCurrentUser().getUid() : null;
        if (uid == null) {
            Toast.makeText(this, "Usuário não autenticado.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("favores")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaFavores.clear();
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            Favor favor = doc.toObject(Favor.class);
                            String docId = doc.getId();
                            if (docId != null && !docId.isEmpty()) {
                                favor.setId(docId);
                                if (uid.equals(favor.getIdExecutor())) {
                                    listaFavores.add(favor);
                                }
                            }
                        }
                        atualizarUI();
                    } else {
                        Toast.makeText(this, "Erro ao carregar favores.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void atualizarUI() {
        if (listaFavores.isEmpty()) {
            textSemFavores.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textSemFavores.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFavorDetalhesClick(Favor favor) {
        if (favor != null && favor.getId() != null) {
            Intent intent = new Intent(this, DetalhesFavor.class);
            intent.putExtra("favor", favor);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Erro: favor inválido.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavorContatoClick(Favor favor) {
// Opcional
    }
}
