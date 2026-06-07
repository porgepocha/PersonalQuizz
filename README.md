# Gift Quiz Project

This workspace now contains a complete example of a cute Java project you can grow into a real gift recommender for your girlfriend.

## Project layout

- `gift-quiz-client`: JavaFX desktop quiz app
- `gift-quiz-server`: private Spring Boot backend
- `gift-quiz-model`: shared DTOs and quiz question catalog

There is also a playful OOP layer inside `gift-quiz-model` under:

- `com.jorge.constanca.model.domain`

That layer is the buildable version of your original `domain` sketch and is now used by the JavaFX app for welcome and result messages.

## New client screens

The JavaFX app now has four paths from the home screen:

- quiz
- couple simulator
- visual moodboard
- Spotify setup

The moodboard is designed for real photos of her, and the couple simulator uses the OOP domain classes more deeply.

The client only sends selected answers to the server. The actual gift recommendation logic lives on the server so it stays private.

## What this teaches you

- JavaFX layouts, scenes, styling, and buttons
- HTTP calls from Java with `HttpClient`
- JSON serialization with Jackson
- REST APIs with Spring Boot
- Persistence with a file-backed H2 database
- Separating public client code from private server logic
- Interfaces, inheritance, singletons, and composition through the couple simulation

## How to run locally

Start the backend first:

```powershell
mvn -pl gift-quiz-server spring-boot:run
```

In a second terminal, launch the JavaFX app:

```powershell
mvn -pl gift-quiz-client javafx:run
```

The client will send quiz submissions to `http://localhost:8080/api/submissions`.

## How the hidden logic works

- The client shows questions and sends only option IDs.
- The server calculates the top gift profile.
- The server stores the submission in H2.
- The client shows a sweet confirmation screen instead of the raw recommendation details.

## What to upgrade later

1. Move the backend from local machine to a small cloud server.
2. Add authentication to the admin endpoint.
3. Replace H2 with PostgreSQL.
4. Add images and real music files to the JavaFX app.
5. Package the client with `jpackage` instead of a plain jar for an easier user experience.

## How the original domain idea maps into the project

Your root-level `domain` folder looked like an early sketch of a mini romantic object model. Instead of deleting it, this project now includes a working version in the Maven build:

- `Deusa` is now an abstract class implementing `Perfeicao`
- `Constanca` is a singleton
- `Jorge` implements `ApaixonadoPelaConstanca`
- `Casal` composes `Jorge` and `Constanca`

That gives you a sandbox for learning:

- inheritance in `Constanca extends Deusa`
- interfaces in `Perfeicao` and `ApaixonadoPelaConstanca`
- singleton pattern in `Constanca.getInstance()`
- composition in `Casal`

If you want to play with it more, a nice next step is adding methods like:

- compatibility score
- simulated date planner
- random affectionate dialogue
- "best gift strategy" based on the quiz aura

## Audio note

The client includes an `AudioManager` that looks for:

- `gift-quiz-client/src/main/resources/audio/background.mp3`

If you add a file there, the app will try to play it on the welcome screen.

## Add her photos

To make the moodboard show the real photos, save them in:

- `gift-quiz-client/src/main/resources/photos/`

Use these names:

- `photo-1.jpg`
- `photo-2.jpg`
- `photo-3.jpg`
- `photo-4.jpg`
- `photo-5.jpg`

The app already has gallery cards ready for them.

There is also an import helper now:

```powershell
.\import-assets.ps1 -PhotosDirectory "C:\path\to\your\photos" -AudioFile "C:\path\to\your\song.mp3"
```

That script copies the five photos into the right resource folder and places the audio file into:

- `gift-quiz-client/src/main/resources/audio/`

See [ASSET_CHECKLIST.md](C:/Users/jorge/Desktop/Para%20o%20amor%20da%20minha%20vida/ASSET_CHECKLIST.md) for the quick checklist.

## Packaging the client

There is now a helper script at [package-client.ps1](C:/Users/jorge/Desktop/Para%20o%20amor%20da%20minha%20vida/package-client.ps1).

Run it from PowerShell:

```powershell
.\package-client.ps1
```

What it does:

- builds the JavaFX client jar
- runs `jpackage`
- creates a desktop-style app image under `dist/`

Before packaging for real, you will probably want to add:

- real photos in `gift-quiz-client/src/main/resources/photos/`
- a real `background.mp3`
- later, a custom app icon

## Best final-delivery flow

1. Confirm the Railway backend is online.
2. Drop in the real photos.
3. Add the music file.
4. Run `.\package-client.ps1`.
5. Open the packaged app from `dist/` and test one full quiz submission.

## Spotify later

There is a setup note in [SPOTIFY_INTEGRATION_NOTES.md](C:/Users/jorge/Desktop/Para%20o%20amor%20da%20minha%20vida/SPOTIFY_INTEGRATION_NOTES.md) explaining the right way to add Spotify taste signals later.

Short version:

- use Spotify login with PKCE
- request `user-top-read`
- treat Spotify as optional bonus input
- never ship a client secret inside the jar

## Private admin view

You can inspect saved submissions at:

- `GET http://localhost:8080/api/submissions`

That endpoint is intentionally simple for learning. Before sharing this beyond a personal project, protect it with authentication.
