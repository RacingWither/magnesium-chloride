package net.racingwither.magnesiumchloride.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.racingwither.magnesiumchloride.block.entity.FluidCanisterBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FluidCanisterBlock extends FluidContainingEntityBlock {

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
}
