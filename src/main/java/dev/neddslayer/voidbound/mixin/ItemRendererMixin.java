package dev.neddslayer.voidbound.mixin;

import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.VoidboundClient;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;getRenderTypes(Lnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;"))
    private List<RenderType> customRenderType(BakedModel instance, ItemStack itemStack, boolean b) {
        if (itemStack.getItem() == VoidboundItems.PURIFICATION_CRYSTAL.get()) {
            return List.of(VeilRenderType.get(VoidboundClient.PURIFICATION_CRYSTAL));
        } else if (itemStack.getItem() == VoidboundFluids.distilledVoidEssenceBucket()) {
            return List.of(VeilRenderType.get(VoidboundClient.DISTILLED_VOID_ESSENCE_BUCKET));
        } else if (itemStack.getItem() == VoidboundFluids.rawVoidEssenceBucket()) {
            return List.of(VeilRenderType.get(VoidboundClient.RAW_VOID_ESSENCE_BUCKET));
        } else {
            return instance.getRenderTypes(itemStack, b);
        }
    }
}
