package com.jorge.constanca.model.domain;

public abstract class Deusa implements Perfeicao {

    @Override
    public final boolean unica() {
        return true;
    }

    @Override
    public final int nivelDeusa() {
        return 100;
    }
}
