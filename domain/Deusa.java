package domain;

public abstract class Deusa {

    public static Deusa Deusa() {
        return new Deusa() {
            @Override
            public String regraPerfeicao() {
                return "Ser a Constança";
            }
        };
    }



}
