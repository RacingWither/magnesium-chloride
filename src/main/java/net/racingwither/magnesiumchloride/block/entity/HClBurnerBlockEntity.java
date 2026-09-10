package net.racingwither.magnesiumchloride.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;
import net.racingwither.magnesiumchloride.fluid.MCFluids;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HClBurnerBlockEntity extends BlockEntity {

    protected HClBurnerFluidHandler handler;

    public HClBurnerFluidHandler getHandler() {
        return handler;
    }

    private static final int INPUT_H2 = 0;
    private static final int INPUT_Cl2 = 1;
    private static final int OUTPUT_HCl = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    public HClBurnerBlockEntity(BlockPos pos, BlockState blockState) {
        super(MagnesiumChlorideMod.HCL_BURNER_BLOCK_ENTITY.get(), pos, blockState);

        handler = new HClBurnerFluidHandler();

        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> HClBurnerBlockEntity.this.progress;
                    case 1 -> HClBurnerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: HClBurnerBlockEntity.this.progress = value;
                    case 1: HClBurnerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {

        tag = handler.getFluidTank(0).writeToNBT(registries, tag);
        tag = handler.getFluidTank(1).writeToNBT(registries, tag);
        tag = handler.getFluidTank(2).writeToNBT(registries, tag);

        tag.putInt("hcl_burner.progress", this.progress);
        tag.putInt("hcl_burner.max_progress", this.maxProgress);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        handler.getFluidTank(0).readFromNBT(registries, tag);
        handler.getFluidTank(1).readFromNBT(registries, tag);
        handler.getFluidTank(2).readFromNBT(registries, tag);

        progress = tag.getInt("hcl_burner.progress");
        maxProgress = tag.getInt("hcl_burner.max_progress");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (handler != null) {
            FluidStack tank0 = handler.getFluidInTank(0);
            FluidStack tank1 = handler.getFluidInTank(1);
            FluidStack tank2 = handler.getFluidInTank(2);
            if (tank2.getAmount() < handler.getTankCapacity(2) && tank0.getAmount() >= 500 && tank1.getAmount() >= 500) {
                FluidTank inputTank = handler.getTank(0);
                FluidTank inputTank1 = handler.getTank(1);
                FluidTank outputTank = handler.getTank(2);
                List<FluidType> contents = new ArrayList<>(List.of(inputTank.getFluid().getFluidType(), inputTank1.getFluid().getFluidType()));
                if (contents.contains(Fluids.WATER.getFluidType()) && contents.contains(Fluids.LAVA.getFluidType())) {
                    int filledAmount = outputTank.fill(new FluidStack(MCFluids.SOURCE_HYDROCHLORIC_ACID.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
                    inputTank.drain(filledAmount / 2, IFluidHandler.FluidAction.EXECUTE);
                    inputTank1.drain(filledAmount / 2, IFluidHandler.FluidAction.EXECUTE);
                    this.setChanged();
                    if (!level.isClientSide()) {
                        level.sendBlockUpdated(blockPos, blockState, blockState, 2);
                    }
                }
            }
        }
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
