package com.iafenvoy.cet.data;

import com.iafenvoy.cet.config.CETConfig;
import com.iafenvoy.cet.screen.VocabularyScreen;
import com.iafenvoy.cet.util.TimeUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.function.IntSupplier;

@Environment(EnvType.CLIENT)
public class ExamCounter {
    private static final IntSupplier INTERVAL = () -> CETConfig.INSTANCE.interval;
    private static int COOLDOWN = INTERVAL.getAsInt();
    private static int FAILURE_REMAIN = CETConfig.INSTANCE.maxFailureChance;

    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        COOLDOWN--;
        if (COOLDOWN == 0)
            client.setScreen(new VocabularyScreen(client.currentScreen, CETConfig.INSTANCE.choiceCount, () -> COOLDOWN = INTERVAL.getAsInt(), () -> {
                COOLDOWN = INTERVAL.getAsInt();
                FAILURE_REMAIN--;
                if (FAILURE_REMAIN <= 0) client.close();
            }));
    }

    public static Text formatFirstText() {
        return Text.translatable("screen.cet_vocabulary.cooldown", TimeUtil.getDate(COOLDOWN / 20));
    }

    public static Text formatSecondText() {
        return Text.translatable("screen.cet_vocabulary.remain_chance", FAILURE_REMAIN);
    }
}
