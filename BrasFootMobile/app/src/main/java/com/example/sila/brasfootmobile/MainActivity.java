package com.example.sila.brasfootmobile;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Clube;
import Model.Estadio;
import Model.Jogador;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private TextView tvJogadores;
    private static final String ARQUIVO_PREFERENCIAS = "arquivo_preferencias";
    private ArrayList<Clube> clubes = new ArrayList<>();
    private String nomeArquivo = "jogos.txt";
    private Button btContinuar;

    public void criarTabelas() {
        db.execSQL("CREATE TABLE IF NOT EXISTS clube (clubeId  INTEGER NOT NULL PRIMARY KEY, forca  INTEGER,nome  TEXT,pontos INTEGER,vitorias INTEGER,derrotas INTEGER,empates INTEGER,caixa REAL);");
        db.execSQL("CREATE TABLE  IF NOT EXISTS estadio (estadioId  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,capacidade  INTEGER,nome  TEXT, precoEntrada  REAL,precoExpansao  REAL,clubeId  INTEGER NOT NULL,CONSTRAINT  FK_possui_clube  FOREIGN KEY ( clubeId ) REFERENCES  clube  ( clubeId ) ON DELETE No Action ON UPDATE No Action);");
        db.execSQL("CREATE TABLE  IF NOT EXISTS jogador(jogadorId  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,posicao  INTEGER,jogando  INTEGER,motivacao  INTEGER,habilidade  INTEGER,condicionamento  INTEGER,nome  TEXT,valor REAL,clubeId  INTEGER NOT NULL,lojaId  INTEGER,CONSTRAINT  FK_pertence_clube  FOREIGN KEY ( clubeId ) REFERENCES  clube  ( clubeId ) ON DELETE No Action ON UPDATE No Action,CONSTRAINT  FK_vende_jogador  FOREIGN KEY ( lojaId ) REFERENCES  loja  ( lojaId ) ON DELETE No Action ON UPDATE No Action);");
        db.execSQL("CREATE TABLE  IF NOT EXISTS jogo(jogoId  INTEGER NOT NULL PRIMARY KEY,golsLocal  INTEGER, golsVisitante  INTEGER,lucro  REAL,vencedor  INTEGER,estadioId  INTEGER,clubeId  INTEGER,CONSTRAINT  FK_contem_jogo  FOREIGN KEY ( estadioId ) REFERENCES  campeonato  ( estadioId ) ON DELETE No Action ON UPDATE No Action,CONSTRAINT  FK_Visitante_clube  FOREIGN KEY ( clubeId ) REFERENCES  clube  ( clubeId ) ON DELETE No Action ON UPDATE No Action,CONSTRAINT  FK_Local_clube  FOREIGN KEY ( clubeId ) REFERENCES  clube  ( clubeId ) ON DELETE No Action ON UPDATE No Action) ;");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvJogadores = (TextView) findViewById(R.id.tvJogadores);
        btContinuar = (Button) findViewById(R.id.btContinuar);
        if (!getSharedPreferences(nomeArquivo, 0).contains("clube"))
            btContinuar.setEnabled(true);


    }

    public void novoJogo(View v) {
        this.deleteDatabase("foot");
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        criarTabelas();
        gerarJogadores();
        deleteFile(nomeArquivo);
        getSharedPreferences(ARQUIVO_PREFERENCIAS, 0).edit().putInt("fase", 0).apply();
        Intent intent = new Intent(getApplicationContext(), EscolherClube.class);
        finish();
        startActivity(intent);
        db.close();
    }

    public void gerarJogadores() {
        double caixaInicial = 100000;

        //lista de clubes
        Clube botafogo = new Clube(1, "Botafogo");
        Clube fluminense = new Clube(2, "Fluminense");
        Clube figueirense = new Clube(3, "Figueirense");
        Clube avai = new Clube(4, "Avaí");
        Clube flamengo = new Clube(5, "Flamengo");
        Clube vasco = new Clube(6, "Vasco");
        Clube corinthians = new Clube(7, "Corinthians");
        Clube saoPaulo = new Clube(8, "São Paulo");
        Clube gremio = new Clube(9, "Grêmio");
        Clube pernambuco = new Clube(10, "Pernambuco");
        Clube havai = new Clube(11, "Havaí");
        Clube internacional = new Clube(12, "Internacional");
        Clube botaFogo = new Clube(13, "Bota Fogo");
        Clube santos = new Clube(14, "Santos");
        Clube palmeiras = new Clube(15, "Palmeiras");
        Clube bahia = new Clube(16, "Bahia");
        Clube fortaleza = new Clube(17, "Fortaleza");
        Clube brasilia = new Clube(18, "Brasilia");
        Clube oeste = new Clube(19, "Oeste");
        Clube saoJose = new Clube(20, "São José");
        Clube bocaJunior = new Clube(21, "Boca Júnior");
        Clube atleticoIbirama = new Clube(22, "Atlético de Ibirama");
        //Clube interDeLages = new Clube(23, "Inter de Lages");

        ;

        Estadio e = new Estadio(1000, "Maracanã", 50, 50, 1);

        clubes.add(botafogo);
        clubes.add(fluminense);
        clubes.add(figueirense);
        clubes.add(avai);
        clubes.add(atleticoIbirama);
        clubes.add(flamengo);
        clubes.add(vasco);
        clubes.add(corinthians);
        clubes.add(saoPaulo);
        clubes.add(gremio);
        clubes.add(pernambuco);
        clubes.add(havai);
        clubes.add(internacional);
        clubes.add(botaFogo);
        clubes.add(santos);
        clubes.add(palmeiras);
        clubes.add(bahia);
        clubes.add(fortaleza);
        clubes.add(brasilia);
        clubes.add(oeste);
        clubes.add(saoJose);
        clubes.add(bocaJunior);
        //clubes.add(interDeLages);
        ArrayList<Jogador> jogadores;
        for (Clube c : clubes) {
            c.setCaixa(caixaInicial);
            jogadores = new ArrayList<>();
            //int numeroAletorio =(int)(Math.random()*30)+20;

            //Goleiros
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Rondinelli", Jogador.GOLEIRO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Valdivia", Jogador.GOLEIRO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 20000 + 80000)));

            //Atacantes
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Rivelino", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Riquelme", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Romario", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Edson", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "David Luiz", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Bernard", Jogador.ATACANTE, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 20000 + 80000)));
            //Defensor
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Luiz Gustavo", Jogador.DEFENSOR, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Anderson", Jogador.DEFENSOR, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Fred", Jogador.DEFENSOR, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Alex Sandro", Jogador.DEFENSOR, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, false, Math.round(Math.random() * 20000 + 80000)));
            //Meiocampos
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Sergio", Jogador.MEIOCAMPO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Mario", Jogador.MEIOCAMPO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Oscar", Jogador.MEIOCAMPO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)));
            jogadores.add(new Jogador((int) (Math.random() * 30) + 20, "Andre", Jogador.MEIOCAMPO, (int) (Math.random() * 30) + 20, (int) (Math.random() * 30) + 20, true, Math.round(Math.random() * 20000 + 80000)
            ));
            c.setJogadores(jogadores);

        }


        int jogand;
        //Insercao dos clubes e dos jogadores no banco


        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement("INSERT INTO clube (clubeId,nome,caixa) VALUES (?,?,?);");
        for (Clube club : clubes) {

            stmt.bindLong(1, club.getClubeId());
            stmt.bindString(2, club.getNome());
            stmt.bindDouble(3, club.getCaixa());
            long entryID = stmt.executeInsert();
            stmt.clearBindings();

        }
        db.setTransactionSuccessful();
        db.endTransaction();

        db.close();

        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        db.beginTransaction();

        SQLiteStatement st = db.compileStatement("INSERT INTO estadio (capacidade,nome, precoEntrada,precoExpansao,clubeId)VALUES(?,?,?,?,?)");
        for (Clube c : clubes) {

            st.bindLong(1, e.getCapacidade());
            st.bindString(2, e.getNome());
            st.bindDouble(3, e.getPrecoEntrada());
            st.bindDouble(4, e.getPrecoExpansao());
            st.bindLong(5, c.getClubeId());


            long entryID = st.executeInsert();
            st.clearBindings();

        }
        db.setTransactionSuccessful();
        db.endTransaction();

        db.close();
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        db.beginTransaction();
        SQLiteStatement s = db.compileStatement("INSERT INTO jogador(posicao,jogando,motivacao,habilidade,condicionamento,nome,clubeId,valor)VALUES (?,?,?,?,?,?,?,?);");


        for (Clube clube : clubes) {

            for (Jogador j : clube.getJogadores()) {
                if (j.isJogando()) {
                    jogand = 1;
                } else {
                    jogand = 0;
                }
                s.bindLong(1, j.getPosicao());
                s.bindLong(2, jogand);
                s.bindLong(3, j.getMotivacao());
                s.bindLong(4, j.getHabilidade());
                s.bindLong(5, j.getCondicionamento());
                s.bindString(6, j.getNome());
                s.bindLong(7, clube.getClubeId());
                s.bindDouble(8, j.getValor());
                long entryID = s.executeInsert();
                s.clearBindings();

            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        db.close();
    }

    public void continuar(View view) {
        Intent intent = new Intent(getApplicationContext(), Campeonato.class);
        finish();
        startActivity(intent);
    }


}
