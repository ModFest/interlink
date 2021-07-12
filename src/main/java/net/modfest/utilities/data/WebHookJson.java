package net.modfest.utilities.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.server.network.ServerPlayerEntity;
import net.modfest.utilities.config.Config;

public class WebHookJson {
    @Expose
    public String content;
    @Expose
    public String username;
    @Expose @SerializedName("avatar_url")
    public String avatar;
    @Expose @SerializedName("allowed_mentions")
    public Mentions mentions = new Mentions();


    public WebHookJson(String content, String username, String avatar) {
        this.content = content;
        this.username = username;
        this.avatar = avatar;
    }

    public static WebHookJson create(ServerPlayerEntity player, String content) {
        return new WebHookJson(content, player.getName().asString(), "https://api.nucleoid.xyz/skin/face/256/" + player.getUuidAsString());
    }

    public static WebHookJson createSystem(String content) {
        return new WebHookJson(content, Config.getInstance().getName(), Config.getInstance().getIcon());
    }

    public static class Mentions {
        @Expose String[] parse = new String[0];
    }
}
