package de.joshuaschuenke.discordbot.serverspecific;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class ForbiddenWordList {

    private static final Logger LOG = LoggerFactory.getLogger(ForbiddenWordList.class);

    private static Set<String> wordList = new HashSet<>();

    public static void addWord(final String wordToAdd, final Long guildId) {
        readWordList(guildId);
        wordList.add(wordToAdd);
        writeWordList(guildId);
    }

    public static boolean removeWord(final String wordToRemove, final Long guildId) {
        readWordList(guildId);
        final var removed = wordList.remove(wordToRemove);
        writeWordList(guildId);
        return removed;
    }

    private static void writeWordList(final Long guildId) {
        final File file = new File(String.format("%d.json", guildId));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        try {
            final var writer = new FileWriter(file);
            final var jsonObject = new JSONObject();
            wordList.forEach(element -> jsonObject.put(element, element));
            writer.write(jsonObject.toJSONString());
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void readWordList(final Long guildId) {
        final File file = new File(String.format("%d.json", guildId));
        if (file.exists()) {
            try {
                final JSONParser parser = new JSONParser();
                final JSONObject jsonObjectWordList = (JSONObject) parser.parse(new FileReader(String.format("%d.json", guildId)));
                final Set<String> readList = new HashSet<>();
                jsonObjectWordList.values().forEach(word -> readList.add(word.toString()));
                wordList = readList;
            } catch (final ParseException | IOException e) {
                e.printStackTrace();
            }
        } else {
            wordList = new HashSet<>();
        }
    }
}
