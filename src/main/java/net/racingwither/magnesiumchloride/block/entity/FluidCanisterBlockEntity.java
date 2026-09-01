package net.racingwither.magnesiumchloride.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;
import org.jetbrains.annotations.Nullable;

public class FluidCanisterBlockEntity extends BlockEntity {

    private FluidTank tank;

    public FluidCanisterBlockEntity(BlockPos pos, BlockState blockState) {
        super(MagnesiumChlorideMod.FLUID_CANISTER_BLOCK_ENTITY.get(), pos, blockState);
        tank = new FluidTank(4000) {
            @Override
            public FluidStack getFluid() {
                return fluid.copy();
            }

            @Override
            public CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
                nbt.put("Fluid", fluid.saveOptional(lookupProvider));
                return nbt;
            }
        };
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

        tank.readFromNBT(registries, tag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


}