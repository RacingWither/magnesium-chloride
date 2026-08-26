package net.racingwither.magnesiumchloride.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector3f;

public class BaseFluidType extends FluidType {

    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final int tintColor;
    private final Vector3f fogColor;

    public BaseFluidType(ResourceLocation still, ResourceLocation flowing, int tint, Vector3f fogColor, Properties properties) {
        super(properties);
        this.stillTexture = still;
        this.flowingTexture = flowing;
        this.tintColor = tint;
        this.fogColor = fogColor;
    }

    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTexture;
    }

    public int getTintColor() {
        return tintColor;
    }

    public Vector3f getFogColor() {
        return fogColor;
    }
}
