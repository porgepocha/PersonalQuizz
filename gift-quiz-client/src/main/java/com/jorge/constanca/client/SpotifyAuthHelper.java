package com.jorge.constanca.client;

public final class SpotifyAuthHelper {

    private static final String PKCE_DOCS_URL = "https://developer.spotify.com/documentation/web-api/tutorials/code-pkce-flow";
    private static final String TOP_ITEMS_URL = "https://developer.spotify.com/documentation/web-api/reference/get-users-top-artists-and-tracks";

    private SpotifyAuthHelper() {
    }

    public static String pkceDocsUrl() {
        return PKCE_DOCS_URL;
    }

    public static String topItemsUrl() {
        return TOP_ITEMS_URL;
    }
}
