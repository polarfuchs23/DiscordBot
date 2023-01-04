package de.joshuaschuenke.discordbot.commands;

import de.joshuaschuenke.discordbot.audiocommandsupply.AudioProviderAndPlayerProvider;
import de.joshuaschuenke.discordbot.audiocommandsupply.PlaylistScheduler;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class CommandMusicStop implements CommandInterface {
    @Override
    public Mono<Void> react(final ChatInputInteractionEvent event) {
        PlaylistScheduler.emptyQueue();
        AudioProviderAndPlayerProvider.player.stopTrack();
        return event.reply().withEphemeral(true).withContent("Stopping");
    }

}

