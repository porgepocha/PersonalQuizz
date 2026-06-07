package com.jorge.constanca.model.domain;

import java.util.List;

public final class Constanca extends Deusa {

    private static Constanca instance;

    private final String nome;
    private final List<String> gostosBase;
    private final boolean gostaDeDetalhesFofos;
    private final boolean gostaDeCoisasPessoais;

    public Constanca() {
        synchronized (Constanca.class) {
            if (instance != null) {
                throw new IllegalStateException("So pode existir uma unica Constanca.");
            }
            this.nome = "Constanca";
            this.gostosBase = List.of("sanrio", "cinema antigo", "detalhes fofos", "coisas com cuidado");
            this.gostaDeDetalhesFofos = true;
            this.gostaDeCoisasPessoais = true;
            instance = this;
        }
    }

    public static synchronized Constanca getInstance() {
        if (instance == null) {
            instance = new Constanca();
        }
        return instance;
    }

    @Override
    public String nome() {
        return nome;
    }

    public List<String> gostosBase() {
        return gostosBase;
    }

    public boolean gostaDeDetalhesFofos() {
        return gostaDeDetalhesFofos;
    }

    public boolean gostaDeCoisasPessoais() {
        return gostaDeCoisasPessoais;
    }
}
