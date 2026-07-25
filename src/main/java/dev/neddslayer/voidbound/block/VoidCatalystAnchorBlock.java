package dev.neddslayer.voidbound.block;

import com.simibubi.create.AllShapes;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import dev.neddslayer.voidbound.blockentity.VoidCatalystAnchorBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class VoidCatalystAnchorBlock extends KineticBlock implements IBE<VoidCatalystAnchorBlockEntity> {
    public VoidCatalystAnchorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_12PX.get(Direction.UP);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return onBlockEntityUseItemOn(level, pos, be -> {
            if (!stack.isEmpty()) {

                if (FluidHelper.tryEmptyItemIntoBE(level, player, hand, stack, be))
                    return ItemInteractionResult.SUCCESS;
                if (FluidHelper.tryFillItemFromBE(level, player, hand, stack, be))
                    return ItemInteractionResult.SUCCESS;

                if (GenericItemEmptying.canItemBeEmptied(level, stack)
                        || GenericItemFilling.canItemBeFilled(level, stack))
                    return ItemInteractionResult.SUCCESS;
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            return ItemInteractionResult.SUCCESS;
        });
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }

    @Override
    public Class<VoidCatalystAnchorBlockEntity> getBlockEntityClass() {
        return VoidCatalystAnchorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VoidCatalystAnchorBlockEntity> getBlockEntityType() {
        return VoidboundBlockEntityTypes.VOID_CATALYST_ANCHOR_BLOCK_ENTITY.get();
    }
}
