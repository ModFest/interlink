package net.modfest.utilities.mixin;

import java.util.function.Predicate;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.message.MessageSourceProfile;
import net.minecraft.network.message.MessageType.Parameters;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.modfest.utilities.ModFestUtilities;
import net.modfest.utilities.WebHookJson;
import net.modfest.utilities.config.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements ServerPlayPacketListener {

    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void playerConnected(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        WebHookJson.createSystem("**" + player.getDisplayName().getString() + "** joined!").send();
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void playerDisconnected(ServerPlayerEntity player, CallbackInfo ci) {
        WebHookJson.createSystem("**" + player.getDisplayName().getString() + "** left!").send();
    }

    @Inject(method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageSourceProfile;Lnet/minecraft/network/message/MessageType$Parameters;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;logChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageType$Parameters;Ljava/lang/String;)V"))
    private void handleMessage(SignedMessage message, Predicate<ServerPlayerEntity> shouldSendFiltered, ServerPlayerEntity sender, MessageSourceProfile sourceProfile, Parameters params, CallbackInfo ci) {
        if (sender != null) {
            WebHookJson.create(sender, message.getContent().getString()).send();
        } else {
            // Note: theoretically, this.server.getServerMetadata().getFavicon() could be used to get the server's set icon (./server-dir/server-icon.png).
            // Unfortunately, Discord doesn't accept data URLs for webhook avatars yet, so this can't be used.
            // I left this note so that a future developer may try again in the future, when it may be supported -- Jamalam :)

            WebHookJson.create("Server", ModFestUtilities.CONFIG.getIcon(), message.getContent().getString()).send();
        }
    }
}
