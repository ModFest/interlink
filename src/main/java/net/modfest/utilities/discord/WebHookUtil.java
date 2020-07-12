package net.modfest.utilities.discord;

import net.modfest.utilities.ModFestUtilities;
import net.modfest.utilities.config.Config;
import net.modfest.utilities.data.WebHookJson;
import okhttp3.*;

import java.io.IOException;

public class WebHookUtil {

    public static void send(WebHookJson json) {
        if(Config.getInstance().getWebhook().isEmpty()) return;
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), ModFestUtilities.GSON.toJson(json));
        Request request = new Request.Builder()
                .url(Config.getInstance().getWebhook())
                .post(body)
                .build();
        try (Response response = ModFestUtilities.client.newCall(request).execute()) {
        } catch (IOException e) {
            ModFestUtilities.LOGGER.warn("[ModFest] Failed to send message to discord.", e);
        }
    }
}
