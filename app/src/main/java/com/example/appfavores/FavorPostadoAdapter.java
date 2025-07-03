package com.example.appfavores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FavorPostadoAdapter extends RecyclerView.Adapter<FavorPostadoAdapter.FavorViewHolder> {

    // Interface para cliques nos itens
    public interface OnFavorPostadoClickListener {
        void onFavorDetalhesClick(Favor favor);

        void onFavorAceitarClick(Favor favor);

        void onDetalhesClick(Favor favor);

        void onExcluirClick(Favor favor);
    }

    private List<Favor> listaFavores;
    private Context context;
    private OnFavorPostadoClickListener listener;

    public FavorPostadoAdapter(Context context, List<Favor> listaFavores, OnFavorPostadoClickListener listener) {
        this.context = context;
        this.listaFavores = listaFavores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meu_favor, parent, false);
        return new FavorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavorViewHolder holder, int position) {
        Favor favor = listaFavores.get(position);

        holder.textTitulo.setText(favor.getTitulo());
        holder.textDescricao.setText(favor.getDescricao());
        holder.textRecompensa.setText("Recompensa: " + favor.getRecompensa());

        holder.btnDetalhes.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetalhesClick(favor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaFavores.size();
    }

    // ViewHolder
    public static class FavorViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textDescricao, textRecompensa;
        MaterialButton btnDetalhes;

        public FavorViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textRecompensa = itemView.findViewById(R.id.textRecompensa);
            btnDetalhes = itemView.findViewById(R.id.btnDetalhes);
        }
    }
}
