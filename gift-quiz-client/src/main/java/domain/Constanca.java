package domain;

public final class Constanca extends Deusa {

    private static Constanca instance;

    private Constanca() {
    }

    public static Constanca criar() {
        if (instance != null) {
            throw new IllegalStateException("Nao da para criar duas Constancas.");
        }
        instance = new Constanca();
        return instance;
    }

    public static void reiniciar() {
        instance = null;
    }

    public static boolean existe() {
        return instance != null;
    }

    public static Constanca atual() {
        return instance;
    }

    @Override
    public String nome() {
        return "Constanca";
    }
}
