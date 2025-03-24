package com.iafenvoy.cet.mixin;

import com.iafenvoy.cet.screen.CooldownHudRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render",at=@At("RETURN"))
    private void onRenderHud(DrawContext context, float tickDelta, CallbackInfo ci){
        CooldownHudRenderer.render(context);
    }
}
