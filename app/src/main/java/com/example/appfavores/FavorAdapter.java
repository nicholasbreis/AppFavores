package com.example.appfavores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavorAdapter extends RecyclerView.Adapter<FavorAdapter.FavorViewHolder> {

    public interface OnFavorClickListener {
        void onFavorDetalhesClick(Favor favor);

        void onFavorContatoClick(Favor favor);
    }

    private Context context;
    private List<Favor> listaFavores;
    private OnFavorClickListener listener;

    public FavorAdapter(Context context, List<Favor> listaFavores, OnFavorClickListener listener) {
        this.context = context;
        this.listaFavores = listaFavores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_favor, parent, false);
        return new FavorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavorViewHolder holder, int position) {
        Favor favor = listaFavores.get(position);

        Log.d("DEBUG", "Bind -> " + favor.getTitulo() + " | ID: " + favor.getId());

        holder.textTitulo.setText(favor.getTitulo());
        holder.textDescricao.setText(favor.getDescricao());
        holder.textRecompensa.setText("Recompensa: " + favor.getRecompensa());

        holder.btnDetalhes.setOnClickListener(v -> listener.onFavorDetalhesClick(favor));
    }

    @Override
    public int getItemCount() {
        return listaFavores.size();
    }

    public static class FavorViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textDescricao, textRecompensa;
        Button btnDetalhes;

        public FavorViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textRecompensa = itemView.findViewById(R.id.textRecompensa);
            btnDetalhes = itemView.findViewById(R.id.btnDetalhes);
        }
    }
}
