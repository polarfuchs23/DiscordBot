package de.joshuaschuenke.discordbot.audiocommandsupply;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlaylistScheduler implements AudioLoadResultHandler {

    public static final ExecutorService SERVICE;
    public static final ArrayDeque<AudioTrack> PLAY_LIST;

    static {
        SERVICE = Executors.newSingleThreadExecutor();
        PLAY_LIST = new ArrayDeque<>();
        SERVICE.submit(() -> {
            final AudioPlayer player = AudioProviderAndPlayerProvider.player;
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                    if (PLAY_LIST.isEmpty()) {
                        continue;
                    }

                    if (player.getPlayingTrack() != null) {
                        continue;
                    }

                    final var track = PLAY_LIST.pop();
                    System.out.println("New track: " + track.getInfo().title);
                    player.playTrack(track);

                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public final AudioPlayer player;

    public PlaylistScheduler(final AudioPlayer player) {
        this.player = player;
    }

    public static void emptyQueue() {
        while (!PLAY_LIST.isEmpty()) {
            PLAY_LIST.pop();
        }
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        PLAY_LIST.add(track);
        System.out.println("Loaded track: " + track.getInfo().title);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        this.player.playTrack(playlist.getSelectedTrack());
    }

    @Override
    public void noMatches() {
        throw new IllegalStateException("Track not found");
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        exception.printStackTrace();
    }
}
