package net.racingwither.magnesiumchloride.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;
import net.racingwither.magnesiumchloride.fluid.ModifiedFluidTank;

public class FluidTankItem extends ModifiedFluidTank implements IFluidHandlerItem {
    private final ItemStack container;
    public FluidTankItem(int capacity, ItemStack container) {
        super(capacity);
        this.container = container;
    }

    @Override
    public ItemStack getContainer() {
        return container;
    }

    @Override
    protected void onContentsChanged() {
        FluidCanisterItem.onFluidChange(this, this.getContainer());
        super.onContentsChanged();
    }
}
