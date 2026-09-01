package net.racingwither.magnesiumchloride.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkResult;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.ArrayList;
import java.util.List;

public abstract class FluidContainingEntityBlock extends BaseEntityBlock {
    protected FluidContainingEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean isClientSide = level.isClientSide();

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return ItemInteractionResult.FAIL;

        IFluidHandler blockCapability = level.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity.getBlockPos(), null);
        if (blockCapability == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        List<FluidStack> existingBlockFluids = new ArrayList<>(List.of());

        boolean blockAllEmpty = true;
        boolean blockFull = true;
        for (int i = 0; i < blockCapability.getTanks(); i++) {
            existingBlockFluids.add(blockCapability.getFluidInTank(i).copy());

            if (!blockCapability.getFluidInTank(i).copy().isEmpty()) blockAllEmpty = false;
            if (!(blockCapability.getFluidInTank(i).copy().getAmount() == blockCapability.getTankCapacity(i))) blockFull = false;

        }

        // the main really important check
        IFluidHandlerItem itemCapability = stack.getCapability(Capabilities.FluidHandler.ITEM, null);
        if (itemCapability == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        FluidStack existingItemFluid = itemCapability.getFluidInTank(0).copy();

        boolean itemFull = existingItemFluid.getAmount() == itemCapability.getTankCapacity(0);

        if ((existingItemFluid.isEmpty() && blockAllEmpty) || ((itemFull) && blockFull)) {
            return ItemInteractionResult.FAIL;
        }

        else if (!itemFull) {
            ItemStack split = stack.copy();
            split.setCount(1);
            IFluidHandlerItem splitCapability = split.getCapability(Capabilities.FluidHandler.ITEM);
            if (splitCapability != null) {
                if (tryExecuteFill(blockCapability, splitCapability, 1000, isClientSide)) {
                    blockEntity.setChanged();
                    if (!isClientSide) {
                        level.sendBlockUpdated(pos, state, state, 2);

                        ItemStack container = splitCapability.getContainer().copy();

                        if (stack.getCount() == 1) {
                            player.setItemInHand(hand, container);
                        } else {
                            stack.shrink(1);
                            player.getInventory().placeItemBackInInventory(container);
                        }
                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                    }
                    return ItemInteractionResult.sidedSuccess(isClientSide);
                }
                return ItemInteractionResult.FAIL;
            }
            return ItemInteractionResult.FAIL;

        } else {
            if (tryExecuteFill(itemCapability, blockCapability, 1000, isClientSide)) {
                blockEntity.setChanged();
                if (!isClientSide) {
                    level.sendBlockUpdated(pos, state, state, 2);
                    ItemStack container = itemCapability.getContainer();
                    player.setItemInHand(hand, container);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
                }
                return ItemInteractionResult.sidedSuccess(isClientSide);
            }
            return ItemInteractionResult.CONSUME;
        }
    }

    private static boolean tryExecuteFill(IFluidHandler source, IFluidHandler target, int maxAmount, boolean isClientSide) {
        FluidStack simDrain = source.drain(maxAmount, IFluidHandler.FluidAction.SIMULATE);
        int drainAmount = simDrain.getAmount();
        if (simDrain.isEmpty()) return false;

        int fillAmount = target.fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
        if (fillAmount == 0) return false;

        if (isClientSide) return true;
        int transferAmount = Math.min(drainAmount, fillAmount);
        FluidStack exDrain = source.drain(transferAmount, IFluidHandler.FluidAction.EXECUTE);
        target.fill(exDrain, IFluidHandler.FluidAction.EXECUTE);
        return true;
    }
}
