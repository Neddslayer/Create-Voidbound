package dev.neddslayer.voidbound.item;

import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.entity.LivingEntityProjectionEntity;
import dev.neddslayer.voidbound.network.SpawnQuasarParticlePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AstralProjectionMobEffect extends MobEffect {
    private static final Map<UUID, ProjectionInfo> infos = new HashMap<>();

    public AstralProjectionMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFB0BFFFF);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide) {
            LivingEntityProjectionEntity projection = new LivingEntityProjectionEntity(livingEntity.level(), livingEntity.getId());
            projection.setPos(livingEntity.getPosition(0));
            projection.setXRot(livingEntity.getXRot());
            projection.setYRot(livingEntity.getYRot());
            projection.setYHeadRot(livingEntity.getYHeadRot());
            projection.setYBodyRot(livingEntity.yBodyRot);
            livingEntity.level().addFreshEntity(projection);
            infos.put(livingEntity.getUUID(), new ProjectionInfo(livingEntity.getPosition(0), livingEntity.level().dimension(), livingEntity.getHealth(), livingEntity.getHealth(), livingEntity.getXRot(), livingEntity.getYRot(), projection.getId()));
        }
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return infos.containsKey(livingEntity.getUUID());
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onMobHurt(LivingEntity livingEntity, int amplifier, DamageSource damageSource, float amount) {
        super.onMobHurt(livingEntity, amplifier, damageSource, amount);
        infos.computeIfPresent(livingEntity.getUUID(), (u, i) -> {i.damage(amount); return i;});
    }

    public static void resetEntity(LivingEntity livingEntity) {
        infos.computeIfPresent(livingEntity.getUUID(), (u, i) -> {
            if (livingEntity.level().dimension().equals(i.originalDimension)) {
                if (!livingEntity.level().isClientSide) {
                    PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, new SpawnQuasarParticlePacket(livingEntity.getPosition(0).add(0, 1, 0).toVector3f(), Voidbound.path("projection_end")));
                }
                if (livingEntity instanceof ServerPlayer player) {
                    player.connection.teleport(i.position.x, i.position.y, i.position.z, i.originalYRot, i.originalXRot, RelativeMovement.ROTATION);
                } else {
                    livingEntity.moveTo(i.position.x, i.position.y, i.position.z, i.originalYRot, i.originalXRot);
                }
                livingEntity.clearFire();
                livingEntity.setHealth(i.originalHealth);
                Entity projection = livingEntity.level().getEntity(i.projectionEntity);
                if (projection != null) projection.discard();
            }
            return null;
        });
    }

    public static final class ProjectionInfo {
        private final Vec3 position;
        private final ResourceKey<Level> originalDimension;
        private final Float originalHealth;
        private Float currentHealth;
        private final float originalXRot, originalYRot;
        private final int projectionEntity;

        public ProjectionInfo(Vec3 position, ResourceKey<Level> originalDimension, Float originalHealth, Float currentHealth, float xRot, float yRot, int projectionEntity) {
            this.position = position;
            this.originalDimension = originalDimension;
            this.originalHealth = originalHealth;
            this.currentHealth = currentHealth;
            this.originalXRot = xRot;
            this.originalYRot = yRot;
            this.projectionEntity = projectionEntity;
        }

        public Vec3 position() {
            return position;
        }

        public Float originalHealth() {
            return originalHealth;
        }

        public Float currentHealth() {
            return currentHealth;
        }

        public float xRot() {
            return this.originalXRot;
        }

        public float yRot() {
            return this.originalYRot;
        }

        public void damage(float damage) {
            currentHealth -= damage;
        }

        public boolean shouldDie() {
            return currentHealth <= 0;
        }

        public int getProjectionEntity() {
            return projectionEntity;
        }
    }
}
