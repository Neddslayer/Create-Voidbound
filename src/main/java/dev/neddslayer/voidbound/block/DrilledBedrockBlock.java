package dev.neddslayer.voidbound.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DrilledBedrockBlock extends Block {

    private static final VoxelShape COLLISION_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 3),
            Block.box(0, 0, 13, 16, 16, 16),
            Block.box(0, 0, 0, 3, 16, 16),
            Block.box(13, 0, 0, 16, 16, 16)
    );

    private static final VoxelShape VISUAL_SHAPE = Shapes.or(
            COLLISION_SHAPE,
            Block.box(3, 0, 3, 4, 16, 4),
            Block.box(3, 0, 12, 4, 16, 13),
            Block.box(12, 0, 12, 13, 16, 13),
            Block.box(12, 0, 3, 13, 16, 4)
    );

    public DrilledBedrockBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return VISUAL_SHAPE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VISUAL_SHAPE;
    }
}
