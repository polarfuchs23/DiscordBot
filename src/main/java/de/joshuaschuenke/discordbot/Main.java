package de.joshuaschuenke.discordbot;

import de.joshuaschuenke.discordbot.audiocommandsupply.AudioProviderAndPlayerProvider;
import de.joshuaschuenke.discordbot.commandinteractions.CommandListener;
import de.joshuaschuenke.discordbot.commandinteractions.CommandRegisterer;
import de.joshuaschuenke.discordbot.commands.CommandDictionary;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

public class Main {

    /*
    public static final LoadingCache<Long, ServerSettings> SETTINGS_CACHE = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public ServerSettings load(@NotNull final Long key) throws Exception {
                    final var settings = new ServerSettings(key);
                    settings.loadSettings();
                    return settings;
                }
            });*/

    public static void main(final String... args) {
        CommandDictionary.initialize();

        System.out.println("Starting bot...");
        final GatewayDiscordClient client = DiscordClientBuilder.create(System.getenv("BOT_TOKEN")).build()
                .login()
                .block();

        CommandRegisterer.registerCommands(client);

        AudioProviderAndPlayerProvider.initialize();

        client.on(ReadyEvent.class).flatMap(event -> Mono.fromRunnable(() -> {
            final User self = event.getSelf();
            System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
        })).subscribe();

        client.on(ChatInputInteractionEvent.class).flatMap(CommandListener::react).subscribe();
        while (true) {
        }
    }
}
