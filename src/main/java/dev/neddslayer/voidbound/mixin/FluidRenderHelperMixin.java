package dev.neddslayer.voidbound.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.neddslayer.voidbound.VoidboundClient;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.createmod.catnip.render.FluidRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.simibubi.create.foundation.fluid.FluidRenderer.renderFluidStream;

@Mixin(FluidRenderHelper.class)
public abstract class FluidRenderHelperMixin<T> {


    @Shadow
    public abstract void renderFluidBox(T fluid, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, VertexConsumer builder, PoseStack ms, int light, boolean renderBottom, boolean invertGasses);

    @Inject(method = "renderFluidBox(Ljava/lang/Object;FFFFFFLnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/PoseStack;IZZ)V", at = @At("HEAD"), cancellable = true)
    private void useRawVoidEssenceShaderHereToo(T fluid, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, MultiBufferSource buffer, PoseStack ms, int light, boolean renderBottom, boolean invertGasses, CallbackInfo ci) {
        if (fluid instanceof FluidStack && ((FluidStack) fluid).getFluidType() == VoidboundFluids.RAW_VOID_ESSENCE.getType()) {
            renderFluidBox(fluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer.getBuffer(VeilRenderType.get(VoidboundClient.RAW_VOID_ESSENCE)), ms, 0xFFFFFF, renderBottom, invertGasses);
            ci.cancel();
        } else if (fluid instanceof FluidStack && ((FluidStack) fluid).getFluidType() == VoidboundFluids.DISTILLED_VOID_ESSENCE.getType()) {
            renderFluidBox(fluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer.getBuffer(VeilRenderType.get(VoidboundClient.DISTILLED_VOID_ESSENCE)), ms, 0xFFFFFF, renderBottom, invertGasses);
            ci.cancel();
        }
    }

}
