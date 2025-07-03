package com.example.appfavores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavorAceitoAdapter extends RecyclerView.Adapter<FavorAceitoAdapter.FavorViewHolder> {

    public interface OnFavorAceitoClickListener {
        void onDetalhesClick(Favor favor);
        void onConcluirClick(Favor favor);
    }

    private List<Favor> listaFavores;
    private Context context;
    private OnFavorAceitoClickListener listener;

    public FavorAceitoAdapter(Context context, List<Favor> listaFavores, OnFavorAceitoClickListener listener) {
        this.context = context;
        this.listaFavores = listaFavores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favor_aceito, parent, false);
        return new FavorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavorViewHolder holder, int position) {
        Favor favor = listaFavores.get(position);
        holder.textTitulo.setText(favor.getTitulo());
        holder.textDescricao.setText(favor.getDescricao());
        holder.textRecompensa.setText("Recompensa: " + favor.getRecompensa());

        holder.btnDetalhes.setOnClickListener(v -> listener.onDetalhesClick(favor));
        holder.btnConcluir.setOnClickListener(v -> listener.onConcluirClick(favor));
    }

    @Override
    public int getItemCount() {
        return listaFavores.size();
    }

    public static class FavorViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textDescricao, textRecompensa;
        Button btnDetalhes, btnConcluir;

        public FavorViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textRecompensa = itemView.findViewById(R.id.textRecompensa);
            btnDetalhes = itemView.findViewById(R.id.btnDetalhes);
            btnConcluir = itemView.findViewById(R.id.btnConcluir);
        }
    }
}

