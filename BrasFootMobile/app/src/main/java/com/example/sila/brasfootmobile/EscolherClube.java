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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Model.Clube;
import Model.Estadio;

public class EscolherClube extends AppCompatActivity {
    private Button btConfirmar;
    private SQLiteDatabase db;
    private TextView tvClubes, tvJogadores, tvCaixa;
    private Spinner lvClubes;
    private ArrayList<Clube> clubes = new ArrayList<>();
    private static final String ARQUIVO_PREFERENCIAS = "arquivo_preferencias";
    private String dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esolher_clube);
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        tvClubes = (TextView) findViewById(R.id.tvClubes);
        tvJogadores = (TextView) findViewById(R.id.tvJogadores);
        tvCaixa = (TextView) findViewById(R.id.tvCaixa);
        lvClubes = (Spinner) findViewById(R.id.spinner);
        btConfirmar = (Button) findViewById(R.id.btConfirmar);


        mostrarDados();
    }

    public void mostrarDados() {

        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM clube", null);
        String[] nomes = new String[cursor.getCount()];
        int size = 0;

        String texto = "";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            texto += cursor.getString(cursor.getColumnIndex("clubeId"));
            texto += ": " + cursor.getString(cursor.getColumnIndex("nome"));
            texto += ": " + cursor.getString(cursor.getColumnIndex("caixa"));
            nomes[size] = cursor.getString(cursor.getColumnIndex("nome"));
            size++;
            texto += "\n";

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getBaseContext(),R.layout.spinner_layout,
                nomes
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout);
        lvClubes.setAdapter(adapter);

        lvClubes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                dis = (String) lvClubes.getItemAtPosition(pos);
                btConfirmar.setClickable(true);
                db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
                Cursor cursorJ = db.rawQuery("SELECT jogador.nome as nomej,jogador.habilidade as habilidadej,jogador.motivacao as motivacaoj,jogador.condicionamento as condicionamentoj ,caixa FROM jogador INNER JOIN clube ON jogador.clubeId = clube.clubeId WHERE clube.nome = '" + dis + "'", null);
                String txt = "";

                cursorJ.moveToFirst();
                while (!cursorJ.isAfterLast()) {

                    txt += cursorJ.getString(cursorJ.getColumnIndex("nomej"));
                    txt += " : " + cursorJ.getString(cursorJ.getColumnIndex("habilidadej"));
                    txt += " : " + cursorJ.getString(cursorJ.getColumnIndex("motivacaoj"));
                    txt += " : " + cursorJ.getString(cursorJ.getColumnIndex("condicionamentoj"));
                    txt += "\n";
                    tvCaixa.setText(String.valueOf(cursorJ.getDouble(cursorJ.getColumnIndex("caixa"))));
                    cursorJ.moveToNext();

                }

                cursorJ.close();
                db.close();
                tvJogadores.setText(txt);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }

    public void escolher(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("clube", dis);
        editor.apply();
        Toast.makeText(getApplicationContext(), sharedPreferences.getString("clube", ""), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), Time.class);
        finish();
        startActivity(intent);
    }



}

