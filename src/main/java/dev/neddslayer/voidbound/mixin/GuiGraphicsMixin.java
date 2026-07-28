package dev.neddslayer.voidbound.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import dev.neddslayer.voidbound.renderer.VFXRenderer;
import foundry.veil.api.client.render.VeilRenderBridge;
import foundry.veil.api.client.render.VeilRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V", shift = At.Shift.AFTER))
    private void voidbound$advancementBackground(ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV, CallbackInfo ci) {
        if (atlasLocation.getNamespace().equals("voidbound")) {
            ShaderInstance instance = VeilRenderBridge.toShaderInstance(VeilRenderSystem.renderer().getShaderManager().getShader(Voidbound.path("gui/advancement")));
            RenderSystem.setShader(() -> instance);
        }

        if (VFXRenderer.renderingHeart && Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(VoidboundItems.ASTRAL_PROJECTION.getDelegate())) {
            ShaderInstance instance = VeilRenderBridge.toShaderInstance(VeilRenderSystem.renderer().getShaderManager().getShader(Voidbound.path("gui/heart")));
            RenderSystem.setShader(() -> instance);
        }
    }

}
