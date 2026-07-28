package dev.neddslayer.voidbound.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.neddslayer.voidbound.renderer.VFXRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

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
}
