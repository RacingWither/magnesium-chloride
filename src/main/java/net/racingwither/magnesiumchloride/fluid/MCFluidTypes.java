package net.racingwither.magnesiumchloride.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;
import org.joml.Vector3f;

public class MCFluidTypes {

    public static final ResourceLocation STILL_HYDROCHLORIC_ACID_LOCATION = ResourceLocation.withDefaultNamespace("block/water_still");
    public static final ResourceLocation FLOWING_HYDROCHLORIC_ACID_LOCATION = ResourceLocation.withDefaultNamespace("block/water_flow");

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(
            NeoForgeRegistries.FLUID_TYPES, MagnesiumChlorideMod.MOD_ID
    );

    public static final DeferredHolder<FluidType, BaseFluidType> HYDROCHLORIC_ACID_TYPE = FLUID_TYPES.register(
            "hydrochloric_acid", () -> new BaseFluidType(
                    STILL_HYDROCHLORIC_ACID_LOCATION,
                    FLOWING_HYDROCHLORIC_ACID_LOCATION,
                    0x80aaaaaa,
                    new Vector3f(0.5f, 0.5f, 0.5f),
                    FluidType.Properties.create()
                    .canConvertToSource(false)
            )
    );

    public static final DeferredHolder<FluidType, BaseFluidType> HYDROGEN_GAS_TYPE = FLUID_TYPES.register(
            "hydrogen_gas", () -> new BaseFluidType(
                    STILL_HYDROCHLORIC_ACID_LOCATION,
                    FLOWING_HYDROCHLORIC_ACID_LOCATION,
                    0x05000000,
                    new Vector3f(0.5f, 0.5f, 0.5f),
                    FluidType.Properties.create()
            )
    );

    public static final DeferredHolder<FluidType, BaseFluidType> CHLORINE_GAS_TYPE = FLUID_TYPES.register(
            "chlorine_gas", () -> new BaseFluidType(
                    STILL_HYDROCHLORIC_ACID_LOCATION,
                    FLOWING_HYDROCHLORIC_ACID_LOCATION,
                    0x80c3e81e,
                    new Vector3f(0.5f, 0.5f, 0.5f),
                    FluidType.Properties.create()
            )
    );

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}

