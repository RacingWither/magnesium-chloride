package net.racingwither.datagen;

import net.minecraft.data.PackOutput;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MagnesiumChlorideMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MagnesiumChlorideMod.MAGNESIUM_CHLORIDE.get());
        basicItem(MagnesiumChlorideMod.RAW_MAGNESIUM.get());
        basicItem(MagnesiumChlorideMod.MAGNESIUM.get());
        basicItem(MagnesiumChlorideMod.ACID_BUCKET.get());
    }
}
