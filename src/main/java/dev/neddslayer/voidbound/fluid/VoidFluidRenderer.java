package dev.neddslayer.voidbound.fluid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

import static net.minecraft.world.level.material.FlowingFluid.FALLING;

public class VoidFluidRenderer {

    public static void render(BlockEntity entity, PoseStack poseStack, MultiBufferSource multiBufferSource, RenderType renderType) {
        if (renderType == null) {
            return;
        }
        int levelAttr = entity.getBlockState().getValue(LiquidBlock.LEVEL);

        float level = (float) (0.9 - Math.pow(levelAttr / 8.0f, 1) * 0.85);

        boolean renderTop = true;
        boolean renderNorth = true;
        boolean renderSouth = true;
        boolean renderEast = true;
        boolean renderWest = true;


        Level world = entity.getLevel();
        BlockPos pos = entity.getBlockPos();

        Block checkBlock = entity.getBlockState().getBlock();
        BlockState belowBlock = world.getBlockState(pos.below());
        boolean renderBottom = !Block.isFaceFull(belowBlock.getOcclusionShape(world, pos.below()), Direction.UP);


        if (checkSameHeightFluidState(world, pos.north(), levelAttr, checkBlock)) renderNorth = false;
        if (checkSameHeightFluidState(world, pos.south(), levelAttr, checkBlock)) renderSouth = false;
        if (checkSameHeightFluidState(world, pos.east(), levelAttr, checkBlock)) renderEast = false;
        if (checkSameHeightFluidState(world, pos.west(), levelAttr, checkBlock)) renderWest = false;
        if (entity.getBlockState().getFluidState().getValue(FALLING)) {
            level = 1.0f;
            renderBottom = true;
            // render all other angles to prevent gaps
            renderNorth = true;
            renderSouth = true;
            renderEast = true;
            renderWest = true;
        }




        VertexConsumer builder = multiBufferSource.getBuffer(renderType);
        Matrix4f pose = poseStack.last().pose();
        //top
        if (renderTop) {
            vtx(builder, pose, 0, level, 1, Direction.UP);
            vtx(builder, pose, 1, level, 1, Direction.UP);
            vtx(builder, pose, 1, level, 0, Direction.UP);
            vtx(builder, pose, 0, level, 0, Direction.UP);
        }
        //north
        if (renderNorth) {
            vtx(builder, pose, 0, level, 0, Direction.NORTH);
            vtx(builder, pose, 1, level, 0, Direction.NORTH);
            vtx(builder, pose, 1, 0, 0, Direction.NORTH);
            vtx(builder, pose, 0, 0, 0, Direction.NORTH);
        }
        //south
        if (renderSouth) {
            vtx(builder, pose, 0, 0, 1, Direction.SOUTH);
            vtx(builder, pose, 1, 0, 1, Direction.SOUTH);
            vtx(builder, pose, 1, level, 1, Direction.SOUTH);
            vtx(builder, pose, 0, level, 1, Direction.SOUTH);
        }
        //east
        if (renderEast) {
            vtx(builder, pose, 1, level, 0, Direction.EAST);
            vtx(builder, pose, 1, level, 1, Direction.EAST);
            vtx(builder, pose, 1, 0, 1, Direction.EAST);
            vtx(builder, pose, 1, 0, 0, Direction.EAST);
        }
        //west
        if (renderWest) {
            vtx(builder, pose, 0, 0, 0, Direction.WEST);
            vtx(builder, pose, 0, 0, 1, Direction.WEST);
            vtx(builder, pose, 0, level, 1, Direction.WEST);
            vtx(builder, pose, 0, level, 0, Direction.WEST);
        }
        if (renderBottom) {
            vtx(builder, pose, 0, 0, 0, Direction.DOWN);
            vtx(builder, pose, 1, 0, 0, Direction.DOWN);
            vtx(builder, pose, 1, 0, 1, Direction.DOWN);
            vtx(builder, pose, 0, 0, 1, Direction.DOWN);
        }
    }

    public static void renderSmallLittleCubeAwww(VertexConsumer fluidConsumer, Matrix4f pose) {
        // topside
        vtx(fluidConsumer, pose, 0.32f, 0.1667f, 0.68f, Direction.UP);
        vtx(fluidConsumer, pose, 0.68f, 0.1667f, 0.68f, Direction.UP);
        vtx(fluidConsumer, pose, 0.68f, 0.1667f, 0.32f, Direction.UP);
        vtx(fluidConsumer, pose, 0.32f, 0.1667f, 0.32f, Direction.UP);

        vtx(fluidConsumer, pose, 0.32f, 0.1667f, 0.32f, Direction.NORTH);
        vtx(fluidConsumer, pose, 0.68f, 0.1667f, 0.32f, Direction.NORTH);
        vtx(fluidConsumer, pose, 0.68f, -0.1667f, 0.32f, Direction.NORTH);
        vtx(fluidConsumer, pose, 0.32f, -0.1667f, 0.32f, Direction.NORTH);

        vtx(fluidConsumer, pose, 0.32f, -0.1667f, 0.68f, Direction.SOUTH);
        vtx(fluidConsumer, pose, 0.68f, -0.1667f, 0.68f, Direction.SOUTH);
        vtx(fluidConsumer, pose, 0.68f, 0.1667f, 0.68f, Direction.SOUTH);
        vtx(fluidConsumer, pose, 0.32f, 0.1667f, 0.68f, Direction.SOUTH);

        vtx(fluidConsumer, pose, 0.68f, 0.1667f, 0.32f, Direction.EAST);
        vtx(fluidConsumer, pose, 0.68f, 0.1667f, 0.68f, Direction.EAST);
        vtx(fluidConsumer, pose, 0.68f, -0.1667f, 0.68f, Direction.EAST);
        vtx(fluidConsumer, pose, 0.68f, -0.1667f, 0.32f, Direction.EAST);

        vtx(fluidConsumer, pose, 0.32f, -0.1667f, 0.32f, Direction.WEST);
        vtx(fluidConsumer, pose, 0.32f, -0.1667f, 0.68f, Direction.WEST);
        vtx(fluidConsumer, pose, 0.32f, 0.1667f, 0.68f, Direction.WEST);
        vtx(fluidConsumer, pose, 0.32f, 0.1667f, 0.32f, Direction.WEST);

        vtx(fluidConsumer, pose, 0.32f, -0.1667f, 0.32f, Direction.DOWN);
        vtx(fluidConsumer, pose, 0.68f, -0.1667f, 0.32f, Direction.DOWN);
        vtx(fluidConsumer, pose, 0.68f, -0.1667f, 0.68f, Direction.DOWN);
        vtx(fluidConsumer, pose, 0.32f, -0.1667f, 0.68f, Direction.DOWN);
    }

    private static void vtx(VertexConsumer builder, Matrix4f pose, float x, float y, float z, Direction direction) {
        Vec3i normal = direction.getNormal();
        builder.addVertex(pose, x,y,z).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1.0F, 0.0F).setLight(0xFFFFFF).setNormal(normal.getX(), normal.getY(), normal.getZ());
    }

    private static boolean checkSameHeightFluidState(Level world, BlockPos pos, int level, Block check) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == check && state.getValue(LiquidBlock.LEVEL) <= level;
    }

}
