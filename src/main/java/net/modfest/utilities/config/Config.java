package net.modfest.utilities.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.modfest.utilities.ModFestUtilities;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("modfest.json").toAbsolutePath();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private ConfigData data = new ConfigData();

    public void save() throws IOException {
        Files.writeString(configPath, gson.toJson(data), StandardCharsets.UTF_8);
    }

    public void load() {
        try {
            if(!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                save();
            }

            data = gson.fromJson(Files.newBufferedReader(configPath, StandardCharsets.UTF_8), ConfigData.class);
        } catch (IOException e) {
            ModFestUtilities.LOGGER.error("Exception while loading config file", e);
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
