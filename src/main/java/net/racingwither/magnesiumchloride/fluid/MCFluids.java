package net.racingwither.magnesiumchloride.fluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;

public class MCFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, MagnesiumChlorideMod.MOD_ID);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOURCE_HYDROCHLORIC_ACID = FLUIDS.register(
            "source_hydrochloric_acid", () -> new BaseFlowingFluid.Source(MCFluids.HYDROCHLORIC_ACID_PROPERTIES)
    );
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_HYDROCHLORIC_ACID = FLUIDS.register(
            "flowing_hydrochloric_acid", () -> new BaseFlowingFluid.Flowing(MCFluids.HYDROCHLORIC_ACID_PROPERTIES)
    );

    public static final DeferredHolder<Fluid, GasFluid> HYDROGEN_GAS = FLUIDS.register(
            "hydrogen_gas", () -> new GasFluid(MCFluidTypes.HYDROGEN_GAS_TYPE)
    );

    public static final DeferredHolder<Fluid, GasFluid> CHLORINE_GAS = FLUIDS.register(
            "chlorine_gas", () -> new GasFluid(MCFluidTypes.CHLORINE_GAS_TYPE)
    );

    public static final BaseFlowingFluid.Properties HYDROCHLORIC_ACID_PROPERTIES = new BaseFlowingFluid.Properties(
        MCFluidTypes.HYDROCHLORIC_ACID_TYPE, SOURCE_HYDROCHLORIC_ACID, FLOWING_HYDROCHLORIC_ACID)
            .slopeFindDistance(6).levelDecreasePerBlock(1).block(MagnesiumChlorideMod.HYDROCHLORIC_ACID).bucket(MagnesiumChlorideMod.ACID_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }

    public static void registerFluidInteractions() {
        FluidInteractionRegistry.InteractionInformation interactionInformation = new FluidInteractionRegistry.InteractionInformation(MCFluidTypes.HYDROCHLORIC_ACID_TYPE.get(),
                (fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : MagnesiumChlorideMod.MAGNESIUM_ORE.get().defaultBlockState()));

        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), interactionInformation);
    }
}
