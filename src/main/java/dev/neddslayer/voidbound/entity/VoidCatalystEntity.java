package dev.neddslayer.voidbound.entity;

import dev.neddslayer.voidbound.registrar.VoidboundEntityTypes;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class VoidCatalystEntity extends Entity implements ItemSupplier {
    public boolean hasLink;

    public VoidCatalystEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    public VoidCatalystEntity(Level level) {
        this(VoidboundEntityTypes.VOID_CATALYST_ENTITY.get(), level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.remove(RemovalReason.KILLED);
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        setDeltaMovement(getDeltaMovement().multiply(0.8, 0.8, 0.8));
        setPos(position().add(getDeltaMovement()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(VoidboundItems.VOID_GEM.get());
    }

    @Override
    public boolean isOnFire() {
        return  false;
    }
}
