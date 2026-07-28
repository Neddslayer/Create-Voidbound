package dev.neddslayer.voidbound.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.renderer.entity.LivingEntityProjectionRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, X extends Entity> extends EntityRenderer<T> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    protected void voidbound$getRenderType(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
        if (LivingEntityProjectionRenderer.renderingProjection) {
            cir.setReturnValue(RenderType.entityCutoutNoCull(Voidbound.path("textures/entity/gold_statue.png")));
        }
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    public void voidbound$setStatue(EntityModel instance, X var1, float var2, float var3, float var4, float var5, float var6) {
        if (LivingEntityProjectionRenderer.renderingProjection) {
            float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
            float f = Mth.rotLerp(partialTicks, LivingEntityProjectionRenderer.currentProjection.yBodyRotO, LivingEntityProjectionRenderer.currentProjection.yBodyRot);
            float f1 = Mth.rotLerp(partialTicks, LivingEntityProjectionRenderer.currentProjection.yHeadRotO, LivingEntityProjectionRenderer.currentProjection.yHeadRot);
            float f2 = f1 - f;
            float f6 = Mth.lerp(partialTicks, LivingEntityProjectionRenderer.currentProjection.xRotO, LivingEntityProjectionRenderer.currentProjection.getXRot());
            HumanoidModel.ArmPose leftArmOriginal = null;
            HumanoidModel.ArmPose rightArmOriginal = null;
            if (instance instanceof HumanoidModel<?> model) {
                leftArmOriginal = model.leftArmPose;
                rightArmOriginal = model.rightArmPose;
                model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
                model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
            }
            instance.setupAnim(LivingEntityProjectionRenderer.currentProjection, 0, 0, var4, f2, f6);
            if (instance instanceof HumanoidModel<?> model) {
                model.leftArmPose = leftArmOriginal;
                model.rightArmPose = rightArmOriginal;
            }
        } else {
            instance.setupAnim(var1, var2, var3, var4, var5, var6);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFFFF)V"))
    public void voidbound$statueLayers(RenderLayer instance, PoseStack poseStack, MultiBufferSource bufferSource, int i, X t, float v1, float v2, float v3, float v4, float v5, float v6) {
        if (!LivingEntityProjectionRenderer.renderingProjection) {
            instance.render(poseStack, bufferSource, i, t, v1, v2, v3, v4, v5, v6);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFFF)V"))
    public void voidbound$statueRotations(LivingEntityRenderer instance, T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        if (LivingEntityProjectionRenderer.renderingProjection) {
            float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
            float f = Mth.rotLerp(partialTicks, LivingEntityProjectionRenderer.currentProjection.yBodyRotO, LivingEntityProjectionRenderer.currentProjection.yBodyRot);
            ((LivingEntityRendererMixin<T, X>) (Object) instance).callSetupRotations(entity, poseStack, bob, f, partialTick, scale);
        } else {
            ((LivingEntityRendererMixin<T, X>) (Object) instance).callSetupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getAttackAnim(Lnet/minecraft/world/entity/LivingEntity;F)F"))
    public float voidbound$statueAttack(LivingEntityRenderer instance, T livingBase, float partialTickTime) {
        if (LivingEntityProjectionRenderer.renderingProjection) {
            return 0;
        } else {
            return livingBase.getAttackAnim(partialTickTime);
        }
    }

    @Invoker
    public abstract void callSetupRotations(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale);
}
