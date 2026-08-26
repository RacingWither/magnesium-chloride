package net.racingwither.magnesiumchloride.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.racingwither.magnesiumchloride.MagnesiumChlorideMod;

public class HClBurnerBlockEntity extends BlockEntity{

    public HClBurnerFluidHandler getHandler() {
        return handler;
    }

    protected HClBurnerFluidHandler handler;

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

    }
}
