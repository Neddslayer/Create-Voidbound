package dev.neddslayer.voidbound.blockentity;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import dev.neddslayer.voidbound.block.VoidMotorBlock;
import dev.neddslayer.voidbound.datagen.VoidboundAdvancementProvider;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

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
        internalTank.drain(50, IFluidHandler.FluidAction.EXECUTE);
        updateGeneratedRotation();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (internalTank.getFluidAmount() > 0 && !this.level.isClientSide) {
            AABB area = AABB.ofSize(getBlockPos().getCenter(), 10, 10, 10);
            for (ServerPlayer player : ((ServerLevel) level).players()) {
                if (area.contains(player.getX(), player.getY(), player.getZ()) && TargetingConditions.forNonCombat().test(null, player)) {
                    VoidboundAdvancementProvider.ADVANCEMENT_TRIGGERS.get("power_void_motor").trigger(player);
                }
            }
        }
    }

    @Override
    public float getGeneratedSpeed() {
        return internalTank.getFluidAmount() > 0 ? 128 : 0;
    }
}
