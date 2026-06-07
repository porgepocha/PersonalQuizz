package domain;

public class Constanca extends Deusa {
    private static Constanca instance;

    private Constanca() {
    }

    public static Constanca getInstance() {
        if (instance == null) {
            instance = new Constanca();
        }
        return instance;
    }
}