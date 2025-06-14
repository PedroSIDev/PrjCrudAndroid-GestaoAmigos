package com.example.prjcrud;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DbAmigosAdapter extends RecyclerView.Adapter<DbAmigosHolder> {

    private final List<DbAmigo> amigos;
    private final Context context;

    public DbAmigosAdapter(List<DbAmigo> amigos, Context context) {
        this.amigos = amigos;
        this.context = context;
    }

    @NonNull
    @Override
    public DbAmigosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DbAmigosHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_dados_amigo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DbAmigosHolder holder, int position) {
        DbAmigo amigo = amigos.get(position);

        holder.txvNome.setText(amigo.getNome());

        String celularBruto = amigo.getCelular();
        String celularFormatado = MaskTextWatcher.mask("(##) # ####-####", celularBruto);
        holder.txvCelular.setText(celularFormatado);

        holder.txvLatitude.setText("Latitude: " + amigo.getLatitude());
        holder.txvLongitude.setText("Longitude: " + amigo.getLongitude());

        if (amigo.getLatitude() == null || amigo.getLatitude().isEmpty()) {
            holder.txvLatitude.setVisibility(View.GONE);
        } else {
            holder.txvLatitude.setVisibility(View.VISIBLE);
        }

        if (amigo.getLongitude() == null || amigo.getLongitude().isEmpty()) {
            holder.txvLongitude.setVisibility(View.GONE);
        } else {
            holder.txvLongitude.setVisibility(View.VISIBLE);
        }

        holder.btnExcluir.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Excluindo...")
                    .setMessage("Tem certeza que quer excluir o amigo " + amigo.getNome() + "?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        DbAmigosDAO dao = new DbAmigosDAO(context);
                        dao.excluir(amigo.getId());
                        excluirAmigo(amigo);
                        Toast.makeText(context, "Amigo excluído!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Não", null)
                    .create().show();
        });

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("amigo", amigo);
            context.startActivity(intent);
        });

        holder.btnLigar.setOnClickListener(v -> {
            Uri uri = Uri.parse("tel:" + amigo.getCelular());
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(intent);
        });

        holder.btnSms.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + amigo.getCelular()));
            intent.putExtra("sms_body", "Olá, " + amigo.getNome() + "! ");
            context.startActivity(intent);
        });

        holder.btnWhats.setOnClickListener(v -> {
            try {
                String numeroLimpo = amigo.getCelular().replaceAll("[^0-9]", "");
                String numeroInternacional = "55" + numeroLimpo;
                String url = "https://api.whatsapp.com/send?phone=" + numeroInternacional;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Não foi possível abrir o WhatsApp.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return amigos != null ? amigos.size() : 0;
    }

    public void inserirAmigo(DbAmigo amigo) {
        amigos.add(amigo);
        notifyItemInserted(getItemCount());
    }

    public void excluirAmigo(DbAmigo amigo) {
        int position = amigos.indexOf(amigo);
        amigos.remove(position);
        notifyItemRemoved(position);
    }

    public void atualizarAmigo(DbAmigo amigo) {
        int position = -1;
        for (int i = 0; i < amigos.size(); i++) {
            if (amigos.get(i).getId() == amigo.getId()) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            amigos.set(position, amigo);
            notifyItemChanged(position);
        }
    }
}
