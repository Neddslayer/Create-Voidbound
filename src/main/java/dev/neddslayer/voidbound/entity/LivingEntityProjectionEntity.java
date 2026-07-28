package dev.neddslayer.voidbound.entity;

import dev.neddslayer.voidbound.registrar.VoidboundEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class LivingEntityProjectionEntity extends LivingEntity {
    public static final EntityDataAccessor<Integer> TARGET_ENTITY = SynchedEntityData.defineId(LivingEntityProjectionEntity.class, EntityDataSerializers.INT);
    private final int targetEntity;

    public LivingEntityProjectionEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.targetEntity = 0;
        this.noPhysics = true;
    }

    public LivingEntityProjectionEntity(Level level, int targetEntity) {
        super(VoidboundEntityTypes.PROJECTION.get(), level);
        this.targetEntity = targetEntity;
        this.entityData.set(TARGET_ENTITY, this.targetEntity);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TARGET_ENTITY, targetEntity);
    }

    @Override
    public void setPos(double x, double y, double z) {
        this.setPosRaw(x, y, z);
        Entity entity = this.level().getEntity(this.entityData.get(TARGET_ENTITY));
        if (entity != null) {
            EntityDimensions dimensions = entity.getDimensions(entity.getPose());
            this.setBoundingBox(dimensions.makeBoundingBox(this.getX(), this.getY(), this.getZ()));
        } else {
            this.setBoundingBox(this.makeBoundingBox());
        }
    }

    @Override
    public void tick() {
        Entity entity = this.level().getEntity(this.entityData.get(TARGET_ENTITY));
        if (entity == null && !this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (TARGET_ENTITY.equals(key)) {
            Entity entity = this.level().getEntity(this.entityData.get(TARGET_ENTITY));
            if (entity != null) {
                EntityDimensions dimensions = entity.getDimensions(entity.getPose());
                this.setBoundingBox(dimensions.makeBoundingBox(this.getX(), this.getY(), this.getZ()));
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
