# Spotify Integration Notes

You can use Spotify to make the gift recommender smarter, but only if she logs in and agrees to share her listening data.

## Best safe approach

For a desktop JavaFX app:

1. Open Spotify login in the browser
2. Use Authorization Code with PKCE
3. Request the `user-top-read` scope
4. Read top tracks and top artists
5. Use those results as extra gift hints

## Why not put secrets in the jar

If you place a Spotify client secret inside the desktop app, it can be extracted. For an installed app, PKCE is the correct direction because it avoids shipping a secret in the client.

## Suggested use in this project

- keep the quiz as the main source of truth
- treat Spotify as an optional bonus signal
- do not block the app if Spotify is not connected

Example ideas:

- if her top artists are soft/cute pop, boost cute gift ideas
- if her playlists lean nostalgic or cinematic, boost vintage film style suggestions
- if there are many calm acoustic songs, boost cozy date-night gifts

## Practical implementation idea

- add a "Connect Spotify" button on the welcome screen
- after login, store only a short taste summary
- send the summary to the backend with the quiz answers
- let the backend merge quiz vibe plus music vibe

## Official docs to follow

- Authorization Code with PKCE
- Get User's Top Items

See:

- https://developer.spotify.com/documentation/web-api/tutorials/code-pkce-flow
- https://developer.spotify.com/documentation/web-api/reference/get-users-top-artists-and-tracks
