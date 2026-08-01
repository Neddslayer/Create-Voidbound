package dev.neddslayer.voidbound.blockentity;

import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import dev.neddslayer.voidbound.config.Config;
import dev.neddslayer.voidbound.datagen.VoidboundAdvancementProvider;
import dev.neddslayer.voidbound.registrar.VoidboundBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BrassDrillBlockEntity extends BlockBreakingKineticBlockEntity {
    private float bedrockDestroyProgress = 0.0f;

    public BrassDrillBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putFloat("BedrockDestroyProgress", bedrockDestroyProgress);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        bedrockDestroyProgress = compound.getFloat("BedrockDestroyProgress");
        super.read(compound, registries, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        if (breakingPos == null) return;

        if (level.isClientSide()) {
            // spawn breaking particles
            if (!level.getBlockState(breakingPos).isEmpty()) {
                for (int i = 0; i < Math.abs(getSpeed()) / 16; i++) {
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(breakingPos)), getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.01, getBlockPos().getZ() + 0.5, 0, 0, 0);
                }
                if (bedrockDestroyProgress > 0) {
                    for (int i = 0; i < 5 + Math.floor(bedrockDestroyProgress * 20); i++) {
                        level.addParticle(
                                ParticleTypes.SMOKE,
                                getBlockPos().getX() + 0.5 + level.random.nextGaussian() * 0.3, getBlockPos().getY() + 0.5 + level.random.nextFloat() * 0.5, getBlockPos().getZ() + 0.5 + level.random.nextGaussian() * 0.3,
                                0, 0, 0);
                    }
                }
                if (bedrockDestroyProgress > 0.75) {
                    for (int i = 0; i < 2; i++) {
                        level.addParticle(
                                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                getBlockPos().getX() + 0.5 + level.random.nextGaussian() * 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5 + level.random.nextGaussian() * 0.5,
                                level.random.nextGaussian() * 0.025, 0.1, level.random.nextGaussian() * 0.025);
                    }

                }
                if (bedrockDestroyProgress > 0.9) {
                    for (int i = 0; i < 10; i++) {
                        level.addParticle(
                                ParticleTypes.FLAME,
                                getBlockPos().getX() + 0.5 + level.random.nextGaussian() * 0.3, getBlockPos().getY() + 0.5 + level.random.nextFloat() * 0.5, getBlockPos().getZ() + 0.5 + level.random.nextGaussian() * 0.3,
                                level.random.nextGaussian() * 0.025, level.random.nextGaussian() * 0.05, level.random.nextGaussian() * 0.025);
                    }
                }
                if (bedrockDestroyProgress >= 0.995f) {
                    for (int i = 0; i < 100; i++) {
                        level.addParticle(
                                ParticleTypes.CLOUD,
                                getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5,
                                level.random.nextGaussian(), level.random.nextFloat(), level.random.nextGaussian());
                    }
                }
            }
        } else {
            if (bedrockDestroyProgress >= 1f) {
                level.setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                level.setBlock(getBreakingPos(), VoidboundBlocks.DRILLED_BEDROCK.getDefaultState(), 3);
                AABB area = AABB.ofSize(getBlockPos().getCenter(), 10, 10, 10);

                for (ServerPlayer player : ((ServerLevel)level).players()) {
                    if (area.contains(player.getX(), player.getY(), player.getZ()) && TargetingConditions.forNonCombat().test(null, player)) {
                        VoidboundAdvancementProvider.ADVANCEMENT_TRIGGERS.get("drill_bedrock").trigger(player);
                    }
                }

                level.explode(null, Explosion.getDefaultDamageSource(level, null), null,
                        getBlockPos().getCenter().x, getBlockPos().getCenter().y, getBlockPos().getCenter().z,
                        10, true, Level.ExplosionInteraction.BLOCK, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
            }
        }
        BlockState stateToBreak = level.getBlockState(breakingPos);
        if (stateToBreak.getBlock() == Blocks.BEDROCK && Math.abs(getSpeed()) == 256) {
            bedrockDestroyProgress += (float) (Config.BEDROCK_DRILL_SPEED.getAsDouble() / 200.0);
            notifyUpdate();
        } else {
            if (bedrockDestroyProgress != 0) notifyUpdate();
            bedrockDestroyProgress = 0;
        }
    }

    @Override
    protected float getBreakSpeed() {
        return (float) Math.pow(Math.abs(getSpeed() / 50f), 1.2);
    }

    @Override
    protected BlockPos getBreakingPos() {
        return getBlockPos().below();
    }

    public float getBedrockDestroyProgress() {
        return bedrockDestroyProgress;
    }

}
