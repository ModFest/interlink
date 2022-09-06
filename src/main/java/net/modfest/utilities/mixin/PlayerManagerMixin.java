package net.modfest.utilities.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.modfest.utilities.WebHookJson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements ServerPlayPacketListener {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void playerConnected(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        WebHookJson.createSystem("**" + player.getDisplayName().getString() + "** joined!").send();
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void playerDisconnected(ServerPlayerEntity player, CallbackInfo ci) {
        WebHookJson.createSystem("**" + player.getDisplayName().getString() + "** left!").send();
    }
}
