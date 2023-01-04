package de.joshuaschuenke.discordbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class CommandPing implements CommandInterface {

    @Override
    public Mono<Void> react(final ChatInputInteractionEvent event) {
        return event.reply().withEphemeral(true).withContent("Pong!");
    }
}
