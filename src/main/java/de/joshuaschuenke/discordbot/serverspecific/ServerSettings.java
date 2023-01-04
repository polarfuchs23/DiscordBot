package de.joshuaschuenke.discordbot.serverspecific;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class ServerSettings {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final long guild;
    private final Path settingsFile;

    private JsonObject settings;

    public ServerSettings(final long guild) {
        this.guild = guild;
        this.settingsFile = Paths.get(String.format("%d.json", guild));
    }

    public void loadSettings() throws IOException {
        this.settings = JsonParser.parseString(Files.readString(this.settingsFile)).getAsJsonObject();
    }

    public void saveSettings() throws IOException {
        Files.writeString(this.settingsFile, GSON.toJson(this.settings), StandardOpenOption.CREATE);
    }

    public JsonObject getSettings() {
        return this.settings;
    }
}
