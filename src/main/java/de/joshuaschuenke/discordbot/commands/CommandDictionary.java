package de.joshuaschuenke.discordbot.commands;

import de.joshuaschuenke.discordbot.commands.blacklist.CommandAddForbiddenWord;
import de.joshuaschuenke.discordbot.commands.blacklist.CommandRemoveForbiddenWord;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;

import java.util.HashMap;


public class CommandDictionary {

    public static HashMap<String, CommandInterface> commands = new HashMap<>();
    public static HashMap<String, ApplicationCommandRequest> requests = new HashMap<>();


    public static void initialize(final String... args) {
        commands.put("ping", new CommandPing());
        requests.put("ping", ApplicationCommandRequest.builder()
                .name("ping")
                .description("Pong!")
                .build());

        commands.put("play", new CommandMusicPlay());
        requests.put("play", ApplicationCommandRequest.builder()
                .name("play")
                .description("play")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("link")
                        .description("link")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build());

        commands.put("stop", new CommandMusicStop());
        requests.put("stop", ApplicationCommandRequest.builder()
                .name("stop")
                .description("Stop the bot!")
                .build());

        commands.put("skip", new CommandMusicSkip());
        requests.put("skip", ApplicationCommandRequest.builder()
                .name("skip")
                .description("Skip the next track")
                .build());

        commands.put("createwtg", new CommandWtgCreate());
        requests.put("createwtg", ApplicationCommandRequest.builder()
                .name("createwtg")
                .description("Create a Watchtogether room")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("link")
                        .description("link")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("red")
                        .description("red value for room color")
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .required(false)
                        .maxValue(255.0)
                        .minValue(0.0)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("green")
                        .description("green value for room color")
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .required(false)
                        .maxValue(255.0)
                        .minValue(0.0)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("blue")
                        .description("blue value for room color")
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .required(false)
                        .maxValue(255.0)
                        .minValue(0.0)
                        .build())
                .build());

        commands.put("addword", new CommandAddForbiddenWord());
        requests.put("addword", ApplicationCommandRequest.builder()
                .name("addword")
                .description("Add a word to a forbidden words list")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("word")
                        .description("The word to add")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build());

        commands.put("removeword", new CommandRemoveForbiddenWord());
        requests.put("removeword", ApplicationCommandRequest.builder()
                .name("removeword")
                .description("Remove a word from a forbidden words list")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("word")
                        .description("The word to remove")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build());
    }
}
