package com.example.appfavores;

import android.os.Parcel;
import android.os.Parcelable;

public class Favor implements Parcelable {
    private String titulo;
    private String descricao;
    private String recompensa;
    private String data;
    private String hora;
    private String idCriador;
    private String idExecutor;
    private String telefoneCriador;


    private String id;

    public Favor() {
    }

    public Favor(String titulo, String descricao, String recompensa, String data, String hora, String idCriador) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.recompensa = recompensa;
        this.data = data;
        this.hora = hora;
        this.idCriador = idCriador;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getRecompensa() { return recompensa; }
    public String getData() { return data; }
    public String getHora() { return hora; }
    public String getIdCriador() { return idCriador; }
    public String getIdExecutor() { return idExecutor; }
    public String getTelefoneCriador() { return telefoneCriador; }
    public String getId() { return id; }


    public void setIdCriador(String idCriador) { this.idCriador = idCriador; }
    public void setIdExecutor(String idExecutor) { this.idExecutor = idExecutor; }
    public void setTelefoneCriador(String telefoneCriador) { this.telefoneCriador = telefoneCriador; }
    public void setId(String id) { this.id = id; }


    protected Favor(Parcel in) {
        titulo = in.readString();
        descricao = in.readString();
        recompensa = in.readString();
        data = in.readString();
        hora = in.readString();
        idCriador = in.readString();
        idExecutor = in.readString();
        telefoneCriador = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(descricao);
        dest.writeString(recompensa);
        dest.writeString(data);
        dest.writeString(hora);
        dest.writeString(idCriador);
        dest.writeString(idExecutor);
        dest.writeString(telefoneCriador);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Favor> CREATOR = new Creator<Favor>() {
        @Override
        public Favor createFromParcel(Parcel in) {
            return new Favor(in);
        }

        @Override
        public Favor[] newArray(int size) {
            return new Favor[size];
        }
    };
}
