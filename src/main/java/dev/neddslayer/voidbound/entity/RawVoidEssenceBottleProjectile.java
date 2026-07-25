package dev.neddslayer.voidbound.entity;

import dev.neddslayer.voidbound.item.RawVoidEssenceBottle;
import dev.neddslayer.voidbound.network.PushPlayerPacket;
import dev.neddslayer.voidbound.network.RepulsePacket;
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

public class RawVoidEssenceBottleProjectile extends ThrowableItemProjectile {
    public RawVoidEssenceBottleProjectile(Level level, LivingEntity shooter) {
        super(VoidboundEntityTypes.RAW_VOID_ESSENCE_BOTTLE.get(), shooter, level);
    }

    public RawVoidEssenceBottleProjectile(Level level, double x, double y, double z) {
        super(VoidboundEntityTypes.RAW_VOID_ESSENCE_BOTTLE.get(), x, y, z, level);
    }

    public RawVoidEssenceBottleProjectile(EntityType<RawVoidEssenceBottleProjectile> entityType, Level level) {
        super(entityType, level);
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel serverLevel) {
            this.level().levelEvent(2002, this.blockPosition(), PotionContents.getColor(Potions.WATER));
            this.level().setBlockAndUpdate(blockPosition(), VoidboundFluids.RAW_VOID_ESSENCE.get().defaultFluidState().createLegacyBlock());
            PacketDistributor.sendToPlayersTrackingEntity(this, new RepulsePacket(blockPosition().getCenter().toVector3f(), 5));
            List<Entity> entities = serverLevel.getEntities(null, AABB.ofSize(blockPosition().getCenter(), 5, 5, 5));
            for (Entity entity : entities) {
                if (entity instanceof Player) continue;
                Vec3 p = entity.position().subtract(blockPosition().getCenter());
                if (p.length() < 5) {
                    double factor = (p.length() / 5) * 3.15f;
                    entity.push(p.normalize().multiply(factor, factor, factor));
                }
            }
            this.discard();
        }

    }

    @Override
    protected Item getDefaultItem() {
        return VoidboundItems.RAW_VOID_ESSENCE_BOTTLE.get();
    }
}

