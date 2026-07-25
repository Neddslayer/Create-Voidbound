package dev.neddslayer.voidbound.blockentity;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.fluids.FluidNetwork;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeConnection;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.block.VoidMotorBlock;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class VoidMotorBlockEntity extends GeneratingKineticBlockEntity {
    private final SmartFluidTank internalTank;

    public VoidMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        internalTank = new SmartFluidTank(500, c -> {});
        internalTank.setValidator(stack -> stack.getFluidType() == VoidboundFluids.DISTILLED_VOID_ESSENCE.getType());
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                VoidboundBlockEntityTypes.VOID_MOTOR_BLOCK_ENTITY.get(),
                (be, context) -> {
                    if (context == null || VoidMotorBlock.hasPipeTowards(be.level, be.worldPosition, be.getBlockState(), context))
                        return be.internalTank;
                    return null;
                }
        );
    }

    @Override
    public void tick() {
        super.tick();
        internalTank.drain(32, IFluidHandler.FluidAction.EXECUTE);
        updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
        Direction direction = getBlockState().getValue(FACING);
        FluidTransportBehaviour pipe = FluidPropagator.getPipe(level, getBlockPos().relative(direction.getOpposite()));
        if (pipe != null && pipe.getConnection(direction) != null && pipe.getFlow(direction) != null && pipe.getFlow(direction).fluid.getFluidType() == VoidboundFluids.DISTILLED_VOID_ESSENCE.getType()) {
            Couple<Float> pressure = pipe.getConnection(direction).getPressure();
            if (pressure.getSecond() >= 32) return pressure.getSecond();
        }
        return internalTank.getFluidAmount() > 0 ? 32 : 0;
    }
}
