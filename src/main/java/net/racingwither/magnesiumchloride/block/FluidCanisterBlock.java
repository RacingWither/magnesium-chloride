package net.racingwither.magnesiumchloride.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.racingwither.magnesiumchloride.block.entity.FluidCanisterBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FluidCanisterBlock extends BaseEntityBlock {

public static final MapCodec<FluidCanisterBlock> CODEC = simpleCodec(FluidCanisterBlock::new);
private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);

    public FluidCanisterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidCanisterBlockEntity(blockPos, blockState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean isClientSide = level.isClientSide;

        if (stack.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof FluidCanisterBlockEntity))
            return ItemInteractionResult.FAIL;

        IFluidHandler canisterCapability = level.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity.getBlockPos(), null);
        if (canisterCapability == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        FluidStack existingCanisterFluid = canisterCapability.getFluidInTank(0).copy();

        IFluidHandlerItem itemCapability = stack.getCapability(Capabilities.FluidHandler.ITEM, null);
        if (itemCapability == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        FluidStack existingItemFluid = itemCapability.getFluidInTank(0).copy();

        SoundEvent event = null;

        if (existingItemFluid.isEmpty() && existingCanisterFluid.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        else if (existingItemFluid.isEmpty()) {
            ItemStack split = stack.copy();
            split.setCount(1);
            IFluidHandlerItem splitCapability = split.getCapability(Capabilities.FluidHandler.ITEM);
            if (splitCapability != null) {
                if (tryExecuteFill(canisterCapability, splitCapability, 1000)) {
                    blockEntity.setChanged();

                    ItemStack container = splitCapability.getContainer().copy();

                    if (stack.getCount() == 1) {
                        player.setItemInHand(hand, container);
                    } else {
                        stack.shrink(1);
                        player.getInventory().placeItemBackInInventory(container);
                    }

                    if (!isClientSide) {
                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                    }
                    return ItemInteractionResult.sidedSuccess(isClientSide);
                }
                return ItemInteractionResult.FAIL;
            }

        } else if (existingCanisterFluid.isEmpty()) {
            if (tryExecuteFill(itemCapability, canisterCapability, 1000)) {
                blockEntity.setChanged();

                ItemStack container = itemCapability.getContainer();
                player.setItemInHand(hand, container);

                event = SoundEvents.BUCKET_EMPTY;

                if (!isClientSide) {
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                }
                return ItemInteractionResult.sidedSuccess(isClientSide);
            }
            return ItemInteractionResult.FAIL;

        } else if (existingItemFluid.is(existingCanisterFluid.getFluidType())) {
            FluidStack simulatedDrain = itemCapability.drain(1000, IFluidHandler.FluidAction.SIMULATE);
            int simulatedFill = canisterCapability.fill(simulatedDrain, IFluidHandler.FluidAction.SIMULATE);
            if (!simulatedDrain.isEmpty() && simulatedFill == 1000) {
                FluidStack drained = itemCapability.drain(simulatedDrain, IFluidHandler.FluidAction.EXECUTE);
                canisterCapability.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                blockEntity.setChanged();

                event = SoundEvents.BUCKET_EMPTY;

            }
        } else
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        //if (event != null && !isClientSide) {
        //    level.playSound(null, pos, event, SoundSource.BLOCKS);
        //}


        return ItemInteractionResult.SUCCESS;
    }


    private static boolean tryExecuteFill(IFluidHandler source, IFluidHandler target, int maxAmount) {
        FluidStack simDrain = source.drain(maxAmount, IFluidHandler.FluidAction.SIMULATE);
        int drainAmount = simDrain.getAmount();
        if (simDrain.isEmpty()) return false;

        int fillAmount = target.fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
        if (fillAmount == 0) return false;

        int transferAmount = Math.min(drainAmount, fillAmount);
        FluidStack exDrain = source.drain(transferAmount, IFluidHandler.FluidAction.EXECUTE);
        target.fill(exDrain, IFluidHandler.FluidAction.EXECUTE);
        return true;
    }
}
