package com.iafenvoy.cet.mixin;

import com.iafenvoy.cet.data.ExamCounter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "tick",at=@At("RETURN"))
    private void onClientWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if(shouldKeepTicking.getAsBoolean()) ExamCounter.tick();
    }
}
