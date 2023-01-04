package de.joshuaschuenke.discordbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public interface CommandInterface {
    Mono<Void> react(ChatInputInteractionEvent event);
}
