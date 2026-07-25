package dev.neddslayer.voidbound.block;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.neddslayer.voidbound.blockentity.BrassDrillBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrassDrillBlock extends KineticBlock implements IBE<BrassDrillBlockEntity> {
    public BrassDrillBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_12PX.get(Direction.DOWN);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    @Override
    public Class<BrassDrillBlockEntity> getBlockEntityClass() {
        return BrassDrillBlockEntity.class;
    }

    @Override
    public BlockEntityType<BrassDrillBlockEntity> getBlockEntityType() {
        return VoidboundBlockEntityTypes.BRASS_DRILL_BLOCK_ENTITY.get();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}
