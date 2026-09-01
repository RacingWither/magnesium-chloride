package net.racingwither.magnesiumchloride.block.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class HClBurnerFluidHandler implements IFluidHandler {

    public List<FluidTank> TANKS;

    public HClBurnerFluidHandler() {
        TANKS = new ArrayList<>(List.of());

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            TANKS.add(new FluidTank(8000) {
                @Override
                public FluidTank readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
                    fluid = FluidStack.parseOptional(lookupProvider, nbt.getCompound("fluid:tank" + finalI));
                    return this;
                }

                @Override
                public CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
                    nbt.put("fluid:tank" + finalI, fluid.saveOptional(lookupProvider));
                    return nbt;
                }
            });
        }
    }

    @Override
    public int getTanks() {
        return TANKS.size();
    }

    public FluidTank getFluidTank(int tank) {
        return TANKS.get(tank);
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return TANKS.get(tank).getFluid().copy();
    }

    @Override
    public int getTankCapacity(int tank) {
        return TANKS.get(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return stack.is(Tags.Fluids.GASEOUS);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {

        for (FluidTank tank : TANKS.subList(0, 2)) {
            if (!tank.isEmpty() && FluidStack.isSameFluidSameComponents(tank.getFluid(), resource) && (tank.getCapacity() != tank.getFluid().getAmount())) return tank.fill(resource, action);
        }
        for (FluidTank tank : TANKS.subList(0, 2)) {
            if (tank.isEmpty()) return tank.fill(resource, action);
        }
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        for (FluidTank tank : TANKS.reversed()) {
            if (!tank.isEmpty() && FluidStack.isSameFluidSameComponents(tank.getFluid(), resource)) return tank.drain(resource, action);
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (FluidTank tank : TANKS.reversed()) {
            if (!tank.isEmpty()) return tank.drain(maxDrain, action);
        }
        return FluidStack.EMPTY;
    }
}
