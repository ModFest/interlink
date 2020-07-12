package net.modfest.utilities.mixin;


import net.minecraft.util.crash.CrashReport;
import net.modfest.utilities.ModFestUtilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(CrashReport.class)
public abstract class CrashReportMixin {

    @Shadow public abstract String asString();

    @Inject(method = "writeToFile", at = @At("RETURN"))
    private void writeToFile(File file, CallbackInfoReturnable<Boolean> info) {
        ModFestUtilities.handleCrashReport(this.asString());
    }
}
