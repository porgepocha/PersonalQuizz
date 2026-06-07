package com.jorge.constanca.model.domain;

import java.util.List;

public record SimulacaoRomantica(
        int compatibilidade,
        Vibe vibe,
        TipoEncontro encontroIdeal,
        TipoPresente presenteIdeal,
        List<String> pistas
) {
    public enum Vibe {
        DELICADA,
        FOFA,
        PESSOAL
    }

    public enum TipoEncontro {
        PASSEIO_CALMO,
        CHA_E_SOBREMESA,
        CAFE_E_CONVERSA
    }

    public enum TipoPresente {
        CARTA_E_DETALHE,
        JOIA_DELICADA,
        COISA_FOFA_E_PENSADA
    }
}
