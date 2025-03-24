package com.iafenvoy.cet.screen;

import com.iafenvoy.cet.Static;
import com.iafenvoy.cet.config.CETConfig;
import com.iafenvoy.cet.data.VocabularyLoader;
import com.mojang.text2speech.Narrator;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class VocabularyScreen extends Screen {
    private static final int BUTTON_WIDTH = 200, BUTTON_HEIGHT = 18;
    private static final IntList Y_OFFSET = IntList.of(-80, -60, -40, -20, 0, 20, 40, 60, 80);
    private static final Narrator NARRATOR = Narrator.getNarrator();
    @Nullable
    private final Screen parent;
    private final LinkedHashMap<String, VocabularyLoader.Explanation> data;
    private final List<String> words;
    private final int answer;
    private final Runnable successCallback, failureCallback;
    private final List<MultiStateButtonWidget> buttons = new LinkedList<>();
    private boolean done = false, correct;

    public VocabularyScreen(@Nullable Screen parent, int count, Runnable successCallback, Runnable failureCallback) {
        super(Text.empty());
        this.parent = parent;
        this.data = VocabularyLoader.random(count, CETConfig.INSTANCE.wordCollection);
        this.words = List.copyOf(this.data.keySet());
        this.answer = new Random().nextInt(count);
        this.successCallback = successCallback;
        this.failureCallback = failureCallback;
        this.sayWord();
    }

    @Override
    protected void init() {
        this.buttons.clear();
        super.init();
        int x = this.width / 2, y = this.height / 2, i = 0;
        for (String word : this.words) {
            this.buttons.add(this.addDrawableChild(new MultiStateButtonWidget(x - BUTTON_WIDTH / 2, y + Y_OFFSET.getInt(i) - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal(this.data.get(word).get()), button -> {
                if (this.done) return;
                this.buttons.forEach(b -> b.setState(MultiStateButtonWidget.State.DEFAULT));
                button.setState(MultiStateButtonWidget.State.HIGHLIGHT);
            })));
            i++;
        }
        this.addDrawableChild(new MultiStateButtonWidget(x - BUTTON_WIDTH / 2, y + 100, BUTTON_WIDTH, BUTTON_HEIGHT, Text.translatable("screen.cet_vocabulary.submit"), button -> {
            if (done) {
                this.close();
                (this.correct ? this.successCallback : this.failureCallback).run();
            } else {
                int answer = this.findFirstAnswer();
                if (answer == -1) return;
                this.correct = this.answer == answer;
                if (this.correct)
                    this.buttons.get(this.answer).setState(MultiStateButtonWidget.State.SUCCESS);
                else {
                    this.buttons.get(this.answer).setState(MultiStateButtonWidget.State.WARNING);
                    this.buttons.get(answer).setState(MultiStateButtonWidget.State.ERROR);
                }
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ambient(this.correct ? Static.BINGO : Static.WRONG));
                button.setMessage(Text.translatable("screen.cet_vocabulary.close"));
                this.done = true;
            }
        }));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.words.get(this.answer), this.width / 2, this.height / 2 - 100, -1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if ((double) this.height / 2 - 100 <= mouseY && mouseY <= (double) this.height / 2 - 80) this.sayWord();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void sayWord() {
        NARRATOR.say(this.words.get(this.answer), true);
    }

    private int findFirstAnswer() {
        for (int i = 0; i < this.buttons.size(); i++)
            if (this.buttons.get(i).isFocused())
                return i;
        return -1;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
