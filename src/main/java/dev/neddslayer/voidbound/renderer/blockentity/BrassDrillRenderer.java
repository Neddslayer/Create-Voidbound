package dev.neddslayer.voidbound.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.neddslayer.voidbound.blockentity.BrassDrillBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class BrassDrillRenderer extends KineticBlockEntityRenderer<BrassDrillBlockEntity> {
    public BrassDrillRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(BrassDrillBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel())) return;
        BlockState state = be.getBlockState();
        Direction facing = Direction.UP;
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        renderRotatingBuffer(be, CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, Direction.UP), ms, vb, light);
        renderRotatingBuffer(be, CachedBuffers.partialFacing(VoidboundPartialModels.BRASS_DRILL,
                state, Direction.NORTH), ms, vb, light);
    }
}
