package net.racingwither.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MagnesiumChlorideMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAllBlockWithItem(MagnesiumChlorideMod.MAGNESIUM_ORE);
    }


    private void cubeAllBlockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
