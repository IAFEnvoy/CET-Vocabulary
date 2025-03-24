package com.iafenvoy.cet.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.iafenvoy.cet.CETVocabulary;
import com.iafenvoy.cet.util.ListUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public enum VocabularyLoader implements SynchronousResourceReloader {
    INSTANCE;
    public static final Codec<Map<String, Explanation>> CODEC = Codec.unboundedMap(Codec.STRING, Explanation.CODEC);
    public static final Map<String, Explanation> CET4 = new LinkedHashMap<>();
    public static final Map<String, Explanation> CET6 = new LinkedHashMap<>();

    @Override
    public void reload(ResourceManager manager) {
        //CET4
        try {
            Resource resource = manager.getResourceOrThrow(Identifier.of(CETVocabulary.MOD_ID, "cet4.json"));
            JsonElement json = JsonParser.parseReader(resource.getReader());
            Map<String, Explanation> data = CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(CETVocabulary.LOGGER::error).orElseThrow();
            CET4.clear();
            CET4.putAll(data);
        } catch (Exception e) {
            CETVocabulary.LOGGER.error("Failed to load CET 4 Vocabulary.", e);
        }
        CETVocabulary.LOGGER.info("Successfully loaded {} CET 4 words.", CET4.size());
        //CET6
        try {
            Resource resource = manager.getResourceOrThrow(Identifier.of(CETVocabulary.MOD_ID, "cet6.json"));
            JsonElement json = JsonParser.parseReader(resource.getReader());
            Map<String, Explanation> data = CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(CETVocabulary.LOGGER::error).orElseThrow();
            CET6.clear();
            CET6.putAll(data);
        } catch (Exception e) {
            CETVocabulary.LOGGER.error("Failed to load CET 6 Vocabulary.", e);
        }
        CETVocabulary.LOGGER.info("Successfully loaded {} CET 6 words.", CET6.size());
    }

    public static LinkedHashMap<String, Explanation> random(int count, Type type) {
        Map<String, Explanation> data = type.collect();
        List<String> randomed = ListUtil.sample(List.copyOf(data.keySet()), count);
        return randomed.stream().collect(LinkedHashMap::new, (m, s) -> m.put(s, data.get(s)), Map::putAll);
    }

    public record Explanation(String zh, String en) {
        public static final Codec<Explanation> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.optionalFieldOf("中释", "???").forGetter(Explanation::zh),
                Codec.STRING.optionalFieldOf("英释", "???").forGetter(Explanation::en)
        ).apply(i, Explanation::new));

        public String get(){
            return MinecraftClient.getInstance().getLanguageManager().getLanguage().startsWith("zh_") ? this.zh : this.en;
        }
    }

    public enum Type {
        CET4(true, false), CET6(false, true), BOTH(true, true);

        private final boolean cet4;
        private final boolean cet6;

        Type(boolean cet4, boolean cet6) {
            this.cet4 = cet4;
            this.cet6 = cet6;
        }

        private Map<String, Explanation> collect() {
            Map<String, Explanation> map = new LinkedHashMap<>();
            if (this.cet4) map.putAll(VocabularyLoader.CET4);
            if (this.cet6) map.putAll(VocabularyLoader.CET6);
            return map;
        }
    }
}
