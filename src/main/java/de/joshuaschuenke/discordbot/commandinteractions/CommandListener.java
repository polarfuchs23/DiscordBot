package de.joshuaschuenke.discordbot.commandinteractions;

import de.joshuaschuenke.discordbot.commands.CommandDictionary;
import de.joshuaschuenke.discordbot.commands.CommandInterface;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class CommandListener {

    public static Mono<Void> react(final ChatInputInteractionEvent event) {
        final CommandInterface commandObject = CommandDictionary.commands.get(event.getCommandName());
        return commandObject.react(event);
    }

}
