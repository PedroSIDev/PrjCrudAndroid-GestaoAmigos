package com.example.prjcrud;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prjcrud.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    RecyclerView recyclerView;
    DbAmigosAdapter adapter;
    DbAmigo amigoAlterado = null;

    private DbAmigosDAO dao;

    private void configurarRecycler() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TextView tvContador = findViewById(R.id.tvContadorAmigos);
        if (tvContador != null) {
            int quantidade = dao.getQuantidade();
            tvContador.setText("Amigos Cadastrados: " + quantidade);
        }

        adapter = new DbAmigosAdapter(dao.listarAmigos(), this);
        recyclerView.setAdapter(adapter);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i=0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        dao = new DbAmigosDAO(this);

        final EditText edtNome = (EditText) findViewById(R.id.edtNome);
        final EditText edtCelular = (EditText) findViewById(R.id.edtCelular);
        final EditText edtLatitude = (EditText) findViewById(R.id.edtLatitude);
        final EditText edtLongitude = (EditText) findViewById(R.id.edtLongitude);

        edtCelular.addTextChangedListener(new MaskTextWatcher(edtCelular, "(##) #####-####"));

        Intent intent = getIntent();
        if(intent.hasExtra("amigo")){
            // Modo de Edição
            findViewById(R.id.include_cadastrar_amigo).setVisibility(View.VISIBLE);
            findViewById(R.id.include_listar_amigos).setVisibility(View.INVISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);

            amigoAlterado = (DbAmigo) intent.getSerializableExtra("amigo");

            edtNome.setText(amigoAlterado.getNome());
            edtCelular.setText(amigoAlterado.getCelular());
            edtLatitude.setText(amigoAlterado.getLatitude());
            edtLongitude.setText(amigoAlterado.getLongitude());

        } else if (dao.getQuantidade() == 0) {
            findViewById(R.id.include_listar_amigos).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_cadastrar_amigo).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            findViewById(R.id.include_listar_amigos).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_cadastrar_amigo).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);
        });

        Button btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(view -> {
            if (dao.getQuantidade() == 0 && amigoAlterado == null) {
                finish();
            } else {
                Snackbar.make(view, "Cancelando...", Snackbar.LENGTH_LONG).show();
                findViewById(R.id.include_listar_amigos).setVisibility(View.VISIBLE);
                findViewById(R.id.include_cadastrar_amigo).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(view -> {
            String nome = edtNome.getText().toString();
            // Remove a máscara antes de salvar no banco
            String celular = MaskTextWatcher.unmask(edtCelular.getText().toString());
            String latitude = edtLatitude.getText().toString();
            String longitude = edtLongitude.getText().toString();
            int situacao = 1;

            boolean sucesso;
            if(amigoAlterado != null) {
                sucesso = dao.salvar(amigoAlterado.getId(), nome, celular, latitude, longitude, situacao);
            } else {
                sucesso = dao.salvar(nome, celular, latitude, longitude, situacao);
            }

            if (sucesso) {
                Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_LONG).show();
                configurarRecycler();

                edtNome.setText("");
                edtCelular.setText("");
                edtLatitude.setText("");
                edtLongitude.setText("");

                findViewById(R.id.include_listar_amigos).setVisibility(View.VISIBLE);
                findViewById(R.id.include_cadastrar_amigo).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                amigoAlterado = null; // Limpa o amigo em edição
            } else {
                Snackbar.make(view, "Erro ao salvar, consulte o log!", Snackbar.LENGTH_LONG).show();
            }
        });

        configurarRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            confirmarApagarTodos();
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmarApagarTodos() {
        final int quantidade = dao.getQuantidade();
        if (quantidade == 0) {
            Toast.makeText(this, "Não há amigos para apagar.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja apagar todos os " + quantidade + " amigo(s)?\n\nEsta ação não pode ser desfeita.")
                .setPositiveButton("Apagar Tudo", (dialog, which) -> {
                    dao.apagarTodos();
                    Toast.makeText(this, "Todos os amigos foram apagados.", Toast.LENGTH_SHORT).show();
                    configurarRecycler();
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
