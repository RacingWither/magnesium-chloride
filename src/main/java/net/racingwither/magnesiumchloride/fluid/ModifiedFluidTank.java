package net.racingwither.magnesiumchloride.fluid;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class ModifiedFluidTank extends FluidTank {
    public ModifiedFluidTank(int capacity) {
        super(capacity);
    }

    protected ModifiedFluidTank(FluidStack fluidStack, int capacity) {
        super(capacity);
        this.fluid = fluidStack;
    }

    @Override
    public FluidStack getFluid() {
        return fluid.copy();
    }

    @Override
    public CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        nbt.put("Fluid", fluid.saveOptional(lookupProvider));
        return nbt;
    }

    @Override
    public void setFluid(FluidStack stack) {
        super.setFluid(stack);
        this.onContentsChanged();
    }
}
