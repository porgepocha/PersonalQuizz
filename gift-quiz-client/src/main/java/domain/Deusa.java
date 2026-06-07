package domain;

public abstract class Deusa implements Perfeicao {

    @Override
    public int nivelPerfeicao() {
        return 100;
    }

    public boolean eUnica() {
        return true;
    }
}
