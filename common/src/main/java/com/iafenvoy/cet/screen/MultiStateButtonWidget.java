package com.iafenvoy.cet.screen;

import com.iafenvoy.cet.CETVocabulary;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class MultiStateButtonWidget extends ButtonWidget {
    public static final Identifier WIDGETS_TEXTURE = Identifier.of(CETVocabulary.MOD_ID, "textures/gui/multi_widget.png");
    private State state = State.DEFAULT;

    public MultiStateButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, button -> onPress.onPress((MultiStateButtonWidget) button), ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawNineSlicedTexture(WIDGETS_TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.state.textureOffset * 20);
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;
        this.drawMessage(context, minecraftClient.textRenderer, i | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    public void setState(State state) {
        this.state = state;
    }

    @Environment(EnvType.CLIENT)
    public interface PressAction {
        void onPress(MultiStateButtonWidget button);
    }

    public enum State {
        DEFAULT(0), HIGHLIGHT(1), ERROR(2), WARNING(3), INFO(4), SUCCESS(5);

        private final int textureOffset;

        State(int textureOffset) {
            this.textureOffset = textureOffset;
        }
    }
}
