package dev.neddslayer.voidbound.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HosePulleyBlockEntity.class)
public abstract class HosePulleyBlockEntityMixin extends KineticBlockEntity {

    public HosePulleyBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Redirect(method = "onSpeedChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced()Z"))
    private boolean allowPassthroughDrilledBedrock(BlockState instance, @Local(name = "newOffset") float newOffset) {
        BlockState state = level.getBlockState(worldPosition.below((int) Math.ceil(newOffset)));
        return state.canBeReplaced() || state.getBlock() == VoidboundBlocks.DRILLED_BEDROCK.get();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced()Z"))
    private boolean allowPassthroughDrilledBedrockOnTick(BlockState instance, @Local(name = "newOffset") float newOffset) {
        BlockState state = level.getBlockState(worldPosition.below((int) Math.ceil(newOffset)));
        return state.canBeReplaced() || state.getBlock() == VoidboundBlocks.DRILLED_BEDROCK.get();
    }

    @Redirect(method = "lazyTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced()Z"))
    private boolean allowPassthroughDrilledBedrockOnLazyTick(BlockState instance, @Local(name = "ceil") int ceil) {
        BlockState state = level.getBlockState(worldPosition.below(ceil));
        return state.canBeReplaced() || state.getBlock() == VoidboundBlocks.DRILLED_BEDROCK.get();
    }

}
