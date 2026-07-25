package dev.neddslayer.voidbound.block;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import dev.neddslayer.voidbound.blockentity.RepulsionCoilBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RepulsionCoilBlock extends DirectionalKineticBlock implements IBE<RepulsionCoilBlockEntity> {

    public RepulsionCoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);
        return defaultBlockState().setValue(FACING, preferred.getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(
                AllShapes.CASING_12PX.get(state.getValue(FACING)),
                VoxelShaper.forDirectional(Block.box(6.5f, 12f, 6.5f, 9.5f, 20f, 9.5f), Direction.UP).get(state.getValue(FACING))
        );

    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }

    @Override
    public Class<RepulsionCoilBlockEntity> getBlockEntityClass() {
        return RepulsionCoilBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RepulsionCoilBlockEntity> getBlockEntityType() {
        return VoidboundBlockEntityTypes.REPULSION_COIL_BLOCK_ENTITY.get();
    }
}
