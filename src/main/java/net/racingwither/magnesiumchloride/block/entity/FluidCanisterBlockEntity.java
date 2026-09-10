package net.racingwither.magnesiumchloride.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;
import net.racingwither.magnesiumchloride.component.FluidStorageComponent;
import org.jetbrains.annotations.Nullable;

public class FluidCanisterBlockEntity extends BlockEntity {

    private final FluidTank tank;

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

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        FluidStorageComponent.FluidStorageRecord record = componentInput.get(MagnesiumChlorideMod.FLUID_STORAGE_COMPONENT);
        FluidTank tank = this.getTank();
        tank.setFluid(record.fluid());
        tank.setCapacity(record.capacity());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        components.set(MagnesiumChlorideMod.FLUID_STORAGE_COMPONENT, new FluidStorageComponent.FluidStorageRecord(tank.getFluid(), tank.getCapacity()));
    }
}