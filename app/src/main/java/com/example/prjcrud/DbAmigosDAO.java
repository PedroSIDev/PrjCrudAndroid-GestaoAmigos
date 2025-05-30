package com.example.prjcrud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DbAmigosDAO {

    private final String TABLE_AMIGOS = "Amigos";
    private DbAmigosGateway gw;

    public DbAmigosDAO(Context ctx){
        gw = DbAmigosGateway.getInstance(ctx);
    }

    public boolean salvar(String nome, String celular, String latitude, String longitude, int status){
        return salvar(0, nome, celular, latitude, longitude, status);
    }

    public boolean salvar(int id, String nome, String celular, String latitude, String longitude, int status){
        ContentValues cv = new ContentValues();
        cv.put("Nome", nome);
        cv.put("Celular", celular);
        cv.put("Latitude", latitude);
        cv.put("Longitude", longitude);
        cv.put("Status", status);
        if (id > 0) {
            return gw.getDatabase().update(TABLE_AMIGOS, cv, "ID=?", new String[]{ id + "" }) > 0;

        } else {
            return gw.getDatabase().insert(TABLE_AMIGOS, null, cv) > 0;
        }
    }

    public List<DbAmigo> listarAmigos(){
        List<DbAmigo> amigos = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Amigos", null);

        while (cursor.moveToNext())
        {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            @SuppressLint("Range") String celular = cursor.getString(cursor.getColumnIndex("Celular"));
            @SuppressLint("Range") String latitude = cursor.getString(cursor.getColumnIndex("Latitude"));
            @SuppressLint("Range") String longitude = cursor.getString(cursor.getColumnIndex("Longitude"));
            @SuppressLint("Range") int situacao = cursor.getInt(cursor.getColumnIndex("Status"));
            amigos.add(new DbAmigo(id, nome, celular, latitude, longitude, situacao));
        }
        cursor.close();
        return amigos;
    }

    public DbAmigo ultimoAmigo(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Amigos ORDER BY ID DESC", null);
        if(cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String celular = cursor.getString(cursor.getColumnIndex("Celular"));
            String latitude = cursor.getString(cursor.getColumnIndex("Latitude"));
            String longitude = cursor.getString(cursor.getColumnIndex("Longitude"));
            int status = cursor.getInt(cursor.getColumnIndex("Status"));
            cursor.close();
            return new DbAmigo(id, nome, celular, latitude, longitude, status);
        }
        return null;
    }

}

