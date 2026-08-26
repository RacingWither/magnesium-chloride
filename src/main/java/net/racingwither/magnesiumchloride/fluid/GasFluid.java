package net.racingwither.magnesiumchloride.fluid;

import net.minecraft.world.level.material.EmptyFluid;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Supplier;

public class GasFluid extends EmptyFluid {

    private final Supplier<? extends FluidType> fluidType;

    public GasFluid(Supplier<? extends FluidType> fluidType) {
        this.fluidType = fluidType;
    }

    @Override
    public FluidType getFluidType() {
        return this.fluidType.get();
    }
}
