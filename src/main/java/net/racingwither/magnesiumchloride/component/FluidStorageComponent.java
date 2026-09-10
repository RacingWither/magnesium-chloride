package net.racingwither.magnesiumchloride.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStorageComponent {

    public static final Codec<FluidStorageRecord> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FluidStack.OPTIONAL_CODEC.optionalFieldOf("fluid", FluidStack.EMPTY).forGetter(FluidStorageRecord::fluid),
                    Codec.INT.fieldOf("capacity").forGetter(FluidStorageRecord::capacity)
            ).apply(instance, FluidStorageRecord::new)
    );

    public record FluidStorageRecord(FluidStack fluid, int capacity) {
        public static FluidStorageRecord CANISTER_DEFAULT = new FluidStorageRecord(FluidStack.EMPTY, 4000);

        public FluidStorageRecord setValue(FluidStack fluid) {
            return this.setValue(fluid, this.capacity());
        }

        public FluidStorageRecord setValue(int capacity) {
            return this.setValue(this.fluid(), capacity);
        }

        public FluidStorageRecord setValue(FluidStack fluid, int capacity) {
            return new FluidStorageRecord(fluid, capacity);
        }

    }

}
