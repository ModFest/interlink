package net.modfest.utilities.mixin;

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

    @Shadow public abstract ServerPlayerEntity getPlayer(UUID uuid);
    @Shadow public abstract MinecraftServer getServer();

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "broadcastChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At("HEAD"))
    private void broadcastChatMessage(Text message, MessageType type, UUID sender, CallbackInfo info) {
        if(this.server.playerManager != null) {
            this.server.playerManager = this.getServer().getPlayerManager();
        }
        if (Config.getInstance().getWebhook().isEmpty()) return;
        if(message instanceof TranslatableText) {
            String key = ((TranslatableText) message).getKey();

            if(key.equals("chat.type.text")) { // chat
                String msg = (String) ((TranslatableText) message).getArgs()[1];
                ServerPlayerEntity player = this.server.playerManager.getPlayer(sender);
                if(player != null)
                    WebHookUtil.send(WebHookJson.create(player, msg));

            } else if (key.equals("multiplayer.player.joined")) { // join
                String name = ((Text)((TranslatableText) message).getArgs()[0]).getString();
                WebHookUtil.send(WebHookJson.createSystem("**" + name + "** Joined."));

            } else if (key.equals("multiplayer.player.left")) { // leave
                String name = ((Text)((TranslatableText) message).getArgs()[0]).getString();
                WebHookUtil.send(WebHookJson.createSystem("**" + name + "** Left."));
            }
        }
    }
}
