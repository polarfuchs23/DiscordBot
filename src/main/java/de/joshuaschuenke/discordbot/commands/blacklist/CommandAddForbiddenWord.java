package de.joshuaschuenke.discordbot.commands.blacklist;

import de.joshuaschuenke.discordbot.commands.CommandInterface;
import de.joshuaschuenke.discordbot.serverspecific.ForbiddenWordList;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Member;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public class CommandAddForbiddenWord implements CommandInterface {
    @Override
    public Mono<Void> react(final ChatInputInteractionEvent event) {
        return Mono.justOrEmpty(event.getInteraction().getMember())
                .flatMap(Member::getHighestRole)
                .map(role -> role.getPermissions().contains(Permission.MANAGE_MESSAGES))
                .flatMap(hasPermission -> {
                    System.out.println(hasPermission);
                    if (hasPermission) {
                        final var guild = event.getInteraction().getMember().orElseThrow().getGuild().block();
                        final var wordOption = event.getOption("word")
                                .flatMap(ApplicationCommandInteractionOption::getValue)
                                .map(ApplicationCommandInteractionOptionValue::asString)
                                .get();

                        ForbiddenWordList.addWord(wordOption, guild.getId().asLong());
                        return event.reply()
                                .withEphemeral(true)
                                .withContent("Adding \"" + wordOption + "\" to the list");
                    } else {
                        System.out.println("No permission");
                        return event.reply()
                                .withEphemeral(true)
                                .withContent("You do not have the necessary permissions to do that");
                    }
                })
                .then();
    }
}
