package com.jorge.constanca.client;

import com.jorge.constanca.model.MusicTasteSnapshot;
import com.jorge.constanca.model.QuizCatalog;
import com.jorge.constanca.model.QuizOption;
import com.jorge.constanca.model.QuizQuestion;
import com.jorge.constanca.model.QuizSubmissionRequest;
import com.jorge.constanca.model.QuizSubmissionResponse;
import com.jorge.constanca.model.SpotifyTrack;
import com.jorge.constanca.model.TextAnswer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GiftQuizClientApp extends Application {

    private final AudioManager audioManager = new AudioManager();
    private final RemoteSubmissionClient remoteSubmissionClient = new RemoteSubmissionClient();
    private final SpotifyPkceClient spotifyPkceClient = new SpotifyPkceClient();
    private final LocalSubmissionBackupStore localSubmissionBackupStore = new LocalSubmissionBackupStore();
    private final LoveSimulation loveSimulation = new LoveSimulation();
    private final List<AppMusicTrack> appMusicTracks = AppMusicCatalog.availableTracks();
    private MusicTasteSnapshot spotifySnapshot;
    private String selectedBackgroundTrackLabel = AppMusicCatalog.defaultTrack().title();
    private String selectedSpotifyTrackLabel;
    private String selectedSpotifyUrl;

    private Stage stage;
    private Scene scene;
    private QuizState quizState;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.stage = primaryStage;
            this.scene = new Scene(new BorderPane(), 980, 720);

            var stylesheet = getClass().getResource("/styles/app.css");
            if (stylesheet != null) {
                this.scene.getStylesheets().add(stylesheet.toExternalForm());
            }

            stage.setScene(scene);
            stage.setTitle("Quiz do Presente");
            showWelcomeScreen();
            stage.show();
        } catch (Exception exception) {
            showStartupError(primaryStage);
        }
    }

    private void showWelcomeScreen() {
        audioManager.playBackgroundLoop(getClass());

        Label title = new Label("Para ti");
        title.getStyleClass().add("title");

        Label body = new Label("Queria perceber melhor aquilo de que gostas.");
        body.setWrapText(true);
        body.getStyleClass().add("body-text");
        body.setTextAlignment(TextAlignment.CENTER);

        Label musicHint = new Label(spotifySnapshot == null ? "Spotify desligado" : "Spotify ligado");
        musicHint.getStyleClass().add("music-hint");
        musicHint.setWrapText(true);
        musicHint.setTextAlignment(TextAlignment.CENTER);
        musicHint.setAlignment(Pos.CENTER);
        musicHint.setMaxWidth(540);

        Button startButton = new Button("Comecar o quiz");
        startButton.getStyleClass().add("primary-button");
        startButton.setOnAction(event -> {
            quizState = new QuizState(QuizCatalog.romanticGiftQuiz());
            showQuestionScreen();
        });

        Button moreButton = new Button("Mais");
        moreButton.getStyleClass().add("secondary-button");
        moreButton.setOnAction(event -> showExtrasScreen());

        HBox actions = new HBox(12, startButton, moreButton);
        actions.setAlignment(Pos.CENTER);

        VBox content = new VBox(18, title, body, musicHint, actions);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(36));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private void showExtrasScreen() {
        Label title = new Label("Mais");
        title.getStyleClass().add("title");

        Button photosButton = new Button("Fotos");
        photosButton.getStyleClass().add("secondary-button");
        photosButton.setOnAction(event -> showPhotosScreen());

        Button spotifyButton = new Button("Musica");
        spotifyButton.getStyleClass().add("secondary-button");
        spotifyButton.setOnAction(event -> showSpotifyScreen());

        Button simulationButton = new Button("Simulacao");
        simulationButton.getStyleClass().add("secondary-button");
        simulationButton.setOnAction(event -> showSimulationScreen());

        Button backButton = new Button("Voltar");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(event -> showWelcomeScreen());

        VBox content = new VBox(14, title, photosButton, spotifyButton, simulationButton, backButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(36));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private void showSimulationScreen() {
        Label eyebrow = new Label("Simulacao");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Criar-nos");
        title.getStyleClass().add("title");

        Label body = new Label("Primeiro cria a Constanca, depois cria o Jorge e no fim cria o casal.");
        body.getStyleClass().add("body-text");
        body.setWrapText(true);
        body.setTextAlignment(TextAlignment.CENTER);

        Canvas canvas = new Canvas(420, 250);
        StackPane canvasFrame = new StackPane(canvas);
        canvasFrame.getStyleClass().add("simulation-frame");

        Label status = new Label("Ainda nao criaste ninguem.");
        status.getStyleClass().add("body-text");
        status.setWrapText(true);
        status.setTextAlignment(TextAlignment.CENTER);
        status.setMaxWidth(520);

        Label loveNote = new Label("");
        loveNote.getStyleClass().add("simulation-note");
        loveNote.setWrapText(true);
        loveNote.setTextAlignment(TextAlignment.CENTER);
        loveNote.setMaxWidth(520);

        Runnable refresh = () -> {
            drawSimulation(canvas);
            if (loveSimulation.hasCasal()) {
                loveNote.setText("Eu amo-te imenso. Vamos ficar juntos para sempre.");
            } else {
                loveNote.setText("");
            }
        };

        Button createConstancaButton = new Button("Criar Constanca");
        createConstancaButton.getStyleClass().add("secondary-button");
        createConstancaButton.setOnAction(event -> {
            LoveSimulationResult result = loveSimulation.createConstanca();
            status.setText(result.message());
            refresh.run();
        });

        Button createJorgeButton = new Button("Criar Jorge");
        createJorgeButton.getStyleClass().add("secondary-button");
        createJorgeButton.setOnAction(event -> {
            LoveSimulationResult result = loveSimulation.createJorge();
            status.setText(result.message());
            refresh.run();
        });

        Button createCoupleButton = new Button("Criar casal");
        createCoupleButton.getStyleClass().add("primary-button");
        createCoupleButton.setOnAction(event -> {
            LoveSimulationResult result = loveSimulation.createCasal();
            status.setText(result.message());
            refresh.run();
        });

        Button resetButton = new Button("Recomecar");
        resetButton.getStyleClass().add("secondary-button");
        resetButton.setOnAction(event -> {
            loveSimulation.reset();
            status.setText("Voltei tudo ao inicio.");
            refresh.run();
        });

        Button backButton = new Button("Voltar");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(event -> showExtrasScreen());

        HBox rowOne = new HBox(12, createConstancaButton, createJorgeButton);
        rowOne.setAlignment(Pos.CENTER);

        HBox rowTwo = new HBox(12, createCoupleButton, resetButton, backButton);
        rowTwo.setAlignment(Pos.CENTER);

        VBox content = new VBox(18, eyebrow, title, body, canvasFrame, status, loveNote, rowOne, rowTwo);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(32));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(28));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
        refresh.run();
    }

    private void showPhotosScreen() {
        Label title = new Label("Fotografias");
        title.getStyleClass().add("title");

        Label body = new Label("Algumas de que gosto muito.");
        body.getStyleClass().add("body-text");
        body.setWrapText(true);

        HBox galleryRow = new HBox(16);
        galleryRow.getStyleClass().add("gallery-row");
        for (PhotoMoodboardItem item : PhotoMoodboardCatalog.defaultGallery()) {
            galleryRow.getChildren().add(createPhotoCard(item));
        }

        ScrollPane galleryScroll = new ScrollPane(galleryRow);
        galleryScroll.setFitToHeight(true);
        galleryScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        galleryScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        galleryScroll.getStyleClass().add("gallery-scroll");

        Button backButton = new Button("Voltar");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(event -> showExtrasScreen());

        VBox content = new VBox(18, title, body, galleryScroll, backButton);
        content.setPadding(new Insets(30));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(28));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private StackPane createPhotoCard(PhotoMoodboardItem item) {
        StackPane card = new StackPane();
        card.getStyleClass().add("photo-card");
        card.setPrefSize(220, 340);

        ImageView imageView = loadImage(item.resourcePath());
        if (imageView != null) {
            card.getChildren().add(imageView);
        } else {
            StackPane placeholder = new StackPane();
            placeholder.getStyleClass().add("photo-placeholder");
            Label placeholderText = new Label(item.title() + "\nColoca aqui a foto correspondente");
            placeholderText.getStyleClass().add("photo-placeholder-text");
            placeholderText.setTextAlignment(TextAlignment.CENTER);
            placeholderText.setWrapText(true);
            placeholder.getChildren().add(placeholderText);
            card.getChildren().add(placeholder);
        }

        VBox overlay = new VBox(6);
        overlay.setAlignment(Pos.BOTTOM_LEFT);
        overlay.setPadding(new Insets(16));
        overlay.getStyleClass().add("photo-overlay");

        Label title = new Label(item.title());
        title.getStyleClass().add("photo-title");
        overlay.getChildren().add(title);

        StackPane.setAlignment(overlay, Pos.BOTTOM_LEFT);
        card.getChildren().add(overlay);
        return card;
    }

    private ImageView loadImage(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return null;
            }
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(220);
            imageView.setFitHeight(340);
            imageView.setPreserveRatio(false);
            return imageView;
        } catch (Exception ignored) {
            return null;
        }
    }

    private void showSpotifyScreen() {
        Label eyebrow = new Label("Musica");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Musica de fundo");
        title.getStyleClass().add("title");

        Label body = new Label("Podes mudar a musica daqui ou abrir outra no Spotify.");
        body.getStyleClass().add("body-text");
        body.setWrapText(true);
        body.setTextAlignment(TextAlignment.CENTER);

        Label appMusicStatus = new Label("Na app: " + selectedBackgroundTrackLabel);
        appMusicStatus.getStyleClass().add("body-text");
        appMusicStatus.setWrapText(true);
        appMusicStatus.setTextAlignment(TextAlignment.CENTER);
        appMusicStatus.setMaxWidth(560);

        VBox appTracksBox = new VBox(10);
        populateAppTracks(appTracksBox, appMusicStatus);

        Label status = new Label(formatSpotifyStatus());
        status.getStyleClass().add("body-text");
        status.setWrapText(true);
        status.setTextAlignment(TextAlignment.CENTER);
        status.setMaxWidth(560);

        Button resumeAppButton = new Button("Retomar a da app");
        resumeAppButton.getStyleClass().add("secondary-button");
        resumeAppButton.setOnAction(event -> {
            audioManager.resumeCurrentTrack(getClass());
            selectedSpotifyUrl = null;
            selectedSpotifyTrackLabel = null;
            appMusicStatus.setText("Na app: " + selectedBackgroundTrackLabel);
            status.setText(formatSpotifyStatus());
        });

        Button openSpotifyButton = new Button("Abrir no Spotify");
        openSpotifyButton.getStyleClass().add("secondary-button");
        openSpotifyButton.setDisable(selectedSpotifyUrl == null || selectedSpotifyUrl.isBlank());
        openSpotifyButton.setOnAction(event -> {
            if (selectedSpotifyUrl != null && !selectedSpotifyUrl.isBlank()) {
                getHostServices().showDocument(selectedSpotifyUrl);
            }
        });

        VBox tracksBox = new VBox(10);
        populateSpotifyTracks(tracksBox, status, openSpotifyButton);

        Button connectSpotifyButton = new Button("Ligar Spotify");
        connectSpotifyButton.getStyleClass().add("primary-button");
        connectSpotifyButton.setOnAction(event -> {
            status.setText("A ligar...");
            connectSpotifyButton.setDisable(true);

            CompletableFuture
                    .supplyAsync(this::connectSpotify)
                    .thenAccept(snapshot -> Platform.runLater(() -> {
                        spotifySnapshot = snapshot;
                        status.setText(formatSpotifyStatus());
                        populateSpotifyTracks(tracksBox, status, openSpotifyButton);
                        connectSpotifyButton.setDisable(false);
                    }))
                    .exceptionally(error -> {
                        Platform.runLater(() -> {
                            status.setText(readableError(error));
                            populateSpotifyTracks(tracksBox, status, openSpotifyButton);
                            connectSpotifyButton.setDisable(false);
                        });
                        return null;
                    });
        });

        Button backButton = new Button("Voltar");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(event -> showExtrasScreen());

        HBox links = new HBox(12, resumeAppButton, connectSpotifyButton, openSpotifyButton, backButton);
        links.setAlignment(Pos.CENTER);

        VBox content = new VBox(18, eyebrow, title, body, appMusicStatus, appTracksBox, status, tracksBox, links);
        content.setPadding(new Insets(32));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(28));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private void showQuestionScreen() {
        QuizQuestion question = quizState.currentQuestion();

        Label step = new Label("Pergunta " + (quizState.currentQuestionIndex() + 1) + " de " + quizState.totalQuestions());
        step.getStyleClass().add("eyebrow");

        Label questionMood = new Label(question.allowsFreeText()
                ? "Podes escrever o que quiseres."
                : "Escolhe a opcao que fizer mais sentido.");
        questionMood.getStyleClass().add("music-hint");

        ProgressBar progressBar = new ProgressBar((double) quizState.currentQuestionIndex() / (double) quizState.totalQuestions());
        progressBar.getStyleClass().add("progress");
        progressBar.setPrefWidth(320);

        Label prompt = new Label(question.prompt());
        prompt.getStyleClass().add("question-title");
        prompt.setWrapText(true);

        VBox optionsBox = new VBox(14);
        if (question.options().isEmpty() && question.allowsFreeText()) {
            TextArea textArea = new TextArea();
            textArea.setPromptText(question.freeTextPlaceholder());
            textArea.setWrapText(true);
            textArea.setPrefRowCount(5);
            textArea.setMaxWidth(Double.MAX_VALUE);

            Button nextButton = new Button("Continuar");
            nextButton.getStyleClass().add("primary-button");
            nextButton.setOnAction(event -> {
                quizState.answerCurrentQuestion(null, textArea.getText());
                if (quizState.isComplete()) {
                    showSubmittingScreen();
                    submitQuiz();
                } else {
                    showQuestionScreen();
                }
            });

            optionsBox.getChildren().addAll(textArea, nextButton);
        } else {
            for (QuizOption option : question.options()) {
                Button optionButton = new Button(option.label());
                optionButton.getStyleClass().add("option-button");
                optionButton.setMaxWidth(Double.MAX_VALUE);
                optionButton.setOnAction(event -> {
                    quizState.answerCurrentQuestion(option.id(), null);
                    if (quizState.isComplete()) {
                        showSubmittingScreen();
                        submitQuiz();
                    } else {
                        showQuestionScreen();
                    }
                });
                optionsBox.getChildren().add(optionButton);
            }
        }

        VBox content = new VBox(18, step, questionMood, progressBar, prompt, optionsBox);
        content.setPadding(new Insets(34));
        content.getStyleClass().add("card");
        VBox.setVgrow(optionsBox, Priority.ALWAYS);

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(36));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private void showSubmittingScreen() {
        Label title = new Label("A guardar respostas...");
        title.getStyleClass().add("title");

        Label body = new Label("So um instante.");
        body.getStyleClass().add("body-text");
        body.setWrapText(true);
        body.setTextAlignment(TextAlignment.CENTER);

        ProgressBar bar = new ProgressBar(ProgressBar.INDETERMINATE_PROGRESS);
        bar.setPrefWidth(320);

        VBox content = new VBox(18, title, body, bar);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(36));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private void submitQuiz() {
        String participantName = "Constanca";

        CompletableFuture
                .supplyAsync(() -> sendSubmissionSafely(participantName, quizState.selectedOptionIds(), quizState.textAnswers()))
                .thenAccept(result -> Platform.runLater(() -> showSubmissionOutcomeScreen(participantName, result)))
                .exceptionally(error -> {
                    Platform.runLater(() -> showErrorScreen(readableError(error)));
                    return null;
                });
    }

    private SubmissionAttemptResult sendSubmissionSafely(String participantName, List<String> selectedOptionIds, List<TextAnswer> textAnswers) {
        QuizSubmissionRequest request = new QuizSubmissionRequest(
                participantName,
                selectedOptionIds,
                textAnswers,
                ClientConfig.CLIENT_VERSION
        );

        try {
            QuizSubmissionResponse response = remoteSubmissionClient.sendSubmission(participantName, selectedOptionIds, textAnswers);
            return SubmissionAttemptResult.remote(response);
        } catch (Exception exception) {
            try {
                return SubmissionAttemptResult.localBackup(
                        localSubmissionBackupStore.saveBackup(request),
                        readableError(exception)
                );
            } catch (Exception backupException) {
                throw new IllegalStateException(readableError(exception), backupException);
            }
        }
    }

    private void showSubmissionOutcomeScreen(String participantName, SubmissionAttemptResult result) {
        if (result.remoteSaved()) {
            showResultScreen(participantName, result.response());
            return;
        }

        showLocalBackupScreen(result);
    }

    private void showResultScreen(String participantName, QuizSubmissionResponse response) {
        audioManager.stopBackground();

        Label eyebrow = new Label("Feito");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Obrigado, " + participantName + ".");
        title.getStyleClass().add("title");

        Label body = new Label(response.sweetMessage());
        body.getStyleClass().add("body-text");
        body.setWrapText(true);
        body.setTextAlignment(TextAlignment.CENTER);

        Label note = new Label("Ja ficou.");
        note.getStyleClass().add("body-text");
        note.setWrapText(true);
        note.setTextAlignment(TextAlignment.CENTER);

        Label postscript = new Label("Obrigado.");
        postscript.getStyleClass().add("music-hint");
        postscript.setWrapText(true);
        postscript.setTextAlignment(TextAlignment.CENTER);

        Button restartButton = new Button("Voltar ao inicio");
        restartButton.getStyleClass().add("primary-button");
        restartButton.setOnAction(event -> showWelcomeScreen());

        HBox actions = new HBox(12, restartButton);
        actions.setAlignment(Pos.CENTER);

        VBox content = new VBox(16, eyebrow, title, body, note, postscript, actions);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(36));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private void showLocalBackupScreen(SubmissionAttemptResult result) {
        Label title = new Label("As respostas ficaram guardadas neste computador");
        title.getStyleClass().add("title");

        String pathText = result.localBackupPath() == null ? "" : result.localBackupPath().toAbsolutePath().toString();
        Label body = new Label(
                "Nao consegui enviarlas agora para o servidor.\n\n"
                        + "Guardei tudo aqui:\n"
                        + pathText
        );
        body.getStyleClass().add("body-text");
        body.setWrapText(true);
        body.setTextAlignment(TextAlignment.CENTER);

        Label note = new Label(result.errorMessage() == null || result.errorMessage().isBlank()
                ? "Tenta outra vez mais daqui a pouco."
                : "Erro: " + result.errorMessage());
        note.getStyleClass().add("music-hint");
        note.setWrapText(true);
        note.setTextAlignment(TextAlignment.CENTER);

        Button retryButton = new Button("Voltar ao inicio");
        retryButton.getStyleClass().add("secondary-button");
        retryButton.setOnAction(event -> showWelcomeScreen());

        VBox content = new VBox(18, title, body, note, retryButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(36));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private void showErrorScreen(String errorMessage) {
        Label title = new Label("Nao foi possivel enviar as respostas");
        title.getStyleClass().add("title");

        Label body = new Label(
                "Nao consegui guardar as respostas agora.\n\n"
                        + "Tenta outra vez daqui a bocadinho."
        );
        body.getStyleClass().add("body-text");
        body.setWrapText(true);

        Label note = new Label(errorMessage == null || errorMessage.isBlank() ? "Erro inesperado." : errorMessage);
        note.getStyleClass().add("music-hint");
        note.setWrapText(true);
        note.setTextAlignment(TextAlignment.CENTER);

        Button retryButton = new Button("Voltar ao inicio");
        retryButton.getStyleClass().add("secondary-button");
        retryButton.setOnAction(event -> showWelcomeScreen());

        VBox content = new VBox(18, title, body, note, retryButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("card");

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(36));
        root.getStyleClass().add("screen");
        setRootWithFade(root);
    }

    private void showStartupError(Stage primaryStage) {
        Label title = new Label("A app nao abriu como devia");
        title.getStyleClass().add("title");

        TextArea details = new TextArea("Houve um problema a abrir a app.\nFecha e volta a tentar.");
        details.setEditable(false);
        details.setWrapText(true);
        details.setPrefRowCount(8);

        VBox content = new VBox(18, title, details);
        content.setPadding(new Insets(24));

        Scene fallbackScene = new Scene(content, 760, 320);
        primaryStage.setTitle("Erro ao abrir");
        primaryStage.setScene(fallbackScene);
        primaryStage.show();
    }

    private void drawSimulation(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#fff7f5"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.web("#f1dfdb"));
        gc.fillRoundRect(18, 18, canvas.getWidth() - 36, canvas.getHeight() - 36, 28, 28);

        if (loveSimulation.hasConstanca()) {
            drawConstanca(gc, loveSimulation.hasCasal() ? 118 : 78, 82, 10);
        }

        if (loveSimulation.hasJorge()) {
            drawJorge(gc, loveSimulation.hasCasal() ? 224 : 266, 82, 10);
        }

        if (loveSimulation.hasCasal()) {
            drawHeart(gc, 178, 46, 10);
            gc.setFill(Color.web("#6a4c55"));
            gc.fillText("para sempre", 160, 220);
        }
    }

    private void drawConstanca(GraphicsContext gc, double x, double y, double size) {
        Color hair = Color.web("#221d20");
        Color skin = Color.web("#f6d7c9");
        Color dress = Color.web("#2b272a");
        Color blush = Color.web("#e9a8ae");

        drawFaceAndHair(gc, x, y, size, hair, skin, blush);
        fillBlock(gc, x + (2 * size), y + (4 * size), size, skin);
        fillBlock(gc, x + size, y + (5 * size), size, dress);
        fillBlock(gc, x + (2 * size), y + (5 * size), size, dress);
        fillBlock(gc, x + (3 * size), y + (5 * size), size, dress);
        fillBlock(gc, x, y + (6 * size), size, dress);
        fillBlock(gc, x + size, y + (6 * size), size, dress);
        fillBlock(gc, x + (2 * size), y + (6 * size), size, dress);
        fillBlock(gc, x + (3 * size), y + (6 * size), size, dress);
        fillBlock(gc, x + (4 * size), y + (6 * size), size, dress);
        fillBlock(gc, x + size, y + (7 * size), size, dress);
        fillBlock(gc, x + (3 * size), y + (7 * size), size, dress);
        fillBlock(gc, x + size, y + (8 * size), size, skin);
        fillBlock(gc, x + (3 * size), y + (8 * size), size, skin);
    }

    private void drawJorge(GraphicsContext gc, double x, double y, double size) {
        Color hair = Color.web("#201b1f");
        Color skin = Color.web("#f4d4c5");
        Color top = Color.web("#7e5666");
        Color blush = Color.web("#e6a1a8");

        drawFaceAndHair(gc, x, y, size, hair, skin, blush);
        fillBlock(gc, x + (2 * size), y + (4 * size), size, skin);
        fillBlock(gc, x + size, y + (5 * size), size, top);
        fillBlock(gc, x + (2 * size), y + (5 * size), size, top);
        fillBlock(gc, x + (3 * size), y + (5 * size), size, top);
        fillBlock(gc, x, y + (6 * size), size, top);
        fillBlock(gc, x + size, y + (6 * size), size, top);
        fillBlock(gc, x + (2 * size), y + (6 * size), size, top);
        fillBlock(gc, x + (3 * size), y + (6 * size), size, top);
        fillBlock(gc, x + (4 * size), y + (6 * size), size, top);
        fillBlock(gc, x + size, y + (7 * size), size, skin);
        fillBlock(gc, x + (3 * size), y + (7 * size), size, skin);
        fillBlock(gc, x + size, y + (8 * size), size, Color.web("#2d2630"));
        fillBlock(gc, x + (3 * size), y + (8 * size), size, Color.web("#2d2630"));
    }

    private void drawHeart(GraphicsContext gc, double x, double y, double size) {
        Color color = Color.web("#cf758d");
        fillBlock(gc, x + size, y, size, color);
        fillBlock(gc, x + (2 * size), y, size, color);
        fillBlock(gc, x + (4 * size), y, size, color);
        fillBlock(gc, x + (5 * size), y, size, color);
        fillBlock(gc, x, y + size, size, color);
        fillBlock(gc, x + size, y + size, size, color);
        fillBlock(gc, x + (2 * size), y + size, size, color);
        fillBlock(gc, x + (3 * size), y + size, size, color);
        fillBlock(gc, x + (4 * size), y + size, size, color);
        fillBlock(gc, x + (5 * size), y + size, size, color);
        fillBlock(gc, x + size, y + (2 * size), size, color);
        fillBlock(gc, x + (2 * size), y + (2 * size), size, color);
        fillBlock(gc, x + (3 * size), y + (2 * size), size, color);
        fillBlock(gc, x + (4 * size), y + (2 * size), size, color);
        fillBlock(gc, x + (2 * size), y + (3 * size), size, color);
        fillBlock(gc, x + (3 * size), y + (3 * size), size, color);
        fillBlock(gc, x + (2 * size), y + (4 * size), size, color);
        fillBlock(gc, x + (3 * size), y + (4 * size), size, color);
        fillBlock(gc, x + (2 * size), y + (5 * size), size, color);
    }

    private void drawFaceAndHair(GraphicsContext gc, double x, double y, double size, Color hair, Color skin, Color blush) {
        fillBlock(gc, x + size, y, size, hair);
        fillBlock(gc, x + (2 * size), y, size, hair);
        fillBlock(gc, x + (3 * size), y, size, hair);
        fillBlock(gc, x, y + size, size, hair);
        fillBlock(gc, x + size, y + size, size, skin);
        fillBlock(gc, x + (2 * size), y + size, size, skin);
        fillBlock(gc, x + (3 * size), y + size, size, skin);
        fillBlock(gc, x + (4 * size), y + size, size, hair);
        fillBlock(gc, x, y + (2 * size), size, hair);
        fillBlock(gc, x + size, y + (2 * size), size, skin);
        fillBlock(gc, x + (2 * size), y + (2 * size), size, skin);
        fillBlock(gc, x + (3 * size), y + (2 * size), size, skin);
        fillBlock(gc, x + (4 * size), y + (2 * size), size, hair);
        fillBlock(gc, x + size, y + (3 * size), size, blush);
        fillBlock(gc, x + (2 * size), y + (3 * size), size, skin);
        fillBlock(gc, x + (3 * size), y + (3 * size), size, blush);

        fillBlock(gc, x + size, y + (2 * size), size / 2, Color.web("#2b2327"));
        fillBlock(gc, x + (3 * size) + (size / 2), y + (2 * size), size / 2, Color.web("#2b2327"));
    }

    private void fillBlock(GraphicsContext gc, double x, double y, double size, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, size, size);
    }

    private void setRootWithFade(Parent root) {
        root.setOpacity(0);
        scene.setRoot(root);
        FadeTransition transition = new FadeTransition(Duration.millis(320), root);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    private MusicTasteSnapshot connectSpotify() {
        try {
            return spotifyPkceClient.connectAndFetch(url -> Platform.runLater(() -> getHostServices().showDocument(url)));
        } catch (Exception exception) {
            throw new IllegalStateException(exception.getMessage(), exception);
        }
    }

    private String formatSpotifyStatus() {
        if (spotifySnapshot == null) {
            if (ClientConfig.SPOTIFY_CLIENT_ID.isBlank()) {
                return "O Spotify ainda nao esta configurado.";
            }
            return "Ainda nao ligaste o Spotify.";
        }

        String artistas = spotifySnapshot.topArtists().isEmpty() ? "-" : String.join(", ", spotifySnapshot.topArtists());
        String musicaSpotify = selectedSpotifyTrackLabel == null
                ? "Ainda nao escolheste nenhuma no Spotify."
                : "No Spotify: " + selectedSpotifyTrackLabel;

        return "Artistas mais ouvidos: " + artistas + "\n\n" + musicaSpotify;
    }

    private String readableError(Throwable error) {
        Throwable cause = error.getCause() != null ? error.getCause() : error;
        return cause.getMessage() == null ? "Erro inesperado." : cause.getMessage();
    }

    private void populateSpotifyTracks(VBox tracksBox, Label statusLabel) {
        populateSpotifyTracks(tracksBox, statusLabel, null);
    }

    private void populateSpotifyTracks(VBox tracksBox, Label statusLabel, Button openSpotifyButton) {
        tracksBox.getChildren().clear();

        if (spotifySnapshot == null || spotifySnapshot.topTracks().isEmpty()) {
            return;
        }

        Label tracksTitle = new Label("Escolhe uma musica");
        tracksTitle.getStyleClass().add("eyebrow");
        tracksBox.getChildren().add(tracksTitle);

        for (SpotifyTrack track : spotifySnapshot.topTracks()) {
            Button trackButton = new Button(track.displayLabel());
            trackButton.getStyleClass().add("option-button");
            trackButton.setMaxWidth(Double.MAX_VALUE);
            trackButton.setOnAction(event -> {
                selectedSpotifyTrackLabel = track.displayLabel();
                selectedSpotifyUrl = track.spotifyUrl();
                if (openSpotifyButton != null) {
                    openSpotifyButton.setDisable(!track.hasSpotifyUrl());
                }

                if (track.hasSpotifyUrl()) {
                    audioManager.pauseBackground();
                    getHostServices().showDocument(track.spotifyUrl());
                }
                statusLabel.setText(formatSpotifyStatus());
            });
            tracksBox.getChildren().add(trackButton);
        }

        Label noPreview = new Label("As musicas do Spotify abrem na web. Quando quiseres, podes voltar a por a musica da app.");
        noPreview.getStyleClass().add("music-hint");
        noPreview.setWrapText(true);
        tracksBox.getChildren().add(noPreview);
    }

    private void populateAppTracks(VBox tracksBox, Label appMusicStatus) {
        tracksBox.getChildren().clear();

        Label tracksTitle = new Label("Na app");
        tracksTitle.getStyleClass().add("eyebrow");
        tracksBox.getChildren().add(tracksTitle);

        for (AppMusicTrack track : appMusicTracks) {
            Button trackButton = new Button(track.title());
            trackButton.getStyleClass().add("option-button");
            trackButton.setMaxWidth(Double.MAX_VALUE);
            trackButton.setOnAction(event -> {
                audioManager.playTrack(track, getClass());
                selectedBackgroundTrackLabel = track.title();
                selectedSpotifyTrackLabel = null;
                selectedSpotifyUrl = null;
                appMusicStatus.setText("Na app: " + selectedBackgroundTrackLabel);
            });
            tracksBox.getChildren().add(trackButton);
        }

        Label help = new Label("Se quiseres mais musicas aqui dentro, mete ficheiros .mp3 na pasta music.");
        help.getStyleClass().add("music-hint");
        help.setWrapText(true);
        tracksBox.getChildren().add(help);
    }
}
