package com.iafenvoy.cet.screen;

import com.iafenvoy.cet.config.CETConfig;
import com.iafenvoy.cet.data.ExamCounter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public class CooldownHudRenderer {
    public static void render(DrawContext context) {
        if (!CETConfig.INSTANCE.enable || !CETConfig.INSTANCE.renderTime) return;
        int scaledHeight = context.getScaledWindowHeight();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawTextWithShadow(textRenderer, ExamCounter.formatFirstText(), 10, scaledHeight - 30, -1);
        context.drawTextWithShadow(textRenderer, ExamCounter.formatSecondText(), 10, scaledHeight - 20, -1);
    }
}
