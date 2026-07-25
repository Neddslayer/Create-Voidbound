package dev.neddslayer.voidbound.fluid;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import dev.neddslayer.voidbound.Voidbound;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.neddslayer.voidbound.Voidbound.MODID;

public class VoidingFanProcessingType implements FanProcessingType {
    public static final TagKey<Fluid> FAN_PROCESSING_CATALYST_FLUID = TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(MODID, "fan_processing_catalysts/voiding"));
    public static final TagKey<Block> FAN_PROCESSING_CATALYST_BLOCK = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MODID, "fan_processing_catalysts/voiding"));

    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);
        if (fluidState.is(FAN_PROCESSING_CATALYST_FLUID)) {
            return true;
        }
        BlockState blockState = level.getBlockState(pos);
        return blockState.is(FAN_PROCESSING_CATALYST_BLOCK);
    }

    @Override
    public int getPriority() {
        return 500;
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        return false;
    }

    @Override
    public @Nullable List<ItemStack> process(ItemStack stack, Level level) {
        return List.of();
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(8) != 0)
            return;
        pos = pos.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                .multiply(1, 0.05f, 1)
                .normalize()
                .scale(0.15f));
        level.addParticle(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y + .45f, pos.z, 0, 0, 0);
        if (level.random.nextInt(2) == 0)
            level.addParticle(ParticleTypes.SMOKE, pos.x, pos.y + .25f, pos.z, 0, 0, 0);
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(Color.mixColors(0x0, 0x631868, random.nextFloat()));
        particleAccess.setAlpha(1f);
        if (random.nextFloat() < 1 / 128f)
            particleAccess.spawnExtraParticle(ParticleTypes.ANGRY_VILLAGER, .125f);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(ParticleTypes.SMOKE, .125f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        entity.hurt(Voidbound.damageSource(level, Voidbound.RAW_VOID_ESSENCE_DAMAGE), 1);
    }
}
