package dev.neddslayer.voidbound.mixin;

import dev.neddslayer.voidbound.renderer.VFXRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "renderHeart", at = @At("HEAD"))
    private void voidbound$markHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, boolean hardcore, boolean halfHeart, boolean blinking, CallbackInfo ci) {
        VFXRenderer.renderingHeart = true;
    }

    @Inject(method = "renderHeart", at = @At("TAIL"))
    private void voidbound$unmarkHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, boolean hardcore, boolean halfHeart, boolean blinking, CallbackInfo ci) {
        VFXRenderer.renderingHeart = false;
    }

}
