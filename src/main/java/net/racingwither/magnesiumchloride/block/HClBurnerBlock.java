package net.racingwither.magnesiumchloride.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.racingwither.magnesiumchloride.block.entity.HClBurnerBlockEntity;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;
import net.racingwither.magnesiumchloride.block.entity.HClBurnerFluidHandler;
import org.jetbrains.annotations.Nullable;

public class HClBurnerBlock extends BaseEntityBlock {

    public static final MapCodec<HClBurnerBlock> CODEC = simpleCodec(HClBurnerBlock::new);

    public HClBurnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HClBurnerBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
//        if (state.getBlock() != newState.getBlock()) {
//            BlockEntity blockEntity = level.getBlockEntity(pos);
//            if (blockEntity instanceof HClBurnerBlockEntity hClBurnerBlockEntity) {
//                hClBurnerBlockEntity.drops();
//            }
//        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof IFluidHandler fluidHandler) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof HClBurnerBlockEntity hClBurner) {
                HClBurnerFluidHandler handler = hClBurner.getHandler();
                FluidStack drained = fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                handler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, MagnesiumChlorideMod.HCL_BURNER_BLOCK_ENTITY.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }
}
