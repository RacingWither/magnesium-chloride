package net.racingwither.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        add(MagnesiumChlorideMod.MAGNESIUM_ORE.get(), createOreDrop(MagnesiumChlorideMod.MAGNESIUM_ORE.get(), MagnesiumChlorideMod.RAW_MAGNESIUM.get()));
        dropSelf(MagnesiumChlorideMod.HCL_BURNER.get());
        dropSelf(MagnesiumChlorideMod.FLUID_CANISTER.get());
        add(MagnesiumChlorideMod.FLUID_CANISTER.get(), createSingleItemTable(MagnesiumChlorideMod.FLUID_CANISTER).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return MagnesiumChlorideMod.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
