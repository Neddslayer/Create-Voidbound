package dev.neddslayer.voidbound.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.entity.VoidCatalystEntity;
import dev.neddslayer.voidbound.renderer.VFXRenderer;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.particle.ParticleEmitter;
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
        if (p_entity.emitter == null && !p_entity.isRemoved()) {
            ParticleEmitter emitter = VeilRenderSystem.renderer().getParticleManager().createEmitter(Voidbound.path("vortex"));
            if (emitter != null) {
                emitter.setAttachedEntity(p_entity);
                VeilRenderSystem.renderer().getParticleManager().addParticleSystem(emitter);
                p_entity.emitter = emitter;
            }
        }

        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(VoidCatalystEntity voidCatalystEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
