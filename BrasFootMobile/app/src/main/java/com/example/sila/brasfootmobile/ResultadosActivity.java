package com.example.sila.brasfootmobile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import Model.*;

public class ResultadosActivity extends AppCompatActivity {
    private TextView tvResultados;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        tvResultados = (TextView) findViewById(R.id.tvResultados);
        mostrarResultados();
    }
    public void mostrarResultados(){
        String resultados="";
        db = openOrCreateDatabase("foot", MODE_PRIVATE, null);
        Cursor c =db.rawQuery("SELECT * FROM CLUBE",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            resultados+=c.getString(c.getColumnIndex("nome"))+"\n"+c.getInt(c.getColumnIndex("vitorias"))+"/"+c.getInt(c.getColumnIndex("derrotas"))+"/"+c.getInt(c.getColumnIndex("empates"))+" - Pontos: "+c.getInt(c.getColumnIndex("pontos"))+"\n";
            c.moveToNext();
        }
        c.close();
        db.close();
        tvResultados.setText(resultados);
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
