package com.example.sila.brasfootmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Model.Jogador;

public class JogadorActivity extends AppCompatActivity {
    private Spinner spinner;
    private TextView tvPos, tvHab, tvCon, tvMot, tvValor, tvCaixa;
    private ArrayList<Jogador> jogadors;
    private SQLiteDatabase db;
    private int pos;
    private static final String ARQUIVO_PREFERENCIAS = "arquivo_preferencias";
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogador);
        spinner = (Spinner) findViewById(R.id.spinnerJogador);
        tvPos = (TextView) findViewById(R.id.tvPosicao);
        tvHab = (TextView) findViewById(R.id.tvHabilidade);
        tvMot = (TextView) findViewById(R.id.tvMotivacao);
        tvValor = (TextView) findViewById(R.id.tvValorVenda);
        tvCon = (TextView) findViewById(R.id.tvCondicionamento);
        tvCaixa = (TextView) findViewById(R.id.tvCaixaClube);
        carregarJogadores();
    }

    public void carregarJogadores() {

        jogadors = new ArrayList<>();
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT jogador.habilidade as habilidadej,jogador.nome as nomej,jogador.posicao as posicaoj,jogador.condicionamento as condj,jogador.motivacao as motj,jogador.jogando as jogandoj,valor,caixa FROM jogador INNER JOIN clube ON jogador.clubeId = clube.clubeId WHERE clube.nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            jogadors.add(new Jogador(c.getInt(c.getColumnIndex("habilidadej")), c.getString(c.getColumnIndex("nomej")), c.getInt(c.getColumnIndex("posicaoj")), c.getInt(c.getColumnIndex("condj")), c.getInt(c.getColumnIndex("motj")), c.getInt(c.getColumnIndex("jogandoj")) != 0, c.getDouble(c.getColumnIndex("valor"))));
            tvCaixa.setText(String.valueOf(c.getDouble(c.getColumnIndex("caixa"))));

            c.moveToNext();
        }

        c.close();
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getBaseContext(),
                R.layout.spinner_layout,
                jogadors);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout);
        spinner.setAdapter(arrayAdapter);
        selecionarJogador();

    }

    private void selecionarJogador() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                Jogador jogador = null;
                if (!jogadors.isEmpty()) {
                    jogador = jogadors.get(pos);
                }
                tvPos.setText(String.valueOf(jogador.getPosicao()));
                tvMot.setText(String.valueOf(jogador.getMotivacao()));
                tvValor.setText(String.valueOf(jogador.getValor()));
                tvHab.setText(String.valueOf(jogador.getHabilidade()));
                tvCon.setText(String.valueOf(jogador.getCondicionamento()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    public void treinarJogador(View v) {

        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Treinamento");
        dialog.setMessage("O treinamento do jogador tem um custo de 10.000 R$ para o condicionamento e \n" +
                "15.000 R$ para treinar a habildade do jogador.");
        dialog.setNegativeButton("Treinar habilidade", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Jogador j = null;
                if (!jogadors.isEmpty()) {
                    j = jogadors.get(pos);
                }
                if (j != null) {
                    db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
                    db.execSQL("UPDATE jogador SET habilidade = habilidade +5 WHERE nome = '" + j.getNome() + "' AND clubeId = (SELECT clubeId from clube WHERE nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "');");
                    db.execSQL("UPDATE clube SET caixa =caixa-10000 WHERE nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'");

                    db.close();
                    Toast.makeText(getApplicationContext(), "Jogador " + j.getNome() + " treinado com sucesso!", Toast.LENGTH_SHORT).show();
                    carregarJogadores();
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhum jogador disponivel!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setPositiveButton("Treinar condicionamento", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Jogador j = jogadors.get(pos);
                if (j != null) {
                    db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
                    db.execSQL("UPDATE jogador SET condicionamento = condicionamento +7 WHERE nome = '" + j.getNome() + "' AND clubeId = (SELECT clubeId from clube WHERE nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "');");
                    db.execSQL("UPDATE clube SET caixa =caixa-15000 WHERE nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'");

                    db.close();
                    Toast.makeText(getApplicationContext(), "Jogador " + j.getNome() + " treinado com sucesso!", Toast.LENGTH_SHORT).show();
                    carregarJogadores();
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhum jogador disponivel!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.create();
        dialog.show();

    }

    public void venderJogador(View v) {
        Jogador j = null;
        if (!jogadors.isEmpty()) {
            j = jogadors.get(pos);
        }
        if (j != null) {
            db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
            db.execSQL("DELETE FROM jogador WHERE nome = '" + j.getNome() + "' AND clubeId = (SELECT clubeId from clube WHERE nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "');");
            db.execSQL("UPDATE clube SET caixa =" + j.getValor() + "+caixa WHERE nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'");

            db.close();
            Toast.makeText(this, "Jogador " + j.getNome() + " vendido com sucesso!", Toast.LENGTH_SHORT).show();
            carregarJogadores();
        } else {
            Toast.makeText(this, "Todos os jogadores foram vendidos!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuInicio:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.menuTime:
                startActivity(new Intent(getApplicationContext(), Time.class));
                finish();
                break;
            case R.id.menuLoja:
                startActivity(new Intent(getApplicationContext(), Loja.class));
                finish();
                break;
            case R.id.menuCampeonato:
                startActivity(new Intent(getApplicationContext(), Campeonato.class));
                finish();
                break;
            case R.id.menuEstadio:
                startActivity(new Intent(getApplicationContext(), Estadio.class));
                finish();
                break;
            case R.id.menuJogos:
                startActivity(new Intent(getApplicationContext(), JogosActivity.class));
                finish();
                break;
            case R.id.menuResultados:
                startActivity(new Intent(getApplicationContext(), ResultadosActivity.class));
                finish();
                break;

            case R.id.menuJogador:
                startActivity(new Intent(getApplicationContext(), JogadorActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
