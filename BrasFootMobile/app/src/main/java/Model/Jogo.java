package Model;


public class Jogo {
    public static final int VISITANTE = 1;
    public static final int LOCAL = 2;
    public static final int EMPATE = 3;

    private Clube visitante;
    private Clube local;
    private int golsLocal;
    private int golsVisitante;
    private double lucro;
    private int vencedor;
    private int posBola;
    private String andamento;

    public Jogo(Clube visitante, Clube local, int golsVisitante, int golsLocal, int vencedor) {
        this.visitante = visitante;
        this.local = local;
        this.golsLocal = golsLocal;
        this.golsVisitante = golsVisitante;
        this.vencedor = vencedor;
    }

    public Jogo(Clube visitante, Clube local) {
        this.visitante = visitante;
        this.local = local;
        this.golsLocal = 0;
        this.golsVisitante = 0;
    }

    public double calcularLucro() {
        Estadio e = local.getEstadio();
        return e.getCapacidade() * e.getPrecoEntrada();
    }

    public void resultado() {
        int chanceVisitante, chanceLocal;
        String jogadas = "";
        // meio campo comeca em 3
        //visitante ganha -1
        //local ganha +1
        // 4 = defesa visitante
        // 2 = defesa local
        // 5 = gol do local
        // 1 = gol do visitante
        posBola = 3;


        // gol local 5
        //defesa local 4
        //meio campo 3
        //defesa visitante 2
        //gol visitante 1
        for (int jogada = 1; jogada < 20; jogada++) {

            if (posBola == 3) {
                chanceLocal = (int) (local.getForca(Jogador.MEIOCAMPO) + Math.random() * 25);
                chanceVisitante = (int) (visitante.getForca(Jogador.MEIOCAMPO) + Math.random() * 25);
                //local dominou
                if (chanceLocal >= chanceVisitante) {
                    posBola--;
                    jogadas += "\n" + local.getNome() + " domina a bola no meio campo e avanca contra o " + visitante.getNome();
                }
                //visitante dominou
                else {
                    jogadas += "\n" + visitante.getNome() + " domina a bola meio campo e avanca contra o " + local.getNome();
                    posBola++;
                }

            } else if (posBola == 2) {
                chanceVisitante = (int) (visitante.getForca(Jogador.DEFENSOR) + Math.random() * 25);
                chanceLocal = (int) (local.getForca(Jogador.ATACANTE) + Math.random() * 25);
                //local dominou
                if (chanceLocal >= chanceVisitante) {
                    posBola--;
                    jogadas += "\nOs atacantes do " + local.getNome() + " roubam a bola dos defensores do " + visitante.getNome() + " e avançam pro gol.";
                }
                //visitante dominou
                else {
                    jogadas += "\nOs defensores do " + visitante.getNome() + " recuperam a bola dos atacantes do " + local.getNome();
                    posBola++;
                }
            } else if (posBola == 4) {
                chanceLocal = (int) (local.getForca(Jogador.DEFENSOR) + Math.random() * 25);
                chanceVisitante = (int) (visitante.getForca(Jogador.ATACANTE) + Math.random() * 25);
                //local dominou
                if (chanceLocal >= chanceVisitante) {
                    posBola--;
                    jogadas += "\nOs defensores do \" " + local.getNome() + " recuperam a bola dos atacantes do " + visitante.getNome();
                }
                //visitante dominou
                else {
                    jogadas += "\nOs atacantes do " + visitante.getNome() + " roubam a bola dos defensores do " + local.getNome() + " e avançam pro gol.";
                    posBola++;
                }
            } else if (posBola == 1) {
                chanceVisitante = (int) (visitante.getForca(Jogador.GOLEIRO) + Math.random() * 25);
                chanceLocal = (int) (local.getForca(Jogador.ATACANTE) + Math.random() * 25);
                //local fez gol
                if (chanceLocal >= chanceVisitante) {
                    posBola = 3;
                    this.golsLocal++;
                    jogadas += "\n" + local.getNome() + " faz gol contra o " + visitante.getNome();
                }
                //visitante dominou
                else
                    posBola++;
            } else if (posBola == 5) {
                chanceLocal = (int) (local.getForca(Jogador.GOLEIRO) + Math.random() * 25);
                chanceVisitante = (int) (visitante.getForca(Jogador.ATACANTE) + Math.random() * 25);
                //local fez gol
                if (chanceLocal >= chanceVisitante) {
                    posBola--;
                }
                //visitante dominou
                else {
                    jogadas += "\n" + visitante.getNome() + " faz gol contra o " + local.getNome();
                    posBola = 3;
                    this.golsVisitante++;
                }

            }

        }

        lesao();
        falta();

        if (golsLocal > golsVisitante) {
            this.vencedor = LOCAL;
            this.local.setVitorias(local.getVitorias()+1);
            this.visitante.setDerrotas(visitante.getDerrotas()+1);
            this.local.setPontos(local.getPontos()+3);
            for (Jogador l : local.getJogadores()){
                l.setMotivacao(l.getMotivacao()+5);
            }
            for (Jogador v : visitante.getJogadores()){
                v.setMotivacao(v.getMotivacao()-5);
            }
        } else if (golsVisitante > golsLocal) {
            this.vencedor = VISITANTE;
            this.local.setDerrotas(local.getDerrotas()+1);
            this.visitante.setVitorias(visitante.getVitorias()+1);
            this.visitante.setPontos(visitante.getPontos()+6);
            for (Jogador l : local.getJogadores()){
                l.setMotivacao(l.getMotivacao()-5);
            }
            for (Jogador v : visitante.getJogadores()){
                v.setMotivacao(v.getMotivacao()+5);
            }
        } else {
            this.vencedor = EMPATE;
            this.local.setEmpates(local.getEmpates()+1);
            this.visitante.setEmpates(visitante.getEmpates()+1);
            this.visitante.setPontos(visitante.getPontos()+1);
            this.local.setPontos(local.getPontos()+1);
        }

        andamento = jogadas;
        lucro = local.getEstadio().getPrecoEntrada()*local.getEstadio().getCapacidade();
        local.setCaixa(local.getCaixa()+lucro);

    }

    public String lesao() {
        Jogador lesionado = null;
        if (Math.random() * 20 < 1) {
            if ((int) Math.random() == 1) {
                int numeroJogador=(int) (Math.random() * 11);
                visitante.getJogadores().get(numeroJogador).setJogando(false);
                lesionado=visitante.getJogadores().get(numeroJogador);

            } else {
                int numeroJogador=(int) (Math.random() * 11);
                local.getJogadores().get(numeroJogador).setJogando(false);
                lesionado=local.getJogadores().get(numeroJogador);

            }
            return "O jogador "+lesionado.getNome()+" sofre uma lesão.\n";
        }
        return "";
    }

    public String falta() {
        Jogador ferido = null;
        if (Math.random() * 10 < 1) {
            if ((int) Math.random() == 1) {
                int numeroJogador=(int) (Math.random() * 11);
                visitante.getJogadores().get(numeroJogador).setJogando(false);
                ferido=visitante.getJogadores().get(numeroJogador);

            } else {
                int numeroJogador=(int) (Math.random() * 11);
                local.getJogadores().get(numeroJogador).setJogando(false);
                ferido=local.getJogadores().get(numeroJogador);

            }
            return "O jogador "+ferido.getNome()+" sofre uma falta.\n";
        }
        return "";
    }

    public Clube getVisitante() {
        return visitante;
    }

    public void setVisitante(Clube visitante) {
        this.visitante = visitante;
    }

    public Clube getLocal() {
        return local;
    }

    public void setLocal(Clube local) {
        this.local = local;
    }

    public int getGolsLocal() {
        return golsLocal;
    }

    public void setGolsLocal(int golsLocal) {
        this.golsLocal = golsLocal;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public double getLucro() {
        return lucro;
    }

    public void setLucro(double lucro) {
        this.lucro = lucro;
    }

    public int getVencedor() {
        return vencedor;
    }

    public void setVencedor(int vencedor) {
        this.vencedor = vencedor;
    }

    public String getAndamento() {
        return andamento;
    }

    public void setAndamento(String andamento) {
        this.andamento = andamento;
    }

    @Override
    public String toString() {
        return visitante.getNome() +
                " X " + local.getNome();
    }
}