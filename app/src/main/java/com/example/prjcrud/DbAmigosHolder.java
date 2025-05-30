package com.example.prjcrud;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DbAmigosHolder extends RecyclerView.ViewHolder {
    public TextView    txvNome;
    public TextView    txvCelular;
    public TextView    txvLatitude;
    public TextView txvLongitude;
    public ImageButton btnEditar;
    public ImageButton btnExcluir;
    public ImageButton btnSms;
    public ImageButton btnLigar;
    public ImageButton btnWhats;
    public DbAmigosHolder(View itemView) {
        super(itemView);
        txvNome = (TextView) itemView.findViewById(R.id.txvNome);
        txvCelular = (TextView) itemView.findViewById(R.id.txvCelular);
        txvLatitude = (TextView) itemView.findViewById(R.id.txvLatitude);
        txvLongitude = (TextView) itemView.findViewById(R.id.txvLongitude);
        btnEditar = (ImageButton) itemView.findViewById(R.id.btnEditar);
        btnExcluir = (ImageButton) itemView.findViewById(R.id.btnExcluir);
        btnSms = (ImageButton) itemView.findViewById(R.id.btnSms);
        btnLigar = (ImageButton) itemView.findViewById(R.id.btnLigar);
        btnWhats = (ImageButton) itemView.findViewById(R.id.btnWhats);
    }
}

