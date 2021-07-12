package net.modfest.utilities.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.modfest.utilities.config.Config;
import net.modfest.utilities.data.WebHookJson;
import net.modfest.utilities.discord.WebHookUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements ServerPlayPacketListener {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void playerConnected(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        WebHookUtil.send(WebHookJson.createSystem("**" + player.getDisplayName().getString() + "** joined!"));
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void playerDisconnected(ServerPlayerEntity player, CallbackInfo ci) {
        WebHookUtil.send(WebHookJson.createSystem("**" + player.getDisplayName().getString() + "** left!"));
    }
}
