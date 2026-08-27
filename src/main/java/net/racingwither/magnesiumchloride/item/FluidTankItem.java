package net.racingwither.magnesiumchloride.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class FluidTankItem extends FluidTank implements IFluidHandlerItem {
    private final ItemStack container;
    public FluidTankItem(int capacity, ItemStack container) {
        super(capacity);
        this.container = container;
    }

    @Override
    public ItemStack getContainer() {
        return container;
    }


}
