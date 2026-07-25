package dev.neddslayer.voidbound.blockentity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.neddslayer.voidbound.network.RepulsePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class RepulsionCoilBlockEntity extends KineticBlockEntity {

    private float repulsionTimer = 8;

    private float repulseServerTime = 0;
    private float repulseRadius = 0;

    public RepulsionCoilBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(4);
    }

    @Override
    public void tick() {
        super.tick();
        if (repulseServerTime >= 0) {
            repulseServerTime--;

            float radius = getRadius();
            float finalRadius = repulseRadius;
            float power = (1f - (radius / finalRadius)) * finalRadius * 0.25f;
            Vec3i normal = getBlockState().getValue(FACING).getNormal();
            List<Entity> entities = level.getEntities(null, AABB.ofSize(getBlockPos().getCenter(), repulseRadius, repulseRadius, repulseRadius));
            for (Entity entity : entities) {
                if (entity instanceof Player) continue;
                Vec3 p = entity.position().subtract(getBlockPos().getCenter().add(normal.getX(), normal.getY(), normal.getZ()));
                if (p.length() < radius) {
                    entity.push(p.normalize().multiply(power, power, power));
                }
            }

        }
    }

    private float getRadius() {
        return (float) (1 - Math.pow(1 - (((20 - repulseServerTime) + 0.5) / 20f), 5)) * repulseRadius;
    }

    @Override
    public void lazyTick() {
        if (!level.isClientSide) {
            if (isSpeedRequirementFulfilled() && level.hasNeighborSignal(getBlockPos())) repulsionTimer -= Math.abs(getSpeed()) / 128f;
            if (repulsionTimer <= 0) {
                repulse();
                repulsionTimer = 8;
            }
        }
    }

    private void repulse() {
        repulseServerTime = 20;
        repulseRadius = level.getBestNeighborSignal(getBlockPos()) * 0.5f;
        Vec3i normal = getBlockState().getValue(FACING).getNormal();
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, level.getChunkAt(getBlockPos()).getPos(), new RepulsePacket(getBlockPos().getCenter().toVector3f().add(normal.getX(), normal.getY(), normal.getZ()), repulseRadius));
    }
}
