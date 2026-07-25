package dev.neddslayer.voidbound.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.VoidboundClient;
import dev.neddslayer.voidbound.renderer.VFXRenderer;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.shader.uniform.ShaderUniform;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Nullable
    private ClientLevel level;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "renderLevel",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/Camera;)V",
                shift = At.Shift.BEFORE
            )
    )
    private void voidbound$renderVFX(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local PoseStack ms, @Local MultiBufferSource.BufferSource buffer) {
        VFXRenderer.renderRepulsionVFX(camera, deltaTracker.getGameTimeDeltaPartialTick(false), ms, buffer, 0, OverlayTexture.NO_OVERLAY);

        VFXRenderer.renderVoidVFX(camera, deltaTracker.getGameTimeDeltaPartialTick(false), ms, buffer, 0, OverlayTexture.NO_OVERLAY);
    }

    @Inject(method = "prepareCullFrustum", at = @At("HEAD"))
    public void voidbound$setupLevelCamera(Vec3 pos, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        Matrix4f modelViewNormalMatrix = frustumMatrix.invert(new Matrix4f());
        ShaderUniform normalMatUniform = VeilRenderSystem.renderer().getShaderManager().getShader(VoidboundClient.REPULSION_SPHERE).getUniform("NormalMat");
        if (normalMatUniform != null) normalMatUniform.setMatrix(modelViewNormalMatrix, true);
        else LOGGER.warn("Failed to find model view normal matrix in repulsion sphere shader");
    }

}
