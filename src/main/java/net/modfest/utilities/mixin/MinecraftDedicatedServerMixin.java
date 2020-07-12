package net.modfest.utilities.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.modfest.utilities.data.WebHookJson;
import net.modfest.utilities.discord.WebHookUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.SERVER)
@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin {

    @Inject(method = "setupServer()Z", at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", ordinal = 0))
    private void serverReady(CallbackInfoReturnable<Boolean> info) {
        WebHookUtil.send(WebHookJson.createSystem("The server has started."));
    }

    @Inject(method = "setupServer()Z", at = @At("HEAD"))
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
        WebHookUtil.send(WebHookJson.createSystem("The server is starting..."));
    }
}
