package net.modfest.utilities.mixin;

import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.modfest.utilities.WebHookJson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "handleDecoratedMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V"))
    private void onChat(SignedMessage message, CallbackInfo ci) {
        WebHookJson.create(this.player, message.getContent().getString()).send();
    }
}
