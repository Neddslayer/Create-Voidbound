package dev.neddslayer.voidbound.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.neddslayer.voidbound.entity.VoidCatalystEntity;
import dev.neddslayer.voidbound.renderer.VFXRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class VoidCatalystRenderer extends EntityRenderer<VoidCatalystEntity> {
    public VoidCatalystRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(VoidCatalystEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        //VFXRenderer.

        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(VoidCatalystEntity voidCatalystEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
