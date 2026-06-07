package com.jorge.constanca.model.domain;

public class Jorge implements ApaixonadoPelaConstanca {

    private final String nome;
    private final int intensidadeDoAmor;
    private final int atencaoAoDetalhe;
    private final int paciencia;

    public Jorge() {
        this("Jorge", 100, 92, 88);
    }

    public Jorge(String nome, int intensidadeDoAmor, int atencaoAoDetalhe, int paciencia) {
        this.nome = nome;
        this.intensidadeDoAmor = intensidadeDoAmor;
        this.atencaoAoDetalhe = atencaoAoDetalhe;
        this.paciencia = paciencia;
    }

    @Override
    public String nome() {
        return nome;
    }

    @Override
    public int intensidadeDoAmor() {
        return intensidadeDoAmor;
    }

    @Override
    public int atencaoAoDetalhe() {
        return atencaoAoDetalhe;
    }

    public int paciencia() {
        return paciencia;
    }
}
