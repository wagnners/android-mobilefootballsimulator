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

import Model.Jogador;

public class Loja extends AppCompatActivity {
    private Button btComprar;
    private TextView tvJogadores;
    private String mostraJogadores;
    private ListView listView;
    private ArrayList<Jogador> jogadores = new ArrayList<>();
    private String[] nomes = new String[11];
    private AlertDialog dialog;
    public String dis = "";
    public String nome = "";
    private SQLiteDatabase db;
    private static final String ARQUIVO_PREFERENCIAS = "arquivo_preferencias";
    private String posicao = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja);
        tvJogadores = (TextView) findViewById(R.id.tvJogadores);
        listView = (ListView) findViewById(R.id.listView);
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);

        gerarJogadores();

    }

    public void gerarJogadores() {


        //Gerador de 20 jogadores aleatorios para cada clube
        // int numeroAletorio =(int)(Math.random()*30)+20;
        jogadores.add(new Jogador((int) (Math.random() * 33) + 21, "Saraiva", Jogador.GOLEIRO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 33) + 26, false, Math.round(Math.random() * 100000 + 50000)));
        jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Leonardo da Vinci", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 34) + 26, false, Math.round(Math.random() * 100000 + 100000)));
        jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Caleu", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 32) + 26, false, Math.round(Math.random() * 100000 + 100000)));
        jogadores.add(new Jogador((int) (Math.random() * 35) + 25, "Neymar", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 37) + 25, false, Math.round(Math.random() * 100000 + 200000)));
        jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Gandhi", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 100000 + 100000)));
        jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Galileu", Jogador.DEFENSOR, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 100000 + 50000)));
        jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Anitta", Jogador.DEFENSOR, (int) (Math.random() * 30) + 20, (int) (Math.random() * 36) + 24, false, Math.round(Math.random() * 100000 + 200000)));
        jogadores.add(new Jogador((int) (Math.random() * 33) + 27, "Pelé", Jogador.DEFENSOR, (int) (Math.random() * 30) + 20, (int) (Math.random() * 40) + 30, false, Math.round(Math.random() * 100000 + 300000)));
        jogadores.add(new Jogador((int) (Math.random() * 34) + 21, "Bartolomeu", Jogador.MEIOCAMPO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 100000 + 100000)));
        jogadores.add(new Jogador((int) (Math.random() * 35) + 21, "Picasso", Jogador.MEIOCAMPO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 100000 + 100000)));
        jogadores.add(new Jogador((int) (Math.random() * 33) + 22, "Simone e Simara", Jogador.MEIOCAMPO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 33) + 23, false, Math.round(Math.random() * 100000 + 50000)));
        int size = 0;


        for (Jogador j : jogadores) {
            int pos = j.getPosicao();
            if (pos == 1) {
                posicao = "DEFESA";
            } else if (pos == 2) {
                posicao = "MEIO CAMPO";

            } else if (pos == 3) {
                posicao = "ATAQUE";

            } else if (pos == 4) {
                posicao = "GOL";

            } else {

            }


            nomes[size] = j.getNome() + " - " + j.getValor() + " - " + posicao;

            size++;
        }


        mostrarJogadores();

    }


    public void mostrarJogadores() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                R.layout.custom_listview_singleitem1_loja,
                android.R.id.text1,
                nomes
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dis = (String) listView.getItemAtPosition(position);
                confirmar(dis);
            }

        });
    }

    public void confirmar(String dis) {
        nome = dis.substring(0, dis.indexOf("-") - 1);
        String preco = dis.substring(dis.indexOf("-") + 2, dis.lastIndexOf(" ") - 2);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Deseja Confirmar sua Compra para o jogador " + nome + " - " + posicao + "? Preço: R$ " + preco + "?");
        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Cursor c = db.rawQuery("SELECT clubeId, caixa from clube WHERE" +
                                " nome='" + getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).getString("clube", "") + "'", null);
                        c.moveToFirst();
                        int meuClubeId = c.getInt(c.getColumnIndex("clubeId"));
                        double caixa = c.getInt(c.getColumnIndex("caixa"));
                        c.close();
                        for (Jogador j : jogadores) {
                            if (nome.equalsIgnoreCase(j.getNome())) {
                                double novoCaixa = 0;
                                if (caixa >= j.getValor()) {
                                    novoCaixa = caixa - j.getValor();
                                    db.execSQL("INSERT INTO jogador (posicao,jogando,motivacao,habilidade,condicionamento,nome,valor,clubeId) VALUES(" + j.getPosicao() + "," + 0 + "," + j.getMotivacao() + "," + j.getHabilidade() + "," + j.getCondicionamento() + ",'" + j.getNome() + "'," + j.getValor() + "," + meuClubeId + ");");
                                    db.execSQL("UPDATE clube SET caixa = '" + novoCaixa + "' WHERE clubeId = '" + meuClubeId + "';");
                                    Toast.makeText(getApplicationContext(), j.getNome() + " adicionado", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(getApplicationContext(), j.getValor() + "Saldo insuficiente. Valor em Caixa: " + novoCaixa, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                    }
                });

        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "Ação NÃO confirmada!", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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