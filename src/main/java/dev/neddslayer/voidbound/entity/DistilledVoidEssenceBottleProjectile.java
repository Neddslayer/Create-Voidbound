package dev.neddslayer.voidbound.entity;

import dev.neddslayer.voidbound.item.DistilledVoidEssenceBottle;
import dev.neddslayer.voidbound.network.PushPlayerPacket;
import dev.neddslayer.voidbound.registrar.VoidboundEntityTypes;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import dev.neddslayer.voidbound.registrar.VoidboundParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class DistilledVoidEssenceBottleProjectile extends ThrowableItemProjectile {

    public DistilledVoidEssenceBottleProjectile(Level level, LivingEntity shooter) {
        super(VoidboundEntityTypes.DISTILLED_VOID_ESSENCE_BOTTLE.get(), shooter, level);
    }

    public DistilledVoidEssenceBottleProjectile(Level level, double x, double y, double z) {
        super(VoidboundEntityTypes.DISTILLED_VOID_ESSENCE_BOTTLE.get(), x, y, z, level);
    }

    public DistilledVoidEssenceBottleProjectile(EntityType<DistilledVoidEssenceBottleProjectile> entityType, Level level) {
        super(entityType, level);
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel serverLevel) {
            Vec3 center = blockPosition().getCenter();
            this.level().levelEvent(2002, this.blockPosition(), PotionContents.getColor(Potions.WATER));
            this.level().setBlockAndUpdate(blockPosition(), VoidboundFluids.DISTILLED_VOID_ESSENCE.get().defaultFluidState().createLegacyBlock());
            for (int i = 0; i < 256; i++) {
                serverLevel.sendParticles(VoidboundParticles.ATTRACT.get(), center.x, center.y, center.z, 0, 5, 0.5, 0, 1);
            }
            float attractionRadius = 8;
            List<Entity> entities = serverLevel.getEntities(null, AABB.ofSize(center, attractionRadius, attractionRadius, attractionRadius));
            for (Entity entity : entities) {
                Vec3 p = entity.position().subtract(center);
                double factor = (-p.length() / attractionRadius) * 3.15f;
                if (entity instanceof Player) {
                    PacketDistributor.sendToPlayer((ServerPlayer) entity, new PushPlayerPacket(p.normalize().multiply(factor, factor, factor).toVector3f()));
                    continue;
                }
                if (p.length() < attractionRadius) {
                    entity.push(p.normalize().multiply(factor, factor, factor));
                }
            }
            this.discard();
        }

    }

    @Override
    protected Item getDefaultItem() {
        return VoidboundItems.DISTILLED_VOID_ESSENCE_BOTTLE.get();
    }
}

