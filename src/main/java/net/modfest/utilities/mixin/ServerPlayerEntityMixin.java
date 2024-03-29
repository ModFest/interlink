package net.modfest.utilities.mixin;

import net.modfest.utilities.ModFestUtilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.modfest.utilities.WebHookJson;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"))
    private void hook_deathMessage(DamageSource source, CallbackInfo ci, boolean bl, Text text) {
        if (!ModFestUtilities.CONFIG.shouldMirrorDeath()) return;
        WebHookJson.create((ServerPlayerEntity) (Object) this, text.getString()).send();
    }
}
