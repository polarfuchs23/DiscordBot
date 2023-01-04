package de.joshuaschuenke.discordbot.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.joshuaschuenke.discordbot.youtubeapisupply.YoutubeLinkGetter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class CommandWtgCreate implements CommandInterface {

    private static final Logger LOG = LoggerFactory.getLogger(CommandWtgCreate.class);

    @Override
    public Mono<Void> react(final ChatInputInteractionEvent event) {
        return Mono.fromCallable(() -> {
                    var link = event.getOption("link")
                            .get().getValue()
                            .get().asString();
                    if (!link.contains("?v") && !link.contains("youtube")) {
                        link = YoutubeLinkGetter.getLinkByKeyword(link);
                    }
                    System.out.println("link: " + link);

                    final var r = event.getOption("red")
                            .flatMap(ApplicationCommandInteractionOption::getValue)
                            .map(ApplicationCommandInteractionOptionValue::asLong)
                            .orElse(24L);

                    final var g = event.getOption("green")
                            .flatMap(ApplicationCommandInteractionOption::getValue)
                            .map(ApplicationCommandInteractionOptionValue::asLong)
                            .orElse(116L);

                    final var b = event.getOption("blue")
                            .flatMap(ApplicationCommandInteractionOption::getValue)
                            .map(ApplicationCommandInteractionOptionValue::asLong)
                            .orElse(32L);

                    final OkHttpClient client = new OkHttpClient.Builder().build();
                    LOG.debug("created client");

                    final var jsonForBody = new JsonObject();
                    jsonForBody.addProperty("share", link);
                    jsonForBody.addProperty("bg_color", String.format("#%02x%02x%02x", r, g, b));
                    jsonForBody.addProperty("bg_opacity", "69");

                    LOG.trace(jsonForBody.toString());
                    final var formBody = RequestBody.create(jsonForBody.toString(), MediaType.parse("application/json"));
                    LOG.debug("created form body");

                    final var request = new Request.Builder()
                            .url(new HttpUrl.Builder()
                                    .scheme("https")
                                    .host("w2g.tv")
                                    .addPathSegments("/rooms/create.json")
                                    .build())
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .post(formBody)
                            .build();

                    LOG.debug("got request");
                    final var response = client.newCall(request).execute();
                    LOG.debug("response came back");

                    final var body = response.body();
                    if (body == null) {
                        throw new IllegalStateException("No body returned!");
                    }

                    final var responseString = body.string();
                    if (responseString.isBlank()) {
                        throw new IllegalStateException("Empty response!");
                    }

                    return JsonParser.parseString(responseString)
                            .getAsJsonObject()
                            .get("streamkey")
                            .getAsString();
                })
                .map(key -> "https://www.w2g.tv/" + key)
                .cache()
                .flatMap(wtgLink -> event
                        .reply()
                        .withEphemeral(false)
                        .withContent("You can access the room with this link:\n" + wtgLink)
                );
    }
}
