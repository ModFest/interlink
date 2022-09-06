package net.modfest.utilities.config;

import com.google.gson.annotations.Expose;

public class ConfigData {
    @Expose public Server server = new Server();
    @Expose public Discord discord = new Discord();
    @Expose public Crashes crashes = new Crashes();

    public static class Server {
        @Expose public String name = "";
        @Expose public String icon = "";
    }

    public static class Discord {
        @Expose public String webhook = "";
        @Expose public String channel = "";
        @Expose public String token = "";
        @Expose public boolean mirrorDeath = false;
    }

    public static class Crashes {
        @Expose public boolean uploadToHastebin = true;
    }
}
