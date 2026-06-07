package com.jorge.constanca.model.domain;

public interface ApaixonadoPelaConstanca {

    String nome();

    int intensidadeDoAmor();

    int atencaoAoDetalhe();

    default boolean totalmenteConquistado() {
        return intensidadeDoAmor() >= 100;
    }
}
