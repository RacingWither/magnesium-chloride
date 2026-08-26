package net.racingwither.magnesiumchloride.block.entity;

import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class HClBurnerFluidHandler implements IFluidHandler {

    private final FluidTank input;
    private final FluidTank input1;
    private final FluidTank output;

    public HClBurnerFluidHandler() {
        input = new FluidTank(8000);
        input1 = new FluidTank(8000);
        output = new FluidTank(8000);
    }

    @Override
    public int getTanks() {
        return 3;
    }

    public FluidTank getFluidTank(int tank) {
        return switch (tank) {
            case 0 -> input;
            case 1 -> input1;
            case 2 -> output;
            default -> input;
        };
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return switch (tank) {
            case 0 -> input.getFluid();
            case 1 -> input1.getFluid();
            case 2 -> output.getFluid();
            default -> FluidStack.EMPTY;
        };
    }

    @Override
    public int getTankCapacity(int tank) {
        return 8000;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return stack.is(Tags.Fluids.GASEOUS);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        for (FluidTank tank : List.of(input, input1)) {
            if (tank.isEmpty() || tank.getFluid().is(resource.getFluidType())) {
                return tank.fill(resource, action);
            }
        }

        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        for (FluidTank tank : List.of(input, input1, output)) {
            if (!tank.isEmpty() && tank.getFluid().is(resource.getFluidType())) {
                return tank.drain(resource, action);
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (FluidTank tank : List.of(input, input1, output)) {
            if (!tank.isEmpty()) {
                return tank.drain(maxDrain, action);
            }
        }
        return FluidStack.EMPTY;
    }
}
