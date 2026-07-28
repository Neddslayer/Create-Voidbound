package dev.neddslayer.voidbound.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.neddslayer.voidbound.entity.LivingEntityProjectionEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class LivingEntityProjectionRenderer extends EntityRenderer<LivingEntityProjectionEntity> {
    public static boolean renderingProjection = false;
    public static LivingEntityProjectionEntity currentProjection = null;

    public LivingEntityProjectionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(LivingEntityProjectionEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Entity entity = p_entity.level().getEntity(p_entity.getEntityData().get(LivingEntityProjectionEntity.TARGET_ENTITY));
        if (entity != null) {
            renderingProjection = true;
            currentProjection = p_entity;
            entityRenderDispatcher.getRenderer(entity).render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
            renderingProjection = false;
            currentProjection = null;
        }
    }

    @Override
    public boolean shouldRender(LivingEntityProjectionEntity p_entity, Frustum camera, double camX, double camY, double camZ) {
        Entity entity = p_entity.level().getEntity(p_entity.getEntityData().get(LivingEntityProjectionEntity.TARGET_ENTITY));
        if (entity != null) {
            return entityRenderDispatcher.getRenderer(entity).shouldRender(p_entity, camera, camX, camY, camZ);
        }
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityProjectionEntity livingEntityProjectionEntity) {
        return null;
    }
}
