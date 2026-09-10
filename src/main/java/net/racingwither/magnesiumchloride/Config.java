package net.racingwither.magnesiumchloride;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue CANISTER_CAPACITY = BUILDER
            .comment("Number of buckets of fluid held by a Fluid Canister")
            .defineInRange("canisterCapacity", 4, 1, 128);

    static final ModConfigSpec SPEC = BUILDER.build();
}
