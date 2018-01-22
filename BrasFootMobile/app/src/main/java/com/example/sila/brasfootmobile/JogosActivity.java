package com.example.sila.brasfootmobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Model.Clube;
import Model.Jogador;
import Model.Jogo;

public class JogosActivity extends AppCompatActivity {
    private String nomeArquivo = "jogos.txt", resultado;
    private ListView lvJogos;
    private ArrayList<Jogo> jogos;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos);

        lvJogos = (ListView) findViewById(R.id.lvJogosDoCampeonato);
        carregarJogos();

    }

    public void carregarJogos() {
        leJogo();
        lvJogos.setAdapter(new ArrayAdapter<>(getBaseContext(),
                R.layout.custom_listview_singleitem1,
                android.R.id.text1,
                jogos));
        selecionarJogo();
    }

    public void selecionarJogo() {
        lvJogos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Jogo jogo = jogos.get(position);
                String vencedor = "";
                if(jogo.getVencedor()==Jogo.LOCAL){
                    vencedor=jogo.getLocal().getNome();
                }else if(jogo.getVencedor()==Jogo.VISITANTE){
                    vencedor=jogo.getVisitante().getNome();
                }else {
                    vencedor= "Empate";
                }
                dialog = new AlertDialog.Builder(JogosActivity.this);
                dialog.setTitle(jogo.getVisitante().getNome() + " X " + jogo.getLocal().getNome());
                dialog.setMessage("Gols do " + jogo.getVisitante().getNome() + ": " + jogo.getGolsVisitante() +
                        "\nGols do " + jogo.getLocal().getNome() + ":" + jogo.getGolsLocal()+"\nVencedor: "+vencedor);
                dialog.create();
                dialog.show();
            }
        });
    }

    public void leJogo() {
        this.jogos = new ArrayList<>();
        String[] dados;
        try {
            FileInputStream fis = openFileInput(nomeArquivo);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String linha = reader.readLine();
            while (linha != null) {
                dados = linha.split(";");
                jogos.add(new Jogo(new Clube(dados[0]), new Clube(dados[1]), Integer.parseInt(dados[2]), Integer.parseInt(dados[3]), Integer.parseInt(dados[4])));
                linha = reader.readLine();
            }
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
