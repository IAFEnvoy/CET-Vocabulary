package com.iafenvoy.cet.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iafenvoy.cet.CETVocabulary;
import com.iafenvoy.cet.data.VocabularyLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CETConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_PATH = "./config/cet.json";
    public static final CETConfig INSTANCE;

    public boolean enable = true;
    public int interval = 3 * 60 * 20;
    public int maxFailureChance = 10;
    public boolean renderTime = true;
    public int choiceCount = 6;
    public VocabularyLoader.Type wordCollection = VocabularyLoader.Type.CET4;

    static {
        CETConfig config;
        try {
            config = GSON.fromJson(new FileReader(CONFIG_PATH), CETConfig.class);
        } catch (FileNotFoundException e) {
            CETVocabulary.LOGGER.error("Failed to load config {}", CONFIG_PATH, e);
            config = new CETConfig();
            try {
                FileUtils.write(new File(CONFIG_PATH), GSON.toJson(config), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                CETVocabulary.LOGGER.error("Failed to create config {}", CONFIG_PATH, ex);
            }
        }
        INSTANCE = config;
    }
}
