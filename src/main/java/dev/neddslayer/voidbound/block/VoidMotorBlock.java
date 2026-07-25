package dev.neddslayer.voidbound.block;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.neddslayer.voidbound.blockentity.VoidMotorBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoidMotorBlock extends DirectionalKineticBlock implements IBE<VoidMotorBlockEntity> {
    public VoidMotorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);
        return defaultBlockState().setValue(FACING, preferred);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_12PX.get(state.getValue(FACING));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    public static boolean hasPipeTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING)
                .getOpposite();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public Class<VoidMotorBlockEntity> getBlockEntityClass() {
        return VoidMotorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VoidMotorBlockEntity> getBlockEntityType() {
        return VoidboundBlockEntityTypes.VOID_MOTOR_BLOCK_ENTITY.get();
    }
}
