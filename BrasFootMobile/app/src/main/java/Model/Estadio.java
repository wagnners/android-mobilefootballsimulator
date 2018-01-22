package Model;

/**
 * Created by sila on 29/10/16.
 */

public class Estadio {
    private int capacidade;
    private String nome;
    private double precoEntrada;
    private double precoExpansao;
    private int estadioid;

    public Estadio(int capacidade, String nome, double precoEntrada, double precoExpansao, int estadioid) {
        this.capacidade = capacidade;
        this.nome = nome;
        this.precoEntrada = precoEntrada;
        this.precoExpansao = precoExpansao;
        this.estadioid = estadioid;
    }

    public void aumentarCapacidade(int cap){
        this.capacidade+=cap;
    }
    public void aumentarPreco(double p){
        this.precoEntrada+= p;
    }
    public void diminuirPreco(double p){
        this.precoEntrada-= p;
    }

    public int getEstadioid() {
        return estadioid;
    }

    public void setEstadioid(int estadioid) {
        this.estadioid = estadioid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public double getPrecoEntrada() {
        return precoEntrada;
    }

    public void setPrecoEntrada(double precoEntrada) {
        this.precoEntrada = precoEntrada;
    }

    public double getPrecoExpansao() {
        return precoExpansao;
    }

    public void setPrecoExpansao(double precoExpansao) {
        this.precoExpansao = precoExpansao;
    }
}
