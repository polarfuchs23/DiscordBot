package de.joshuaschuenke.discordbot.commands;

import de.joshuaschuenke.discordbot.audiocommandsupply.AudioProviderAndPlayerProvider;
import de.joshuaschuenke.discordbot.audiocommandsupply.PlaylistScheduler;
import de.joshuaschuenke.discordbot.youtubeapisupply.YoutubeLinkGetter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.VoiceChannelJoinSpec;
import reactor.core.publisher.Mono;

public class CommandMusicPlay implements CommandInterface {

    @Override
    public Mono<Void> react(final ChatInputInteractionEvent event) {

        return Mono.justOrEmpty(event.getInteraction().getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> channel.join(VoiceChannelJoinSpec.builder().provider(AudioProviderAndPlayerProvider.provider).build()))
                .flatMap(connection -> Mono.fromCallable(() -> event.getOption("link")
                        .get().getValue()
                        .get().asString()))
                .flatMap(link -> Mono.fromCallable(() -> {
                    final String newLink;
                    if (!link.contains("?v") && !link.contains("youtube")) {
                        newLink = YoutubeLinkGetter.getLinkByKeyword(link);
                    } else {
                        newLink = link;
                    }
                    AudioProviderAndPlayerProvider.manager.loadItem(newLink, new PlaylistScheduler(AudioProviderAndPlayerProvider.player));
                    return newLink;
                }))
                .flatMap(link -> {
                            final var messageBuilder = event.reply().withEphemeral(true);

                            if (PlaylistScheduler.PLAY_LIST.isEmpty()) {
                                return messageBuilder.withContent("Playing: " + link);
                            }

                            return messageBuilder.withContent("Added to playlist: " + link);
                        }
                );
    }
}
