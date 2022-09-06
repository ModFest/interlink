package net.modfest.utilities;

import com.google.gson.annotations.Expose;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CrashUploader {
    public static void handleCrashReport(String report) {
        if(!ModFestUtilities.CONFIG.shouldHastebinCrashes()) return;
        
        ModFestUtilities.LOGGER.info("[ModFest] Publishing crash report.");
        try {
            HttpResponse<String> response = ModFestUtilities.CLIENT.send(HttpRequest.newBuilder()
                    .uri(URI.create("https://hastebin.com/documents"))
                    .POST(HttpRequest.BodyPublishers.ofString(report))
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .build(), HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() / 100 != 2) throw new RuntimeException("Non-success status code from Hastebin request " + response);
            
            HasteBinResponse haste = ModFestUtilities.GSON.fromJson(response.body(), HasteBinResponse.class);
            ModFestUtilities.LOGGER.info("[ModFest] Crash report available at: https://hastebin.com/" + haste.key);
            WebHookJson.createSystem("The server has crashed!\nReport: https://hastebin.com/" + haste.key).send().join();
        } catch (Exception e) {
            ModFestUtilities.LOGGER.error("[ModFest] Crash log failed to send.", e);
        }
    }

    public static class HasteBinResponse {
        @Expose public String key = "";
    }
}
