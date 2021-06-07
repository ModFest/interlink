package net.modfest.utilities.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.modfest.utilities.config.Config;
import net.modfest.utilities.data.WebHookJson;
import net.modfest.utilities.discord.WebHookUtil;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void sendDeathMessage(DamageSource source, CallbackInfo info) {
        if (!Config.getInstance().shouldMirrorDeath()) return;
        if (Config.getInstance().getWebhook().isEmpty()) return;

        Text displayName = ((PlayerEntity) (Object) this).getDisplayName();
        if (source.getAttacker() instanceof PlayerEntity) {
            Text attackerDisplayName = ((PlayerEntity) source.getAttacker()).getDisplayName();
            WebHookUtil.send(WebHookJson.createSystem("**" + displayName.getString() + "** Died To **" + attackerDisplayName.getString() + "**."));
        } else {
            WebHookUtil.send(WebHookJson.createSystem("**" + displayName.getString() + "** Died."));
        }
    }
}
