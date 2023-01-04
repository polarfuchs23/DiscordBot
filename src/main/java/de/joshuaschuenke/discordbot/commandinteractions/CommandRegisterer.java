package de.joshuaschuenke.discordbot.commandinteractions;

import de.joshuaschuenke.discordbot.commands.CommandDictionary;
import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.ApplicationCommandData;

public class CommandRegisterer {

    public static void registerCommands(final GatewayDiscordClient client) {
        final var restClient = client.getRestClient();
        final var guildIds = new Long[]{796689893694242817L, 743930006153199797L}; //796689893694242817L ist mein test server
        //final var guildId = 743930006153199797L

        for (final Long guildId : guildIds) {
            final var applicationId = restClient.getApplicationId().block();
            final var discordCommands = restClient.getApplicationService()
                    .getGuildApplicationCommands(applicationId, guildId)
                    .collectMap(ApplicationCommandData::name)
                    .block();

            for (final var name : CommandDictionary.commands.keySet()) {

                final var commandrequest = CommandDictionary.requests.get(name);

                restClient.getApplicationService()
                        .createGuildApplicationCommand(
                                applicationId,
                                guildId,
                                commandrequest
                        ).subscribe();
            }
        }
    }
}

