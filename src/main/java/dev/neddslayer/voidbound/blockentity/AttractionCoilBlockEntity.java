package dev.neddslayer.voidbound.blockentity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class AttractionCoilBlockEntity extends KineticBlockEntity {
    private float attractionRadius = 0;

    public AttractionCoilBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (isSpeedRequirementFulfilled()) {
            List<Entity> entities = level.getEntities(null, AABB.ofSize(getBlockPos().getCenter(), attractionRadius, attractionRadius, attractionRadius));
            Vec3i normal = getBlockState().getValue(FACING).getNormal();
            Vec3 origin = getBlockPos().getCenter().add(normal.getX(), normal.getY(), normal.getZ());
            for (Entity entity : entities) {
                Vec3 p = entity.position().subtract(origin);
                if (p.length() < attractionRadius) {
                    double factor = (-(attractionRadius - p.length()) / attractionRadius) * (getSpeed() / 1024);
                    entity.push(p.normalize().multiply(factor, factor, factor));
                }
            }
            if (level.isClientSide) {
                for (int i = 0; i < Mth.ceil(getSpeed() / 64); i++) {
                    level.addParticle(VoidboundParticles.ATTRACT.get(), origin.x, origin.y, origin.z, attractionRadius, getSpeed() / 2048, 0);
                }
            }
        }

    }

    @Override
    public void lazyTick() {
        attractionRadius = level.getBestNeighborSignal(getBlockPos()) * 0.5f;

    }
}
