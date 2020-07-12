package net.modfest.utilities.mixin;

import net.minecraft.server.MinecraftServer;
import net.modfest.utilities.ModFestUtilities;
import net.modfest.utilities.data.WebHookJson;
import net.modfest.utilities.discord.WebHookUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo info) {
        ModFestUtilities.shutdown();
        WebHookUtil.send(WebHookJson.createSystem("The server has shutdown."));
    }
}
