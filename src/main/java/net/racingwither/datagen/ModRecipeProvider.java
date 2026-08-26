package net.racingwither.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(MagnesiumChlorideMod.RAW_MAGNESIUM),
                RecipeCategory.MISC,
                MagnesiumChlorideMod.MAGNESIUM,
                0.1f,
                100
        )
                .unlockedBy("has_raw_magnesium", has(MagnesiumChlorideMod.RAW_MAGNESIUM))
                .save(recipeOutput, "magnesium_blasting");

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(MagnesiumChlorideMod.RAW_MAGNESIUM),
                RecipeCategory.MISC,
                MagnesiumChlorideMod.MAGNESIUM,
                0.1f,
                200
        )
                .unlockedBy("has_raw_magnesium", has(MagnesiumChlorideMod.RAW_MAGNESIUM))
                .save(recipeOutput, "magnesium_smelting");
    }
}
