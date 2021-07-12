package net.modfest.utilities.mixin;

import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.modfest.utilities.data.WebHookJson;
import net.modfest.utilities.discord.WebHookUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "handleMessage", cancellable = true, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private void onChat(TextStream.Message message, CallbackInfo ci) {
        WebHookUtil.send(WebHookJson.create(this.player, message.getFiltered()));
    }
}
