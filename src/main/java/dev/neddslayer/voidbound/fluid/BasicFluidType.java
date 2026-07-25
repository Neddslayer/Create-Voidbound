package dev.neddslayer.voidbound.fluid;

import dev.neddslayer.voidbound.Voidbound;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

public class BasicFluidType implements IClientFluidTypeExtensions {

    private final String fluidName;

    public BasicFluidType(String fluidName) {
        this.fluidName = fluidName;
    }

    @Override
    public @NotNull ResourceLocation getStillTexture() {
        return ResourceLocation.fromNamespaceAndPath(Voidbound.MODID, "block/" + this.fluidName + "_still");
    }

    @Override
    public @NotNull ResourceLocation getFlowingTexture() {
        return ResourceLocation.fromNamespaceAndPath(Voidbound.MODID, "block/" + this.fluidName + "_flowing");
    }
}