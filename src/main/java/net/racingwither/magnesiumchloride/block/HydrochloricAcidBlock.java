package net.racingwither.magnesiumchloride.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.Optional;

public class HydrochloricAcidBlock extends LiquidBlock {

    public HydrochloricAcidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }
}