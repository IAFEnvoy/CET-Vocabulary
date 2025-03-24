package com.iafenvoy.cet.fabric;

import com.iafenvoy.cet.CETVocabulary;
import com.iafenvoy.cet.data.VocabularyLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public final class CETVocabularyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.of(CETVocabulary.MOD_ID, "vocabulary");
            }

            @Override
            public void reload(ResourceManager manager) {
                VocabularyLoader.INSTANCE.reload(manager);
            }
        });
    }
}
