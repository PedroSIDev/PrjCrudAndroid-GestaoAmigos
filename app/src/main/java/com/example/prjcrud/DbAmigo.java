package com.example.prjcrud;

import java.io.Serializable;

public class DbAmigo implements Serializable {

    private int id;
    private String nome;
    private String celular;
    private String latitude;
    private String longitude;
    private int status;

    public DbAmigo(int id, String nome, String celular, String latitude, String longitude, int status) {
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }
    public int getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getCelular(){
        return this.celular;
    }

    public String getLatitude(){
        return this.latitude;
    }

    public String getLongitude(){
        return this.longitude;
    }

    public int getStatus(){
        return this.status;
    }

    @Override
    public boolean equals(Object o){
        return this.id == ((DbAmigo)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}

