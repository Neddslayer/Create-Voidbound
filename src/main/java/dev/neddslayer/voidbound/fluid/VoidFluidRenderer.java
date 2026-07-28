package dev.neddslayer.voidbound.fluid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.textures.FluidSpriteCache;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.block.LiquidBlockRenderer.shouldRenderFace;

public class VoidFluidRenderer {

    private static boolean isNeighborSameFluid(FluidState firstState, FluidState secondState) {
        return secondState.getType().isSame(firstState.getType());
    }

    private static boolean isNeighborStateHidingOverlay(FluidState selfState, BlockState otherState, Direction neighborFace) {
        return otherState.shouldHideAdjacentFluidFace(neighborFace, selfState);
    }

    private static boolean isFaceOccludedByState(BlockGetter level, Direction face, float height, BlockPos pos, BlockState state) {
        if (state.canOcclude()) {
            VoxelShape voxelshape = Shapes.box(0.0F, 0.0F, 0.0F, 1.0F, height, 1.0F);
            VoxelShape voxelshape1 = state.getOcclusionShape(level, pos);
            return Shapes.blockOccudes(voxelshape, voxelshape1, face);
        } else {
            return false;
        }
    }

    private static boolean isFaceOccludedByNeighbor(BlockGetter level, BlockPos pos, Direction side, float height, BlockState blockState) {
        return isFaceOccludedByState(level, side, height, pos.relative(side), blockState);
    }

    private static boolean isFaceOccludedBySelf(BlockGetter level, BlockPos pos, BlockState state, Direction face) {
        return isFaceOccludedByState(level, face.getOpposite(), 1.0F, pos, state);
    }

    private static float calculateAverageHeight(BlockAndTintGetter level, Fluid fluid, float currentHeight, float height1, float height2, BlockPos pos) {
        if (!(height2 >= 1.0F) && !(height1 >= 1.0F)) {
            float[] afloat = new float[2];
            if (height2 > 0.0F || height1 > 0.0F) {
                float f = getHeight(level, fluid, pos);
                if (f >= 1.0F) {
                    return 1.0F;
                }

                addWeightedHeight(afloat, f);
            }

            addWeightedHeight(afloat, currentHeight);
            addWeightedHeight(afloat, height2);
            addWeightedHeight(afloat, height1);
            return afloat[0] / afloat[1];
        } else {
            return 1.0F;
        }
    }

    private static void addWeightedHeight(float[] output, float height) {
        if (height >= 0.8F) {
            output[0] += height * 10.0F;
            output[1] += 10.0F;
        } else if (height >= 0.0F) {
            output[0] += height;
            output[1]++;
        }

    }

    private static float getHeight(BlockAndTintGetter level, Fluid fluid, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        return getHeight(level, fluid, pos, blockstate, blockstate.getFluidState());
    }

    private static void vertex(VertexConsumer consumer, PoseStack pose, float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int light) {
        consumer.addVertex(pose.last(), x, y, z).setColor(red, green, blue, alpha).setUv(u, v).setLight(light).setNormal(pose.last(), 0.0F, 1.0F, 0.0F);
    }

    private static float getHeight(BlockAndTintGetter level, Fluid fluid, BlockPos pos, BlockState blockState, FluidState fluidState) {
        if (fluid.isSame(fluidState.getType())) {
            BlockState blockstate = level.getBlockState(pos.above());
            return fluid.isSame(blockstate.getFluidState().getType()) ? 1.0F : fluidState.getOwnHeight();
        } else {
            return !blockState.isSolid() ? 0.0F : -1.0F;
        }
    }

    private static int getLightColor(BlockAndTintGetter level, BlockPos pos) {
        int i = LevelRenderer.getLightColor(level, pos);
        int j = LevelRenderer.getLightColor(level, pos.above());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (Math.max(k, l)) | (Math.max(i1, j1)) << 16;
    }

    public static void tesselate(BlockAndTintGetter level, BlockPos pos, VertexConsumer buffer, PoseStack poseStack, BlockState blockState, FluidState fluidState) {
        TextureAtlasSprite[] atextureatlassprite = FluidSpriteCache.getFluidSprites(level, pos, fluidState);
        int i = IClientFluidTypeExtensions.of(fluidState).getTintColor(fluidState, level, pos);
        float alpha = (float)(i >> 24 & 255) / 255.0F;
        float f = (float)(i >> 16 & 255) / 255.0F;
        float f1 = (float)(i >> 8 & 255) / 255.0F;
        float f2 = (float)(i & 255) / 255.0F;
        BlockState blockstate = level.getBlockState(pos.relative(Direction.DOWN));
        BlockState blockstate1 = level.getBlockState(pos.relative(Direction.UP));
        BlockState blockstate2 = level.getBlockState(pos.relative(Direction.NORTH));
        FluidState fluidstate2 = blockstate2.getFluidState();
        BlockState blockstate3 = level.getBlockState(pos.relative(Direction.SOUTH));
        FluidState fluidstate3 = blockstate3.getFluidState();
        BlockState blockstate4 = level.getBlockState(pos.relative(Direction.WEST));
        FluidState fluidstate4 = blockstate4.getFluidState();
        BlockState blockstate5 = level.getBlockState(pos.relative(Direction.EAST));
        FluidState fluidstate5 = blockstate5.getFluidState();
        boolean flag1 = !isNeighborStateHidingOverlay(fluidState, blockstate1, Direction.DOWN);
        boolean flag2 = shouldRenderFace(level, pos, fluidState, blockState, Direction.DOWN, blockstate) && !isFaceOccludedByNeighbor(level, pos, Direction.DOWN, 0.8888889F, blockstate);
        boolean flag3 = shouldRenderFace(level, pos, fluidState, blockState, Direction.NORTH, blockstate2);
        boolean flag4 = shouldRenderFace(level, pos, fluidState, blockState, Direction.SOUTH, blockstate3);
        boolean flag5 = shouldRenderFace(level, pos, fluidState, blockState, Direction.WEST, blockstate4);
        boolean flag6 = shouldRenderFace(level, pos, fluidState, blockState, Direction.EAST, blockstate5);
        if (flag1 || flag2 || flag6 || flag5 || flag3 || flag4) {
            float f3 = level.getShade(Direction.DOWN, true);
            float f4 = level.getShade(Direction.UP, true);
            float f5 = level.getShade(Direction.NORTH, true);
            float f6 = level.getShade(Direction.WEST, true);
            Fluid fluid = fluidState.getType();
            float f11 = getHeight(level, fluid, pos, blockState, fluidState);
            float f7;
            float f8;
            float f9;
            float f10;
            if (f11 >= 1.0F) {
                f7 = 1.0F;
                f8 = 1.0F;
                f9 = 1.0F;
                f10 = 1.0F;
            } else {
                float f12 = getHeight(level, fluid, pos.north(), blockstate2, fluidstate2);
                float f13 = getHeight(level, fluid, pos.south(), blockstate3, fluidstate3);
                float f14 = getHeight(level, fluid, pos.east(), blockstate5, fluidstate5);
                float f15 = getHeight(level, fluid, pos.west(), blockstate4, fluidstate4);
                f7 = calculateAverageHeight(level, fluid, f11, f12, f14, pos.relative(Direction.NORTH).relative(Direction.EAST));
                f8 = calculateAverageHeight(level, fluid, f11, f12, f15, pos.relative(Direction.NORTH).relative(Direction.WEST));
                f9 = calculateAverageHeight(level, fluid, f11, f13, f14, pos.relative(Direction.SOUTH).relative(Direction.EAST));
                f10 = calculateAverageHeight(level, fluid, f11, f13, f15, pos.relative(Direction.SOUTH).relative(Direction.WEST));
            }

            float f36 = 0;
            float f37 = 0;
            float f38 = 0;
            float f39 = 0.001F;
            float f16 = flag2 ? 0.001F : 0.0F;
            if (flag1 && !isFaceOccludedByNeighbor(level, pos, Direction.UP, Math.min(Math.min(f8, f10), Math.min(f9, f7)), blockstate1)) {
                f8 -= 0.001F;
                f10 -= 0.001F;
                f9 -= 0.001F;
                f7 -= 0.001F;
                Vec3 vec3 = fluidState.getFlow(level, pos);
                float f17;
                float f18;
                float f19;
                float f20;
                float f21;
                float f22;
                float f23;
                float f24;
                if (vec3.x == (double)0.0F && vec3.z == (double)0.0F) {
                    TextureAtlasSprite textureatlassprite1 = atextureatlassprite[0];
                    f17 = textureatlassprite1.getU(0.0F);
                    f21 = textureatlassprite1.getV(0.0F);
                    f18 = f17;
                    f22 = textureatlassprite1.getV(1.0F);
                    f19 = textureatlassprite1.getU(1.0F);
                    f23 = f22;
                    f20 = f19;
                    f24 = f21;
                } else {
                    TextureAtlasSprite textureatlassprite = atextureatlassprite[1];
                    float f25 = (float) Mth.atan2(vec3.z, vec3.x) - ((float)Math.PI / 2F);
                    float f26 = Mth.sin(f25) * 0.25F;
                    float f27 = Mth.cos(f25) * 0.25F;
                    float f28 = 0.5F;
                    f17 = textureatlassprite.getU(0.5F + (-f27 - f26));
                    f21 = textureatlassprite.getV(0.5F + -f27 + f26);
                    f18 = textureatlassprite.getU(0.5F + -f27 + f26);
                    f22 = textureatlassprite.getV(0.5F + f27 + f26);
                    f19 = textureatlassprite.getU(0.5F + f27 + f26);
                    f23 = textureatlassprite.getV(0.5F + (f27 - f26));
                    f20 = textureatlassprite.getU(0.5F + (f27 - f26));
                    f24 = textureatlassprite.getV(0.5F + (-f27 - f26));
                }

                float f53 = (f17 + f18 + f19 + f20) / 4.0F;
                float f54 = (f21 + f22 + f23 + f24) / 4.0F;
                float f55 = atextureatlassprite[0].uvShrinkRatio();
                f17 = Mth.lerp(f55, f17, f53);
                f18 = Mth.lerp(f55, f18, f53);
                f19 = Mth.lerp(f55, f19, f53);
                f20 = Mth.lerp(f55, f20, f53);
                f21 = Mth.lerp(f55, f21, f54);
                f22 = Mth.lerp(f55, f22, f54);
                f23 = Mth.lerp(f55, f23, f54);
                f24 = Mth.lerp(f55, f24, f54);
                int l = getLightColor(level, pos);
                vertex(buffer, poseStack, f36 + 0.0F, f37 + f8, f38 + 0.0F, 1f, 1f, 1f, 1f, f17, f21, l);
                vertex(buffer, poseStack, f36 + 0.0F, f37 + f10, f38 + 1.0F, 1f, 1f, 1f, 1f, f18, f22, l);
                vertex(buffer, poseStack, f36 + 1.0F, f37 + f9, f38 + 1.0F, 1f, 1f, 1f, 1f, f19, f23, l);
                vertex(buffer, poseStack, f36 + 1.0F, f37 + f7, f38 + 0.0F, 1f, 1f, 1f, 1f, f20, f24, l);
                if (fluidState.shouldRenderBackwardUpFace(level, pos.above())) {
                    vertex(buffer, poseStack, f36 + 0.0F, f37 + f8, f38 + 0.0F, 1f, 1f, 1f, 1f, f17, f21, l);
                    vertex(buffer, poseStack, f36 + 1.0F, f37 + f7, f38 + 0.0F, 1f, 1f, 1f, 1f, f20, f24, l);
                    vertex(buffer, poseStack, f36 + 1.0F, f37 + f9, f38 + 1.0F, 1f, 1f, 1f, 1f, f19, f23, l);
                    vertex(buffer, poseStack, f36 + 0.0F, f37 + f10, f38 + 1.0F, 1f, 1f, 1f, 1f, f18, f22, l);
                }
            }

            if (flag2) {
                float f40 = atextureatlassprite[0].getU0();
                float f41 = atextureatlassprite[0].getU1();
                float f42 = atextureatlassprite[0].getV0();
                float f43 = atextureatlassprite[0].getV1();
                int k = getLightColor(level, pos.below());
                float f46 = f3 * f;
                float f48 = f3 * f1;
                float f50 = f3 * f2;
                vertex(buffer, poseStack, f36, f37 + f16, f38 + 1.0F, f46, f48, f50, alpha, f40, f43, k);
                vertex(buffer, poseStack, f36, f37 + f16, f38, f46, f48, f50, alpha, f40, f42, k);
                vertex(buffer, poseStack, f36 + 1.0F, f37 + f16, f38, f46, f48, f50, alpha, f41, f42, k);
                vertex(buffer, poseStack, f36 + 1.0F, f37 + f16, f38 + 1.0F, f46, f48, f50, alpha, f41, f43, k);
            }

            int j = getLightColor(level, pos);

            for(Direction direction : Direction.Plane.HORIZONTAL) {
                float f44;
                float f45;
                float f47;
                float f49;
                float f51;
                float f52;
                boolean flag7;
                switch (direction) {
                    case NORTH:
                        f44 = f8;
                        f45 = f7;
                        f47 = f36;
                        f51 = f36 + 1.0F;
                        f49 = f38 + 0.001F;
                        f52 = f38 + 0.001F;
                        flag7 = flag3;
                        break;
                    case SOUTH:
                        f44 = f9;
                        f45 = f10;
                        f47 = f36 + 1.0F;
                        f51 = f36;
                        f49 = f38 + 1.0F - 0.001F;
                        f52 = f38 + 1.0F - 0.001F;
                        flag7 = flag4;
                        break;
                    case WEST:
                        f44 = f10;
                        f45 = f8;
                        f47 = f36 + 0.001F;
                        f51 = f36 + 0.001F;
                        f49 = f38 + 1.0F;
                        f52 = f38;
                        flag7 = flag5;
                        break;
                    default:
                        f44 = f7;
                        f45 = f9;
                        f47 = f36 + 1.0F - 0.001F;
                        f51 = f36 + 1.0F - 0.001F;
                        f49 = f38;
                        f52 = f38 + 1.0F;
                        flag7 = flag6;
                }

                if (flag7 && !isFaceOccludedByNeighbor(level, pos, direction, Math.max(f44, f45), level.getBlockState(pos.relative(direction)))) {
                    BlockPos blockpos = pos.relative(direction);
                    TextureAtlasSprite textureatlassprite2 = atextureatlassprite[1];
                    if (atextureatlassprite[2] != null && level.getBlockState(blockpos).shouldDisplayFluidOverlay(level, blockpos, fluidState)) {
                        textureatlassprite2 = atextureatlassprite[2];
                    }

                    float f56 = textureatlassprite2.getU(0.0F);
                    float f58 = textureatlassprite2.getU(0.5F);
                    float f59 = textureatlassprite2.getV((1.0F - f44) * 0.5F);
                    float f60 = textureatlassprite2.getV((1.0F - f45) * 0.5F);
                    float f31 = textureatlassprite2.getV(0.5F);
                    float f32 = direction.getAxis() == Direction.Axis.Z ? f5 : f6;
                    float f33 = f4 * f32 * f;
                    float f34 = f4 * f32 * f1;
                    float f35 = f4 * f32 * f2;
                    vertex(buffer, poseStack, f47, f37 + f44, f49, f33, f34, f35, alpha, f56, f59, j);
                    vertex(buffer, poseStack, f51, f37 + f45, f52, f33, f34, f35, alpha, f58, f60, j);
                    vertex(buffer, poseStack, f51, f37 + f16, f52, f33, f34, f35, alpha, f58, f31, j);
                    vertex(buffer, poseStack, f47, f37 + f16, f49, f33, f34, f35, alpha, f56, f31, j);
                    if (textureatlassprite2 != atextureatlassprite[2]) {
                        vertex(buffer, poseStack, f47, f37 + f16, f49, f33, f34, f35, alpha, f56, f31, j);
                        vertex(buffer, poseStack, f51, f37 + f16, f52, f33, f34, f35, alpha, f58, f31, j);
                        vertex(buffer, poseStack, f51, f37 + f45, f52, f33, f34, f35, alpha, f58, f60, j);
                        vertex(buffer, poseStack, f47, f37 + f44, f49, f33, f34, f35, alpha, f56, f59, j);
                    }
                }
            }
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
