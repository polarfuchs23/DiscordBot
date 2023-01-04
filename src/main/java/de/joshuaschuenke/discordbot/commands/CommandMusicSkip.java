package de.joshuaschuenke.discordbot.commands;

import de.joshuaschuenke.discordbot.audiocommandsupply.AudioProviderAndPlayerProvider;
import de.joshuaschuenke.discordbot.audiocommandsupply.PlaylistScheduler;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class CommandMusicSkip implements CommandInterface {
    @Override
    public Mono<Void> react(final ChatInputInteractionEvent event) {
        if (!PlaylistScheduler.PLAY_LIST.isEmpty()) {
            final var track = PlaylistScheduler.PLAY_LIST.pop();
            System.out.println("New track: " + track.getInfo().title);
            AudioProviderAndPlayerProvider.player.playTrack(track);
            return event.reply().withEphemeral(true).withContent("Skipping");
        } else {
            PlaylistScheduler.emptyQueue();
            AudioProviderAndPlayerProvider.player.stopTrack();
            return event.reply().withEphemeral(true).withContent("This was the last track. Stopping the music...");
        }
    }
}
