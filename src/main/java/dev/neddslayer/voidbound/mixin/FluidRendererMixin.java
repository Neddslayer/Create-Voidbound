package dev.neddslayer.voidbound.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import dev.neddslayer.voidbound.VoidboundClient;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.simibubi.create.foundation.fluid.FluidRenderer.renderFluidStream;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {

    @Inject(method = "renderFluidStream(Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/minecraft/core/Direction;FFZLnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/PoseStack;I)V", at = @At("HEAD"), cancellable = true)
    private static void useRawVoidEssenceShader(FluidStack fluidStack, Direction direction, float radius, float progress, boolean inbound, MultiBufferSource buffer, PoseStack ms, int light, CallbackInfo ci) {
        if (fluidStack.getFluidType() == VoidboundFluids.RAW_VOID_ESSENCE.getType()) {
            renderFluidStream(fluidStack, direction, radius, progress, inbound, buffer.getBuffer(VeilRenderType.get(VoidboundClient.RAW_VOID_ESSENCE)), ms, 0xFFFFFF);
            ci.cancel();
        } else if (fluidStack.getFluidType() == VoidboundFluids.DISTILLED_VOID_ESSENCE.getType()) {
            renderFluidStream(fluidStack, direction, radius, progress, inbound, buffer.getBuffer(VeilRenderType.get(VoidboundClient.DISTILLED_VOID_ESSENCE)), ms, 0xFFFFFF);
            ci.cancel();
        }
    }



}
