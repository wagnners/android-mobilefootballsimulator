package com.example.sila.brasfootmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Model.Clube;
import Model.Jogador;
import Model.Jogo;

public class Campeonato extends AppCompatActivity {
    private TextView tvClubes, tvAndamento, tvGols, tvLucro;
    private SQLiteDatabase db;
    private Button btJogar;
    private static final String ARQUIVO_PREFERENCIAS = "arquivo_preferencias";
    private String nomeArquivo = "jogos.txt";
    private ScrollView scAndamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campeonato);
        tvAndamento = (TextView) findViewById(R.id.tvAndamento);
        tvClubes = (TextView) findViewById(R.id.tvClubes);
        tvGols = (TextView) findViewById(R.id.tvGols);
        btJogar = (Button) findViewById(R.id.btJogar);
        tvLucro = (TextView) findViewById(R.id.tvLucro);
        scAndamento = (ScrollView) findViewById(R.id.scAndamento);
    }

    public void rodarPartida(View v) {
        scAndamento.setVisibility(View.VISIBLE);
        tvGols.setVisibility(View.VISIBLE);
        tvClubes.setVisibility(View.VISIBLE);
        tvAndamento.setVisibility(View.VISIBLE);
        tvLucro.setVisibility(View.VISIBLE);

        //recuperar os clubes do banco
        ArrayList<Clube> clubes = new ArrayList<>();
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT clube.clubeId as idC,clube.nome as nomec,capacidade ,estadio.nome as nomee,precoEntrada,precoExpansao,vitorias,derrotas,empates,pontos FROM clube INNER JOIN estadio ON clube.clubeId = estadio.clubeId", null);
        int indiceColunaId = cursor.getColumnIndex("idC");
        int indiceColunaNome = cursor.getColumnIndex("nomec");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Model.Estadio e = new Model.Estadio(cursor.getInt(cursor.getColumnIndex("capacidade")), cursor.getString(cursor.getColumnIndex("nomee")), cursor.getDouble(cursor.getColumnIndex("precoEntrada")), cursor.getDouble(cursor.getColumnIndex("precoExpansao")), cursor.getInt(cursor.getColumnIndex("idC")));
            clubes.add(new Clube(cursor.getInt(indiceColunaId), cursor.getString(indiceColunaNome),
                    cursor.getInt(cursor.getColumnIndex("vitorias")), cursor.getInt(cursor.getColumnIndex("derrotas")), cursor.getInt(cursor.getColumnIndex("empates")), cursor.getInt(cursor.getColumnIndex("pontos")), e));


            cursor.moveToNext();
        }
        cursor.close();
        db.close();


        ArrayList<Jogador> jogadores;
        Cursor cur = null;
        for (Clube c : clubes) {
            jogadores = new ArrayList<>();
            db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
            cur = db.rawQuery("SELECT * FROM jogador WHERE clubeId = " + c.getClubeId(), null);
            int indiceColunaHabilidade = cur.getColumnIndex("habilidade");
            int indiceNomeJogador = cur.getColumnIndex("nome");
            int indiceColunaPos = cur.getColumnIndex("posicao");
            int indiceColunaCond = cur.getColumnIndex("condicionamento");
            int indiceColunaMot = cur.getColumnIndex("motivacao");
            int indiceColunaJogando = cur.getColumnIndex("jogando");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                jogadores.add(new Jogador(cur.getInt(indiceColunaHabilidade), cur.getString(indiceNomeJogador), cur.getInt(indiceColunaPos), cur.getInt(indiceColunaCond), cur.getInt(indiceColunaMot), cur.getInt(indiceColunaJogando) != 0));
                cur.moveToNext();
            }
            c.setJogadores(jogadores);
        }
        cur.close();
        db.close();
        SharedPreferences s = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        int fase = s.getInt("fase", 0);
        String meuClube = s.getString("clube", "");
        Clube meu = null;
        for (Clube cc : clubes) {
            if (cc.getNome().equals(meuClube)) {
                meu = cc;
            }
        }


        Jogo j = null;
        //Clube local = null;
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        Cursor curs = db.rawQuery("SELECT * FROM clube", null);
        int quantidadeClubes = curs.getCount();
        curs.close();
        db.close();
        if (fase > quantidadeClubes - 1) {
            startActivity(new Intent(getApplicationContext(), ResultadosActivity.class));
            finish();
        } else {
            //Rotar clubes
            for (int i = 0; i < fase; i++) {
                clubes.add(1, clubes.get(clubes.size() - 1));
                clubes.remove(clubes.size() - 1);
            }
            int mid = quantidadeClubes / 2;
            ArrayList<Clube> l1 = new ArrayList<>();
            for (int i = 0; i < mid; i++) {
                l1.add(clubes.get(i));
            }


            ArrayList<Clube> l2 = new ArrayList<>();
            for (int i = clubes.size() - 1; i >= mid; i--) {
                l2.add(clubes.get(i));
            }

            for (int tId = 0; tId < l1.size(); tId++) {
                Clube local;
                Clube visitante;
                // Switch sides after each round
                if (fase + 1 % 2 == 1) {
                    local = l1.get(tId);
                    visitante = l2.get(tId);
                } else {
                    local = l2.get(tId);
                    visitante = l1.get(tId);
                }
                j = new Jogo(visitante, local);
                j.resultado();
                db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
                db.execSQL("UPDATE clube SET caixa = caixa+" + j.getLucro() / 2 + ", vitorias = '" + visitante.getVitorias() + "' ,derrotas = '" + visitante.getDerrotas() + "' ,empates = '" + visitante.getEmpates() + "',pontos = '" + visitante.getPontos() + "' WHERE clubeId = '" + visitante.getClubeId() + "'");
                db.execSQL("UPDATE clube SET caixa = caixa+" + j.getLucro() / 2 + ", vitorias = '" + local.getVitorias() + "' ,derrotas = '" + local.getDerrotas() + "' ,empates = '" + local.getEmpates() + "',pontos = '" + local.getPontos() + "' WHERE clubeId = '" + local.getClubeId() + "'");
                // db.execSQL("UPDATE clube SET caixa = caixa+" + j.getLucro() / 2 + " WHERE clubeId = '" + local.getClubeId() + "'");
                // db.execSQL("UPDATE clube SET caixa = caixa+" + j.getLucro() / 2 + " WHERE clubeId = '" + visitante.getClubeId() + "'");
                db.close();
                salvarJogo(j);
                if (local == meu || visitante == meu) {
                    tvClubes.setText(j.getVisitante().getNome() + "x" + j.getLocal().getNome());
                    tvAndamento.setText(j.getAndamento());
                    tvGols.setText(j.getGolsLocal() + "x" + j.getGolsVisitante());
                    tvLucro.setText(String.valueOf(j.getLucro()));
                }

            }


//            local = clubes.get(fase);
//            for (Clube visitante : clubes) {
//                if (local != visitante) {
//
//                    j = new Jogo(visitante, local);
//                    j.resultado();
//
//                    db.execSQL("UPDATE clube SET vitorias = '" + visitante.getVitorias() + "' ,derrotas = '" + visitante.getDerrotas() + "' ,empates = '" + visitante.getEmpates() + "',pontos = '" + visitante.getPontos() + "' WHERE clubeId = '" + visitante.getClubeId() + "'");
//                    db.execSQL("UPDATE clube SET vitorias = '" + local.getVitorias() + "' ,derrotas = '" + local.getDerrotas() + "' ,empates = '" + local.getEmpates() + "',pontos = '" + local.getPontos() + "' WHERE clubeId = '" + local.getClubeId() + "'");
//                    db.execSQL("UPDATE clube SET caixa = caixa+" + j.getLucro() / 2 + " WHERE clubeId = '" + local.getClubeId() + "'");
//                    db.execSQL("UPDATE clube SET caixa = caixa+" + j.getLucro() / 2 + " WHERE clubeId = '" + visitante.getClubeId() + "'");
//
//                    if (local == meu || visitante == meu) {
//                        tvClubes.setText(j.getVisitante().getNome() + "x" + j.getLocal().getNome());
//                        tvAndamento.setText(j.getAndamento());
//                        tvGols.setText(j.getGolsLocal() + "x" + j.getGolsVisitante());
//                        tvLucro.setText(String.valueOf(j.getLucro()));
//                    }
//                    salvarJogo(j);
//                }
//            }
            fase++;


            s.edit().putInt("fase", fase).apply();
        }
        curs.close();
        db.close();


    }

    public void salvarJogo(Jogo j) {

        String mensagem = j.getVisitante().getNome() + ";" + j.getLocal().getNome() + ";" + j.getGolsVisitante() + ";" + j.getGolsLocal() + ";" + j.getVencedor() + "\n";
        try {
            FileOutputStream fos = openFileOutput(nomeArquivo, MODE_APPEND);
            fos.write(mensagem.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
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
