package com.example.sila.brasfootmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Model.*;

public class Time extends AppCompatActivity {
    private ListView lvGoleiros, lvAtacantes, lvMeioCampos, lvDefensores;
    ArrayList<Jogador> defensores, atacantes, meiocampos, goleiros;
    private Button btSalvar;
    private TextView tvQuantGoleiros, tvQuantDefensores, tvQuantAtacantes, tvQuantMeiocampos;
    private static final String ARQUIVO_PREFERENCIAS = "arquivo_preferencias";
    private SQLiteDatabase db;
    private TextView tvNomeClube;
    private AlertDialog.Builder dialog;
    private int emCampo;
    private int posJ;
    private boolean colocar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        lvGoleiros = (ListView) findViewById(R.id.lvGoleiros);
        lvAtacantes = (ListView) findViewById(R.id.lvAtacantes);
        lvMeioCampos = (ListView) findViewById(R.id.lvMeioCampos);
        lvDefensores = (ListView) findViewById(R.id.lvDefensores);
        tvNomeClube = (TextView) findViewById(R.id.tvNomeClube);
        tvNomeClube.setText(getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", ""));
        tvQuantAtacantes = (TextView) findViewById(R.id.tvQuantAtacantes);
        tvQuantDefensores = (TextView) findViewById(R.id.tvQuantDefensores);
        tvQuantGoleiros = (TextView) findViewById(R.id.tvQuantGoleiros);
        tvQuantMeiocampos = (TextView) findViewById(R.id.tvQuantMeioCampos);
        carregarGoleiros();
        carregarAtacantes();
        carregarMeiCampos();
        carregarDefensores();
    }

    public void carregarDefensores() {
        defensores = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT jogador.habilidade as habilidadej,jogador.nome as nomej,jogador.posicao as posicaoj,jogador.condicionamento as condj,jogador.motivacao as motj,jogador.jogando as jogandoj FROM jogador INNER JOIN clube ON jogador.clubeId = clube.clubeId WHERE posicao =" + Jogador.DEFENSOR + " AND clube.nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            defensores.add(new Jogador(c.getInt(c.getColumnIndex("habilidadej")), c.getString(c.getColumnIndex("nomej")), c.getInt(c.getColumnIndex("posicaoj")), c.getInt(c.getColumnIndex("condj")), c.getInt(c.getColumnIndex("motj")), c.getInt(c.getColumnIndex("jogandoj")) != 0));
            c.moveToNext();
        }

        c.close();
        lvDefensores.setAdapter(new ArrayAdapter<>(getBaseContext(),
                R.layout.custom_item_listview_layout,
                android.R.id.text1,
                defensores));
        lvDefensores.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        atualizarJogadores(defensores, lvDefensores, tvQuantDefensores);
        selecionarDefensores();
    }

    public void carregarAtacantes() {
        atacantes = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT jogador.habilidade as habilidadej,jogador.nome as nomej,jogador.posicao as posicaoj,jogador.condicionamento as condj,jogador.motivacao as motj,jogador.jogando as jogandoj FROM jogador INNER JOIN clube ON jogador.clubeId = clube.clubeId WHERE posicao =" + Jogador.ATACANTE + " AND clube.nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            atacantes.add(new Jogador(c.getInt(c.getColumnIndex("habilidadej")), c.getString(c.getColumnIndex("nomej")), c.getInt(c.getColumnIndex("posicaoj")), c.getInt(c.getColumnIndex("condj")), c.getInt(c.getColumnIndex("motj")), c.getInt(c.getColumnIndex("jogandoj")) != 0));
            c.moveToNext();
        }
        c.close();

        lvAtacantes.setAdapter(new ArrayAdapter<>(getBaseContext(),
                R.layout.custom_item_listview_layout,
                android.R.id.text1,
                atacantes));
        lvAtacantes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        atualizarJogadores(atacantes, lvAtacantes, tvQuantAtacantes);
        selecionarAtacantes();
    }

    public void carregarGoleiros() {
        goleiros = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT jogador.habilidade as habilidadej,jogador.nome as nomej,jogador.posicao as posicaoj,jogador.condicionamento as condj,jogador.motivacao as motj,jogador.jogando as jogandoj FROM jogador INNER JOIN clube ON jogador.clubeId = clube.clubeId WHERE posicao =" + Jogador.GOLEIRO + " AND clube.nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            goleiros.add(new Jogador(c.getInt(c.getColumnIndex("habilidadej")), c.getString(c.getColumnIndex("nomej")), c.getInt(c.getColumnIndex("posicaoj")), c.getInt(c.getColumnIndex("condj")), c.getInt(c.getColumnIndex("motj")), c.getInt(c.getColumnIndex("jogandoj")) != 0));
            c.moveToNext();
        }
        c.close();
        lvGoleiros.setAdapter(new ArrayAdapter<>(getBaseContext(),
                R.layout.custom_item_listview_layout,
                android.R.id.text1,
                goleiros));
        lvGoleiros.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        atualizarJogadores(goleiros, lvGoleiros, tvQuantGoleiros);
        selecionarGoleiro();
    }

    public void carregarMeiCampos() {
        meiocampos = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT jogador.habilidade as habilidadej,jogador.nome as nomej,jogador.posicao as posicaoj,jogador.condicionamento as condj,jogador.motivacao as motj,jogador.jogando as jogandoj FROM jogador INNER JOIN clube ON jogador.clubeId = clube.clubeId WHERE posicao =" + Jogador.MEIOCAMPO + " AND clube.nome = '" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            meiocampos.add(new Jogador(c.getInt(c.getColumnIndex("habilidadej")), c.getString(c.getColumnIndex("nomej")), c.getInt(c.getColumnIndex("posicaoj")), c.getInt(c.getColumnIndex("condj")), c.getInt(c.getColumnIndex("motj")), c.getInt(c.getColumnIndex("jogandoj")) != 0));
            c.moveToNext();
        }
        c.close();
        lvMeioCampos.setAdapter(new ArrayAdapter<>(getBaseContext(),
                R.layout.custom_item_listview_layout,
                android.R.id.text1,
                meiocampos));
        lvMeioCampos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        atualizarJogadores(meiocampos, lvMeioCampos, tvQuantMeiocampos);
        selecionarMeicampos();
    }

    public void selecionarGoleiro() {
        lvGoleiros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo(position, goleiros, 1, lvGoleiros, tvQuantGoleiros);
            }
//
        });
    }

    public void selecionarAtacantes() {
        lvAtacantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo(position, atacantes, 4, lvAtacantes, tvQuantAtacantes);
            }
        });
    }

    public void selecionarMeicampos() {
        lvMeioCampos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo(position, meiocampos, 4, lvMeioCampos, tvQuantMeiocampos);
            }
        });
    }

    public void selecionarDefensores() {
        lvDefensores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo(position, defensores, 2, lvDefensores, tvQuantDefensores);
            }
//
        });
    }

    public void salvarTime(View view) {
        ArrayList<Jogador> jogadors = new ArrayList<>();
        jogadors.addAll(goleiros);
        jogadors.addAll(atacantes);
        jogadors.addAll(meiocampos);
        jogadors.addAll(defensores);
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT clubeId from clube WHERE" +
                " nome='" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'", null);
        c.moveToFirst();
        int meuClubeId = c.getInt(c.getColumnIndex("clubeId"));
        c.close();

        for (Jogador j : jogadors) {
            if (j.isJogando()) {
                db.execSQL("UPDATE jogador SET jogando = '1' WHERE nome = '" + j.getNome() + "' AND clubeId = '" + meuClubeId + "';");
            } else {
                db.execSQL("UPDATE jogador SET jogando = '0' WHERE nome = '" + j.getNome() + "' AND clubeId = '" + meuClubeId + "';");
            }
        }
        db.close();
        Intent intent = new Intent(getApplicationContext(), Campeonato.class);
        finish();
        startActivity(intent);

    }

    public void atualizarJogadores(ArrayList<Jogador> jogadores, ListView listView, TextView textView) {
        int quant = 0;
        //Conta quantos jogadores da posicao escolhida estao em campo
        for (Jogador jog : jogadores) {
            if (jog.isJogando()) {
                quant++;
            }
        }
        int i = 0;
        for (Jogador j : jogadores) {
            if (j.isJogando()) {
                listView.setItemChecked(i, true);
            } else {
                listView.setItemChecked(i, false);
            }
            i++;
        }

        textView.setText(Integer.toString(quant));

    }

    public boolean dialogo(int posJogador, final ArrayList<Jogador> jogadores, final int max, final ListView listView, final TextView tv) {
        this.posJ = posJogador;
        emCampo = 0;
        //Conta quantos jogadores da posicao escolhida estao em campo
        for (Jogador jog : jogadores) {
            if (jog.isJogando()) {
                emCampo++;
            }
        }

        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Ficha de: " + jogadores.get(posJogador).getNome());
        dialog.setMessage("Condicionamento: " + jogadores.get(posJogador).getCondicionamento() + "\nHabilidade: " + jogadores.get(posJogador).getHabilidade() + "\nMotivação: " + jogadores.get(posJogador).getMotivacao() + "\nDeseja escalar ou retirar o jogador da ação?");

        dialog.setNegativeButton("Retirar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jogadores.get(posJ).setJogando(false);
                Toast.makeText(getApplicationContext(), "Jogador " + jogadores.get(posJ).getNome() + " retirado do campo!", Toast.LENGTH_LONG).show();
                atualizarJogadores(jogadores, listView, tv);
            }
        });


        dialog.setPositiveButton("Escalar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (emCampo < max) {
                    jogadores.get(posJ).setJogando(true);
                    Toast.makeText(getApplicationContext(), "Jogador " + jogadores.get(posJ).getNome() + " adicionado!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Numero de jogadores nesta posição excedido!", Toast.LENGTH_LONG).show();
                    atualizarJogadores(jogadores, listView, tv);
                }

            }
        });
        dialog.create();
        dialog.show();

        return colocar;
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
            case R.id.menuJogador:
                startActivity(new Intent(getApplicationContext(), JogadorActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
