package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sila on 14/10/16.
 */

public class Clube {


    private List<Jogador> jogadores = new ArrayList<>();
    private List<Jogador> goleiros= new ArrayList<>();
    private List<Jogador> defensores = new ArrayList<>();
    private List<Jogador> meiocampos = new ArrayList<>();
    private List<Jogador> atacantes = new ArrayList<>();
    private int clubeId;



    private Estadio estadio;
    private String nome;
    private int forca;
    private int pontos =0;
    private int vitorias=0;
    private int empates=0;
    private int derrotas=0;
    private double caixa;
    public Clube(int clubeId, String nome) {
        this.clubeId = clubeId;
        this.nome = nome;
    }

    public Clube(int clubeId, String nome, int vitorias, int derrotas, int empates, int pontos) {
        this.clubeId = clubeId;
        this.nome = nome;
        this.pontos = pontos;
        this.vitorias = vitorias;
        this.empates = empates;
        this.derrotas = derrotas;
    }
    public Clube(int clubeId, String nome, int vitorias, int derrotas, int empates, int pontos,Estadio e) {
        this.clubeId = clubeId;
        this.nome = nome;
        this.pontos = pontos;
        this.vitorias = vitorias;
        this.empates = empates;
        this.derrotas = derrotas;
        this.estadio =e;
    }
    public int getForca(int pos) {
        forca=0;
        if (pos==Jogador.ATACANTE){
            for(Jogador j:atacantes){
                if(j.isJogando())
                forca+=j.getCondicionamento()*j.getHabilidade()*(j.getMotivacao()/100);
            }
            forca=forca/atacantes.size();
            }else if(pos==Jogador.DEFENSOR){
            for(Jogador j:defensores){
                if(j.isJogando())
                forca+=j.getCondicionamento()*j.getHabilidade()*(j.getMotivacao()/100);
            }
            forca=forca/defensores.size();
            }else if(pos==Jogador.MEIOCAMPO){
            for(Jogador j:meiocampos){
                if(j.isJogando())
                forca+=j.getCondicionamento()*j.getHabilidade()*(j.getMotivacao()/100);
            }
            forca=forca/meiocampos.size();
            }else if(pos==Jogador.GOLEIRO){
            for(Jogador j:goleiros){
                if(j.isJogando())
                forca+=j.getCondicionamento()*j.getHabilidade()*(j.getMotivacao()/100);
            }
            forca=forca/goleiros.size();
        }
        return forca;
    }
    public void classificarJogadores(){
        for (Jogador j:jogadores
                ) {
            if (j.getPosicao()==Jogador.DEFENSOR){
                defensores.add(j);
            }
            else
            if (j.getPosicao()==Jogador.ATACANTE){
                atacantes.add(j);
            }
            else
            if (j.getPosicao()==Jogador.MEIOCAMPO){
                meiocampos.add(j);
            }
            else
            if(j.getPosicao()==Jogador.GOLEIRO){
                goleiros.add(j);
            }

        }

    }
    public void setForca(int forca) {
        this.forca = forca;
    }

    public Clube(String nome) {
        this.nome = nome;
    }

    public Clube(String nome, List<Jogador> jogadores, Estadio e) {
        this.estadio = e;
        this.nome = nome;
        this.jogadores = jogadores;


    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
        classificarJogadores();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public int getClubeId() {
        return clubeId;
    }

    public int getEmpates() {
        return empates;
    }

    public void setEmpates(int empates) {
        this.empates = empates;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public double getCaixa() {
        return caixa;
    }

    public void setCaixa(double caixa) {
        this.caixa = caixa;
    }

    public void setClubeId(int clubeId) {
        this.clubeId = clubeId;
    }
}
