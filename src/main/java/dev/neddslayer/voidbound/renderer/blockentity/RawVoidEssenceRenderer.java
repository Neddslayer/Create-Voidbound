package dev.neddslayer.voidbound.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.neddslayer.voidbound.VoidboundClient;
import dev.neddslayer.voidbound.blockentity.RawVoidEssenceBlockEntity;
import dev.neddslayer.voidbound.fluid.VoidFluidRenderer;
import dev.neddslayer.voidbound.renderer.IRenderLate;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class RawVoidEssenceRenderer implements BlockEntityRenderer<RawVoidEssenceBlockEntity>, IRenderLate {
    public RawVoidEssenceRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public boolean shouldRender(RawVoidEssenceBlockEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    public void render(RawVoidEssenceBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        RenderType renderType = VeilRenderType.get(VoidboundClient.RAW_VOID_ESSENCE);
        VoidFluidRenderer.render(be, ms, bufferSource, renderType);
    }
}
