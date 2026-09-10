package net.racingwither.magnesiumchloride.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;
import net.racingwither.magnesiumchloride.component.FluidStorageComponent;
import net.racingwither.magnesiumchloride.fluid.BaseFluidType;

import java.util.List;

public class FluidCanisterItem extends BlockItem {

    private FluidTankItem tank;
    public long lastUsed = 0;

    public FluidCanisterItem(Block block, Properties properties) {
        super(block, properties);
    }

    public FluidTankItem getFluidHandler(ItemStack container) {
        FluidTankItem tank = new FluidTankItem(container.get(MagnesiumChlorideMod.FLUID_STORAGE_COMPONENT).capacity(), container);
        tank.setFluid(container.get(MagnesiumChlorideMod.FLUID_STORAGE_COMPONENT).fluid());
        return tank;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        IFluidHandler capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability != null) {
            return !capability.getFluidInTank(0).isEmpty();
        } return false;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        FluidStack fluid = this.getFluidHandler(stack).getFluid();
        if (fluid.is(Fluids.WATER)) return 0x3F76E4;
        if (fluid.is(Fluids.LAVA)) return 0xff540a;
        if (fluid.getFluidType() instanceof BaseFluidType base) return base.getTintColor();
        return 0xffffff;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        IFluidHandler capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability != null) {
            FluidStack fluid = capability.getFluidInTank(0);
            int amount = fluid.getAmount();
            int capacity = capability.getTankCapacity(0);
            float f = (float) amount / capacity;
            float f1 = f * 13;
            return Math.round(f1);
        }
        return 13;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        IFluidHandlerItem capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability != null) {
            tooltipComponents.add(Component.empty()
                    .append(Component.literal(String.valueOf(capability.getFluidInTank(0).getAmount())).withColor(this.getBarColor(stack))
                    .append("mB ")
                    .append(capability.getFluidInTank(0).getHoverName().copy().withColor(this.getBarColor(stack)))));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        IFluidHandler capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability != null) if (!capability.getFluidInTank(0).isEmpty()) return 1;
        return super.getMaxStackSize(stack);
    }


    public static void onFluidChange(FluidTankItem tank, ItemStack stack) {
        if (!stack.is(MagnesiumChlorideMod.FLUID_CANISTER_ITEM)) return;
        FluidStack fluid = tank.getFluid();
        stack.update(
                MagnesiumChlorideMod.FLUID_STORAGE_COMPONENT,
                FluidStorageComponent.FluidStorageRecord.CANISTER_DEFAULT,
                storage -> storage.setValue(fluid)
        );
    }
}