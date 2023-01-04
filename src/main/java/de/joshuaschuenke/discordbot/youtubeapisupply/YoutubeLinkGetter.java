package de.joshuaschuenke.discordbot.youtubeapisupply;

import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class YoutubeLinkGetter {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .build();

    public static String getLinkByKeyword(final String title) {
        final var request = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .scheme("https")
                        .host("youtube.googleapis.com")
                        .addPathSegments("youtube/v3/search")
                        .addQueryParameter("part", "snippet")
                        .addQueryParameter("maxResults", "1")
                        .addQueryParameter("key", System.getenv("API_TOKEN"))
                        .addQueryParameter("q", title)
                        .build())
                .get()
                .build();
        System.out.println("alive");
        final Response response;
        try {
            final var startTime = System.nanoTime();
            response = client.newCall(request).execute();
            final var duration = System.nanoTime() - startTime;
            String videoId = null;
            try {
                videoId = JsonParser.parseString(Objects.requireNonNull(response.body()).string())
                        .getAsJsonObject()
                        .getAsJsonArray("items")
                        .get(0)
                        .getAsJsonObject()
                        .getAsJsonObject("id")
                        .get("videoId")
                        .getAsString();
            } catch (final Exception e) {
                System.out.println("Youtube returned a unreadable JSON");
                e.printStackTrace();
            }
            System.out.println("Request took " + TimeUnit.NANOSECONDS.toMillis(duration) + "ms");

            return "https://www.youtube.com/watch?v=" + videoId;
        } catch (final IOException e) {
            System.out.println("Wasn't able to send and receive the GET request");
            e.printStackTrace();

            return "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        }
    }
}
