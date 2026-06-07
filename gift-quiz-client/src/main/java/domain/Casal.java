package domain;

public final class Casal {

    private static Casal instance;

    private final Constanca constanca;
    private final Jorge jorge;

    private Casal(Constanca constanca, Jorge jorge) {
        this.constanca = constanca;
        this.jorge = jorge;
    }

    public static Casal criar(Constanca constanca, Jorge jorge) {
        if (instance != null) {
            throw new IllegalStateException("O casal ja existe.");
        }
        if (constanca == null || jorge == null) {
            throw new IllegalStateException("Primeiro e preciso criar a Constanca e o Jorge.");
        }
        instance = new Casal(constanca, jorge);
        return instance;
    }

    public static void reiniciar() {
        instance = null;
    }

    public static boolean existe() {
        return instance != null;
    }

    public static Casal atual() {
        return instance;
    }

    public Constanca constanca() {
        return constanca;
    }

    public Jorge jorge() {
        return jorge;
    }

    public boolean paraSempre() {
        return true;
    }
}
