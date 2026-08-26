package net.racingwither.magnesiumchloride.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.racingwither.magnesiumchloride.block.entity.FluidCanisterBlockEntity;
import net.racingwither.magnesiumchloride.fluid.BaseFluidType;

import java.util.List;

public class FluidCanisterItem extends BlockItem {

    private final FluidTankItem tank;

    public FluidCanisterItem(Block block, Item.Properties properties) {
        super(block, properties);
        tank = new FluidTankItem(1000, this.getDefaultInstance());
    }

    public IFluidHandlerItem getFluidHandler() {
        return tank;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !this.tank.isEmpty();
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (this.tank.getFluid().getFluidType() instanceof BaseFluidType base) return base.getTintColor();
        if (this.tank.getFluid().is(Fluids.WATER)) return 0x3F76E4;
        if (this.tank.getFluid().is(Fluids.LAVA)) return 0xFE432A;
        return 0xffffff;

    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return 10;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        IFluidHandlerItem capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability != null) {
            tooltipComponents.add(capability.getFluidInTank(0).getHoverName());
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}