package dev.neddslayer.voidbound.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.neddslayer.voidbound.VoidboundClient;
import dev.neddslayer.voidbound.blockentity.AttractionCoilBlockEntity;
import dev.neddslayer.voidbound.fluid.VoidFluidRenderer;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class AttractionCoilRenderer extends KineticBlockEntityRenderer<AttractionCoilBlockEntity> {

    public AttractionCoilRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(AttractionCoilBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        ms.pushPose();
        Vec3i normal = be.getBlockState().getValue(FACING).getNormal();
        Vec3 normalMul = new Vec3(normal.getX(), normal.getY(), normal.getZ()).add(0, 0.5f, 0);
        ms.translate(normalMul.x, normalMul.y, normalMul.z);

        Matrix4f pose = ms.last().pose();
        VertexConsumer fluidConsumer = buffer.getBuffer(VeilRenderType.get(VoidboundClient.DISTILLED_VOID_ESSENCE));
        VoidFluidRenderer.renderSmallLittleCubeAwww(fluidConsumer, pose);
        ms.popPose();
    }

    @Override
    protected SuperByteBuffer getRotatedModel(AttractionCoilBlockEntity be, BlockState state) {
        Direction facing = state.getValue(BlockStateProperties.FACING).getOpposite();
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, facing);
    }
}
