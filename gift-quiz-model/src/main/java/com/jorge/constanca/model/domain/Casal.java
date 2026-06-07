package com.jorge.constanca.model.domain;

import java.util.List;
import java.util.Objects;

public class Casal {

    private final Jorge jorge;
    private final Constanca constanca;

    public Casal(Jorge jorge, Constanca constanca) {
        this.jorge = Objects.requireNonNull(jorge, "jorge");
        this.constanca = Objects.requireNonNull(constanca, "constanca");
    }

    public static Casal criarCanonico() {
        return new Casal(new Jorge(), Constanca.getInstance());
    }

    public Jorge jorge() {
        return jorge;
    }

    public Constanca constanca() {
        return constanca;
    }

    public SimulacaoRomantica simular() {
        int compatibilidade = Math.min(100,
                70
                        + (jorge.intensidadeDoAmor() / 5)
                        + (jorge.atencaoAoDetalhe() / 6)
                        + (jorge.paciencia() / 8));

        SimulacaoRomantica.Vibe vibe = constanca.gostaDeDetalhesFofos()
                ? SimulacaoRomantica.Vibe.FOFA
                : SimulacaoRomantica.Vibe.DELICADA;

        if (constanca.gostaDeCoisasPessoais() && jorge.atencaoAoDetalhe() >= 90) {
            vibe = SimulacaoRomantica.Vibe.PESSOAL;
        }

        SimulacaoRomantica.TipoEncontro encontroIdeal = jorge.paciencia() >= 85
                ? SimulacaoRomantica.TipoEncontro.PASSEIO_CALMO
                : SimulacaoRomantica.TipoEncontro.CAFE_E_CONVERSA;

        if (constanca.gostaDeDetalhesFofos()) {
            encontroIdeal = SimulacaoRomantica.TipoEncontro.CHA_E_SOBREMESA;
        }

        SimulacaoRomantica.TipoPresente presenteIdeal = switch (vibe) {
            case FOFA -> SimulacaoRomantica.TipoPresente.COISA_FOFA_E_PENSADA;
            case DELICADA -> SimulacaoRomantica.TipoPresente.JOIA_DELICADA;
            case PESSOAL -> SimulacaoRomantica.TipoPresente.CARTA_E_DETALHE;
        };

        return new SimulacaoRomantica(
                compatibilidade,
                vibe,
                encontroIdeal,
                presenteIdeal,
                List.of(
                        "detalhes fofinhos",
                        "cores suaves",
                        "coisas escolhidas com cuidado",
                        "um toque pessoal"
                )
        );
    }
}
