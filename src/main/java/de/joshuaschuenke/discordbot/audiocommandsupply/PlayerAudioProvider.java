package de.joshuaschuenke.discordbot.audiocommandsupply;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

public class PlayerAudioProvider extends AudioProvider {

    private final AudioPlayer player;
    private final MutableAudioFrame frame = new MutableAudioFrame();

    public PlayerAudioProvider(final AudioPlayer player) {
        super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));

        this.frame.setBuffer(this.getBuffer());
        this.player = player;
    }

    @Override
    public boolean provide() {
        final boolean provided = this.player.provide(this.frame);

        if (provided) {
            this.getBuffer().flip();
        }
        return provided;
    }
}
