package de.joshuaschuenke.discordbot.audiocommandsupply;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.voice.AudioProvider;

public class AudioProviderAndPlayerProvider {

    public static AudioPlayerManager manager;
    public static AudioPlayer player;
    public static AudioProvider provider;

    public static void initialize() {
        manager = new DefaultAudioPlayerManager();
        manager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

        AudioSourceManagers.registerRemoteSources(manager);

        player = manager.createPlayer();

        provider = new PlayerAudioProvider(player);
    }

}
