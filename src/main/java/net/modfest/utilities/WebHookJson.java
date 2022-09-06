package net.modfest.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.modfest.utilities.config.Config;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<Unit> send() {
        if (Config.getInstance().getWebhook().isEmpty()) {
            return CompletableFuture.completedFuture(Unit.INSTANCE);
        }

        return CompletableFuture.supplyAsync(() -> {
            RequestBody body = RequestBody.create(ModFestUtilities.GSON.toJson(this), MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                .url(Config.getInstance().getWebhook())
                .post(body)
                .build();

            try (Response response = ModFestUtilities.client.newCall(request).execute()) {
            } catch (IOException e) {
                ModFestUtilities.LOGGER.warn("[ModFest] Failed to send message to discord.", e);
            }

            return Unit.INSTANCE;
        });
    }

    public static class Mentions {
        @Expose String[] parse = new String[0];
    }
}
