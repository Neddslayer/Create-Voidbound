package dev.neddslayer.voidbound.item;

import dev.neddslayer.voidbound.entity.DistilledVoidEssenceBottleProjectile;
import dev.neddslayer.voidbound.network.PushPlayerPacket;
import dev.neddslayer.voidbound.network.RepulsePacket;
import dev.neddslayer.voidbound.registrar.VoidboundEntityTypes;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import dev.neddslayer.voidbound.registrar.VoidboundParticles;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class DistilledVoidEssenceBottle extends Item implements ProjectileItem {
    public DistilledVoidEssenceBottle(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            DistilledVoidEssenceBottleProjectile bottleProjectile = new DistilledVoidEssenceBottleProjectile(level, player);
            bottleProjectile.setItem(itemstack);
            bottleProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            level.addFreshEntity(bottleProjectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        DistilledVoidEssenceBottleProjectile bottleProjectile = new DistilledVoidEssenceBottleProjectile(level, pos.x(), pos.y(), pos.z());
        bottleProjectile.setItem(stack);
        return bottleProjectile;
    }

}
