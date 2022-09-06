package net.modfest.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        return new WebHookJson(content, player.getName().getString(), "https://api.nucleoid.xyz/skin/face/256/" + player.getUuidAsString());
    }

    public static WebHookJson createSystem(String content) {
        return new WebHookJson(content, ModFestUtilities.CONFIG.getName(), ModFestUtilities.CONFIG.getIcon());
    }

    public CompletableFuture<Unit> send() {
        if (ModFestUtilities.CONFIG.getWebhook().isEmpty()) {
            return CompletableFuture.completedFuture(Unit.INSTANCE);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse<String> response = ModFestUtilities.CLIENT.send(HttpRequest.newBuilder()
                        .uri(URI.create(ModFestUtilities.CONFIG.getWebhook()))
                        .POST(HttpRequest.BodyPublishers.ofString(ModFestUtilities.GSON.toJson(this)))
                        .header("Content-Type", "application/json; charset=utf-8")
                        .build(), HttpResponse.BodyHandlers.ofString());
                if(response.statusCode() / 100 != 2) throw new RuntimeException("Non-success status code from webhook request " + response);
            } catch (Exception e) {
                ModFestUtilities.LOGGER.warn("[ModFest] Failed to send message to discord.", e);
            }

            return Unit.INSTANCE;
        });
    }

    public static class Mentions {
        @Expose String[] parse = new String[0];
    }
}
