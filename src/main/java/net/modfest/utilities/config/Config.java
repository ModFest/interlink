package net.modfest.utilities.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static Config instance;
    private final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("modfest.json").toAbsolutePath();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private ConfigData data;

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void save() {
        try {
            if(this.data == null) {
                this.data = new ConfigData();
            }
            Files.writeString(configPath, gson.toJson(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            if(!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                save();
            }

            data = gson.fromJson(Files.newBufferedReader(configPath, StandardCharsets.UTF_8), ConfigData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWebhook() {
        return this.data.discord.webhook;
    }

    public String getChannel() {
        return this.data.discord.channel;
    }

    public String getToken() {
        return this.data.discord.token;
    }

    public boolean shouldMirrorDeath() {
        return this.data.discord.mirrorDeath;
    }

    public String getName() {
        return this.data.server.name;
    }

    public String getIcon() {
        return this.data.server.icon;
    }
    
    public boolean shouldHastebinCrashes() {
        return this.data.crashes.uploadToHastebin;
    }
}
