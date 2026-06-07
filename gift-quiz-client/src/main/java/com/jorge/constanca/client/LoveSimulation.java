package com.jorge.constanca.client;

import domain.Casal;
import domain.Constanca;
import domain.Jorge;

public class LoveSimulation {

    private Constanca constanca;
    private Jorge jorge;
    private Casal casal;

    public LoveSimulation() {
        reset();
    }

    public LoveSimulationResult createConstanca() {
        if (constanca != null) {
            return new LoveSimulationResult(false, "Nao da para criar outra Constanca so pode existir uma pois ela e unica e perfeita.");
        }

        constanca = Constanca.criar();
        return new LoveSimulationResult(true, "Pronto. A Constanca ja esta criada.");
    }

    public LoveSimulationResult createJorge() {
        if (jorge != null) {
            return new LoveSimulationResult(false, "Tambem nao da para criar dois Jorges.");
        }

        jorge = Jorge.criar();
        return new LoveSimulationResult(true, "Pronto. O Jorge ja esta criado.");
    }

    public LoveSimulationResult createCasal() {
        if (casal != null) {
            return new LoveSimulationResult(false, "O casal ja esta feito.");
        }
        if (constanca == null && jorge == null) {
            return new LoveSimulationResult(false, "Primeiro cria a Constanca e o Jorge.");
        }
        if (constanca == null) {
            return new LoveSimulationResult(false, "Primeiro cria a Constanca.");
        }
        if (jorge == null) {
            return new LoveSimulationResult(false, "Primeiro cria o Jorge.");
        }
        if (!jorge.ama(constanca)) {
            return new LoveSimulationResult(false, "Este Jorge claramente nao esta a funcionar bem.");
        }

        casal = Casal.criar(constanca, jorge);
        return new LoveSimulationResult(true, "Agora sim. Ficamos juntos.");
    }

    public void reset() {
        Casal.reiniciar();
        Jorge.reiniciar();
        Constanca.reiniciar();
        casal = null;
        jorge = null;
        constanca = null;
    }

    public boolean hasConstanca() {
        return constanca != null;
    }

    public boolean hasJorge() {
        return jorge != null;
    }

    public boolean hasCasal() {
        return casal != null;
    }
}
