package dev.neddslayer.voidbound.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.neddslayer.voidbound.blockentity.VoidCatalystAnchorBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;

public class VoidCatalystAnchorRenderer extends KineticBlockEntityRenderer<VoidCatalystAnchorBlockEntity> {
    private final ItemRenderer itemRenderer;

    public VoidCatalystAnchorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    protected void renderSafe(VoidCatalystAnchorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        ms.pushPose();
        ms.scale(.5f, .5f, .5f);
        ms.translate(1, 1.5, 1);
        ms.mulPose(new Quaternionf().rotateY((be.age() + partialTicks) * 0.1f));

        this.itemRenderer.renderStatic(new ItemStack(Items.NETHER_STAR), ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.getLevel(), be.hashCode());

        ms.popPose();

        if (be.tankInventory.isEmpty()) return;

        LerpedFloat fluidLevel = be.getFluidLevel();
        if (fluidLevel == null)
            return;

        float tankHullWidth = 1 / 16f + 1 / 128f;
        float minPuddleHeight = 1 / 8f;
        float totalHeight = 0.5f - minPuddleHeight;

        float level = fluidLevel.getValue(partialTicks);
        if (level < 1 / (512f * totalHeight))
            return;
        float clampedLevel = Mth.clamp(level * totalHeight, 0, totalHeight);

        float xMin = tankHullWidth;
        float xMax = xMin + 1 - 2 * tankHullWidth;
        float yMin = totalHeight + minPuddleHeight - clampedLevel;
        float yMax = yMin + clampedLevel;

        float zMin = tankHullWidth;
        float zMax = zMin + 1 - 2 * tankHullWidth;

        ms.pushPose();
        ms.translate(0, clampedLevel - totalHeight, 0);

        NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(be.tankInventory.getFluid(), xMin, yMin, zMin, xMax, yMax, zMax, buffer,
                ms, light, false, false);
        ms.popPose();
    }
}
