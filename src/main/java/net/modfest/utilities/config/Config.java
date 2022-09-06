package net.modfest.utilities.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {

    private static Config instance;
    private final File file = FabricLoader.getInstance().getConfigDir().resolve("modfest.json").toFile();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
            FileUtils.writeStringToFile(this.file, this.gson.toJson(this.data), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            save();
        }
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(this.file.getPath()));
            this.data = this.gson.fromJson(new String(bytes, Charset.defaultCharset()), ConfigData.class);
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
}
