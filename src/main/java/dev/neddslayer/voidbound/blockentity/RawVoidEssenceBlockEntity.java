package dev.neddslayer.voidbound.blockentity;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.config.Config;
import dev.neddslayer.voidbound.datagen.VoidboundAdvancementProvider;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundParticles;
import dev.neddslayer.voidbound.registrar.VoidboundSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.minecraft.world.level.material.FlowingFluid.FALLING;
import static net.minecraft.world.level.material.FlowingFluid.LEVEL;

public class RawVoidEssenceBlockEntity extends SmartBlockEntity {
    private boolean toPurifyNeighbors;

    private final RandomSource random = RandomSource.create();

    public RawVoidEssenceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);

        setLazyTickRate(Config.PURIFY_SPEED.getAsInt());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void lazyTick() {
        if (toPurifyNeighbors) {
            toPurifyNeighbors = false;
            BlockPos pos = getBlockPos();
            BlockState state = level.getBlockState(pos);
            FluidState oldFluidState = state.getFluidState();
            boolean fluidFalling = oldFluidState.getValue(FALLING);

            Vec3 center = pos.getCenter();
            if (level instanceof ServerLevel serverLevel) {
                if (fluidFalling) {
                    for (int i = 0; i < 6; i++) {
                        serverLevel.sendParticles(VoidboundParticles.PURIFY.get(),
                                center.x + level.random.nextGaussian() * 0.5, center.y + level.random.nextGaussian() * 0.05 + 0.5, center.z + level.random.nextGaussian() * 0.5, 0,
                                level.random.nextGaussian() * 0.2, level.random.nextDouble() * 0.2, level.random.nextGaussian() * 0.2, 1);
                    }
                } else {
                    for (int i = 0; i < 2; i++) {
                        serverLevel.sendParticles(VoidboundParticles.PURIFY.get(),
                                center.x + level.random.nextGaussian() * 0.25, center.y + level.random.nextGaussian() * 0.05 + 0.5, center.z + level.random.nextGaussian() * 0.25, 0,
                                level.random.nextGaussian() * 0.05, level.random.nextDouble() * 0.5, level.random.nextGaussian() * 0.05, 1);
                    }
                }
                serverLevel.playSound(null, getBlockPos(), VoidboundSounds.PURIFY.get(), SoundSource.BLOCKS, 1.0f, 1.0f + (float)random.nextGaussian() * 0.05f);
            }

            // purify self first!
            // get old data to copy
            int blockFluidLevel = state.getValue(LiquidBlock.LEVEL);
            BlockState newState;
            if (oldFluidState.isSource()) {
                newState = VoidboundFluids.DISTILLED_VOID_ESSENCE.getSource().getSource().defaultFluidState().createLegacyBlock();
            } else {
                // FluidState LEVEL property only exists if it's flowing
                int fluidLevel = oldFluidState.getAmount();

                // transfer over to new data
                newState = VoidboundFluids.DISTILLED_VOID_ESSENCE.getSource().getFlowing().defaultFluidState().createLegacyBlock();
                newState.getFluidState().setValue(LEVEL, fluidLevel);
            }
            newState.getFluidState().setValue(FALLING, fluidFalling);
            newState.setValue(LiquidBlock.LEVEL, blockFluidLevel);

            // update block and start purification for others
            level.setBlockAndUpdate(pos, newState);

            if (!level.isClientSide) {
                AABB area = AABB.ofSize(getBlockPos().getCenter(), 10, 10, 10);
                for (ServerPlayer player : ((ServerLevel) level).players()) {
                    if (area.contains(player.getX(), player.getY(), player.getZ()) && TargetingConditions.forNonCombat().test(null, player)) {
                        VoidboundAdvancementProvider.ADVANCEMENT_TRIGGERS.get("essence_distill").trigger(player);
                    }
                }
            }

            tryPurifyNeighbor(pos.north());
            tryPurifyNeighbor(pos.south());
            tryPurifyNeighbor(pos.east());
            tryPurifyNeighbor(pos.west());
            tryPurifyNeighbor(pos.above());
            tryPurifyNeighbor(pos.below());
        }
    }

    private void tryPurifyNeighbor(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() == Voidbound.registrate().get("raw_void_essence", BuiltInRegistries.BLOCK.key()).get()) {
            RawVoidEssenceBlockEntity be = (RawVoidEssenceBlockEntity) level.getBlockEntity(pos);
            if (be != null) be.startPurification();
        }
    }

    public void startPurification() {
        toPurifyNeighbors = true;
        lazyTickCounter = getBlockState().getFluidState().getValue(FALLING) ? 1 : 2;
    }
}
