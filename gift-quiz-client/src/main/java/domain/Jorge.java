package domain;

public final class Jorge implements ApaixonadoPelaConstanca {

    private static Jorge instance;

    private Jorge() {
    }

    public static Jorge criar() {
        if (instance != null) {
            throw new IllegalStateException("Nao da para criar dois Jorges.");
        }
        instance = new Jorge();
        return instance;
    }

    public static void reiniciar() {
        instance = null;
    }

    public static boolean existe() {
        return instance != null;
    }

    public static Jorge atual() {
        return instance;
    }

    @Override
    public boolean ama(Constanca constanca) {
        return constanca != null;
    }
}
