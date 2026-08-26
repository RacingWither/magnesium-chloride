package net.racingwither.magnesiumchloride.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;

public class FluidCanisterBlockEntity extends BlockEntity {

    private FluidTank tank;

    public FluidCanisterBlockEntity(BlockPos pos, BlockState blockState) {
        super(MagnesiumChlorideMod.FLUID_CANISTER_BLOCK_ENTITY.get(), pos, blockState);
        tank = new FluidTank(1000);
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {

        tag = tank.writeToNBT(registries, tag);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.tank = tank.readFromNBT(registries, tag);
    }
}