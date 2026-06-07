package com.jorge.constanca.model;

import java.util.List;

public final class QuizCatalog {

    private QuizCatalog() {
    }

    public static List<QuizQuestion> romanticGiftQuiz() {
        return List.of(
                new QuizQuestion(
                        "bag-surprise",
                        "Se eu te aparecesse com uma prenda e dissesse 'abre isto so quando chegares a casa', o que te deixava mais curiosa?",
                        List.of(
                                new QuizOption("bag-cute", "Algo fofinho que gostes", "cute"),
                                new QuizOption("bag-wear", "Uma coisa mais chique ou roupa para usar", "luxury"),
                                new QuizOption("bag-personal", "Uma coisa mais pessoal ou feita por mim", "romantic"),
                                new QuizOption("bag-random", "Uma coisa bue niche escolhida especificamente para ti", "dreamy")
                        ),
                        false,
                        ""
                ),
                new QuizQuestion(
                        "keep-or-use",
                        "O que e que te gostas mais de receber?",
                        List.of(
                                new QuizOption("keep-memory", "Algo que possas guardar durante anos", "romantic"),
                                new QuizOption("use-daily", "Uma coisa para usar quase todos os dias", "luxury"),
                                new QuizOption("desk-room", "Uma coisa para ter no quarto", "cozy"),
                                new QuizOption("small-obsession", "Uma coisinha que seja relacionado a algo que gostes", "cute")
                        ),
                        false,
                        ""
                ),
                new QuizQuestion(
                        "where-it-fits",
                        "Onde e que gostavas mais que a prenda entrasse no teu dia a dia?",
                        List.of(
                                new QuizOption("fits-body", "No que tu vestes ou levo contigo", "luxury"),
                                new QuizOption("fits-room", "No teu quarto", "cozy"),
                                new QuizOption("fits-bag", "Na mala, no telemovel ou em pequenos detalhes", "cute"),
                                new QuizOption("fits-heart", "No coracao hehe", "romantic")
                        ),
                        false,
                        ""
                ),
                new QuizQuestion(
                        "instant-yes",
                        "Qual destas coisas tinha mais probabilidade de te fazer dizer logo 'eu queria isto'?",
                        List.of(
                                new QuizOption("yes-sanrio", "Uma coisa querida e fofinha tipo um peluche", "cute"),
                                new QuizOption("yes-jewel", "Algo mais caro tipo um perfume ou makeup", "luxury"),
                                new QuizOption("yes-film", "Uma coisa mais vintage tipo um vinyl", "cinema"),
                                new QuizOption("yes-personal", "Uma coisa mais nossa ou mais pessoal", "romantic")
                        ),
                        false,
                        ""
                ),
                new QuizQuestion(
                        "tiny-extra",
                        "Se a prenda viesse com um extra pequenino, qual e que ias achar mais querido?",
                        List.of(
                                new QuizOption("extra-letter", "Uma carta", "romantic"),
                                new QuizOption("extra-sweets", "Um doce ou snack", "cozy"),
                                new QuizOption("extra-charm", "Um charm, autocolante ou detalhe fofo", "cute"),
                                new QuizOption("extra-song", "Uma musica escolhida que me faz pensar em ti", "dreamy")
                        ),
                        false,
                        ""
                ),
                new QuizQuestion(
                        "dont-miss",
                        "O que e que faz uma prenda deixar de parecer so mais uma?",
                        List.of(
                                new QuizOption("miss-detail", "Ter detalhe e atencao", "romantic"),
                                new QuizOption("miss-style", "Ser algo que gostas imenso pessoalmente", "luxury"),
                                new QuizOption("miss-cute", "Ter qualquer coisa querida", "cute"),
                                new QuizOption("miss-story", "Vir com historia ou significado nosso", "cinema")
                        ),
                        false,
                        ""
                ),
                new QuizQuestion(
                        "write-perfect-gift",
                        "Algo que me queiras contar sobre a tua prenda ideal amor?",
                        List.of(),
                        true,
                        "Escreve aqui"
                ),
                new QuizQuestion(
                        "write-last-seen",
                        "Qual foi a ultima coisa que viste e pensaste 'eu gostava mesmo disto'?",
                        List.of(),
                        true,
                        "Escreve aqui"
                ),
                new QuizQuestion(
                        "write-obsession",
                        "Ha alguma personagem, cor, tema ou coisinha de que andes a gostar mais ultimamente?",
                        List.of(),
                        true,
                        "Escreve aqui"
                ),
                new QuizQuestion(
                        "write-dont-buy",
                        "Ha alguma coisa que eu deva mesmo evitar comprar-te?",
                        List.of(),
                        true,
                        "Opcional"
                ),
                new QuizQuestion(
                        "write-help-me",
                        "Se eu te tivesse de comprar uma prenda esta semana, que dica me davas para eu acertar bue em algo perfeito?",
                        List.of(),
                        true,
                        "Escreve aqui"
                ),
                new QuizQuestion(
                        "write-extra",
                        "Se quiseres, deixa-me aqui qualquer pista extra que aches que me ajude.",
                        List.of(),
                        true,
                        "Opcional"
                )
        );
    }
}
