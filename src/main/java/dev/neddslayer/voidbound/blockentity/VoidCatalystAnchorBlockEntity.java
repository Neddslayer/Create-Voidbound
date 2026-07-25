package dev.neddslayer.voidbound.blockentity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.datagen.VoidboundAdvancementProvider;
import dev.neddslayer.voidbound.entity.VoidCatalystEntity;
import dev.neddslayer.voidbound.network.BeginVoidVFXPacket;
import dev.neddslayer.voidbound.network.StopVoidVFXPacket;
import dev.neddslayer.voidbound.network.UpdateVoidVFXPositionPacket;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundParticles;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;

public class VoidCatalystAnchorBlockEntity extends KineticBlockEntity {
    @Nullable
    private VoidCatalystEntity targetedCatalyst;
    protected UUID forceTarget;
    private float lockOnTicks = 0;

    private int voidImplosionTimer = -1;

    private static int voidVFXGlobalIndex = 0;
    private int voidVFXInstanceIndex = 0;

    private int age;

    private final LerpedFloat fluidLevel;
    public FluidTank tankInventory;

    public VoidCatalystAnchorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);

        tankInventory = new SmartFluidTank(1000, this::onFluidStackChanged);
        tankInventory.setValidator(s -> s.is(VoidboundFluids.RAW_VOID_ESSENCE.getType()));
        fluidLevel = LerpedFloat.linear()
                .startWithValue(0)
                .chase(0, .5, LerpedFloat.Chaser.EXP);

        age = (int)(Math.random() * 20) + 1;
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;

        if (!level.isClientSide) {
            setChanged();
            sendData();
        }

        fluidLevel.chase(getFillState(), .5, LerpedFloat.Chaser.EXP);
    }

    public float getFillState() {
        return (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();
    }

    @Override
    public void tick() {
        super.tick();

        fluidLevel.tickChaser();

        if (!level.isClientSide) {
            if (targetedCatalyst == null) {
                lockOnTicks = 0;
                if (forceTarget != null) {
                    targetedCatalyst = (VoidCatalystEntity) ((ServerLevel) level).getEntity(forceTarget);
                    forceTarget = null;
                } else {
                    List<Entity> entities = level.getEntities(null, AABB.ofSize(getBlockPos().getCenter(), 40, 40, 40));
                    float closest = Float.MAX_VALUE;
                    for (Entity entity : entities) {
                        if (entity instanceof VoidCatalystEntity v && !v.hasLink) {
                            // target closest catalyst
                            float dist = (float) getBlockPos().getCenter().distanceTo(v.position());
                            if (dist < closest) {
                                closest = dist;
                                targetedCatalyst = v;
                            }

                        }
                    }
                }

                if (targetedCatalyst != null) targetedCatalyst.hasLink = true;
            } else {
                if (lockOnTicks < 20 && level.hasNeighborSignal(getBlockPos())) {
                    lockOnTicks += abs(getSpeed()) / 128f * 0.5f;
                }

                if (voidImplosionTimer == -1 && isSpeedRequirementFulfilled() && level.hasNeighborSignal(getBlockPos()) && tankInventory.getFluidAmount() >= 1000 && lockOnTicks >= 20) {
                    voidImplosionTimer = 80;
                    voidVFXInstanceIndex = ++voidVFXGlobalIndex;
                    PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(getBlockPos()).getPos(), new BeginVoidVFXPacket(targetedCatalyst.position().toVector3f(), voidVFXInstanceIndex));
                }

                if (voidImplosionTimer > 0) {
                    voidImplosionTimer--;
                    PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(getBlockPos()).getPos(), new UpdateVoidVFXPositionPacket(targetedCatalyst.position().toVector3f(), voidVFXInstanceIndex));
                    if (tankInventory.getFluidAmount() < 1000) {
                        targetedCatalyst.hasLink = false;
                        targetedCatalyst = null;
                    }
                }
                if (voidImplosionTimer == 0 && targetedCatalyst != null) {
                    voidImplosionTimer = -1;
                    lockOnTicks = 0;
                    tankInventory.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    Vec3 position = targetedCatalyst.position();

                    AABB area = AABB.ofSize(position, 12, 12, 12);
                    BlockPos.betweenClosedStream(area).forEach(p -> {
                        Vec3 center = p.getCenter();
                        if (center.distanceTo(position) <= 6) {
                            if (level.getBlockState(p).getBlock() != Blocks.AIR)
                                ((ServerLevel) level).sendParticles(ParticleTypes.END_ROD, center.x, center.y, center.z, 2, 0, 0, 0, 0.05);
                            if (p.equals(getBlockPos())) {
                                level.getNearbyPlayers(TargetingConditions.forNonCombat(), null, area.inflate(12))
                                        .forEach(player -> {
                                            System.out.println(player);
                                            VoidboundAdvancementProvider.ADVANCEMENT_TRIGGERS.get("destroy_anchor").trigger((ServerPlayer) player);
                                        });
                            }
                            level.destroyBlock(p, true);
                        }
                    });

                    level.getEntities(EntityTypeTest.forClass(LivingEntity.class), area, e -> true).forEach(entity -> {
                        if (entity.getPosition(0).distanceTo(position) <= 6) {
                            if (entity instanceof Player) {
                                entity.hurt(Voidbound.damageSource(entity.level(), Voidbound.RAW_VOID_ESSENCE_DAMAGE), 500);
                            } else {
                                entity.remove(Entity.RemovalReason.KILLED);
                            }
                        }

                    });

                    targetedCatalyst.remove(Entity.RemovalReason.DISCARDED);
                    targetedCatalyst = null;
                }


                if (targetedCatalyst != null && targetedCatalyst.isRemoved()) {
                    targetedCatalyst = null;
                }

                if (targetedCatalyst == null && voidImplosionTimer != -1) {
                    voidImplosionTimer = -1;
                    PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(getBlockPos()).getPos(), new StopVoidVFXPacket(voidVFXInstanceIndex));
                }
            }

            List<Entity> entities = level.getEntities(null, AABB.ofSize(getBlockPos().getCenter(), 40, 40, 40));
            for (Entity entity : entities) {
                if (entity instanceof VoidCatalystEntity v && (!v.hasLink || v == targetedCatalyst)) {
                    for (int i = 0; i < Math.ceil(getBlockPos().getCenter().distanceTo(v.position()) * 2); i++) {
                        float dist = level.random.nextFloat();
                        Vec3 normVector = v.position().subtract(getBlockPos().getCenter()).multiply(dist, dist, dist);
                        Vec3 p = getBlockPos().getCenter().add(normVector);

                        ParticleOptions particle = v == targetedCatalyst && dist <= (lockOnTicks / 20) ? VoidboundParticles.SPARK_CONNECTED.get() : ParticleTypes.ELECTRIC_SPARK;
                        ((ServerLevel) level).sendParticles(particle, p.x, p.y + 0.25, p.z, 0, level.random.nextGaussian() * 0.1, level.random.nextGaussian() * 0.1, level.random.nextGaussian() * 0.1, 1);
                    }

                }
            }


        } else {
            age++;
        }
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        compound.put("TankContent", tankInventory.writeToNBT(registries, new CompoundTag()));
        compound.put("Level", fluidLevel.writeNBT());
        if (targetedCatalyst != null && !targetedCatalyst.isRemoved()) {
            compound.putUUID("Target", targetedCatalyst.getUUID());
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        tankInventory.readFromNBT(registries, compound.getCompound("TankContent"));
        fluidLevel.readNBT(compound.getCompound("Level"), clientPacket);
        if (compound.hasUUID("Target")) {
            forceTarget = compound.getUUID("Target");
        }
    }

    public void forceConnectToCatalyst(VoidCatalystEntity catalyst) {
        if (this.targetedCatalyst != null) {
            targetedCatalyst.hasLink = false;
        }

        targetedCatalyst = catalyst;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean kineticTooltip = super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        return containedFluidTooltip(tooltip, isPlayerSneaking,
                level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos(), null)) || kineticTooltip;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                VoidboundBlockEntityTypes.VOID_CATALYST_ANCHOR_BLOCK_ENTITY.get(),
                (be, context) -> be.tankInventory
        );
    }

    @Override
    public void remove() {
        super.remove();
        if (!level.isClientSide) {
            if (targetedCatalyst != null) targetedCatalyst.hasLink = false;
            if (voidImplosionTimer != -1) {
                PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(getBlockPos()).getPos(), new StopVoidVFXPacket(voidVFXInstanceIndex));
            }
        }
    }

    public int age() {
        return age;
    }

    public LerpedFloat getFluidLevel() {
        return fluidLevel;
    }

    public void setFluidLevel(float level) {
        this.fluidLevel.setValue(level);
    }
}
