package dev.neddslayer.voidbound.mixin;

import com.simibubi.create.content.fluids.transfer.FluidDrainingBehaviour;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidDrainingBehaviour.class)
public class FluidDrainingBehaviourMixin {

    @Inject(method = "getDrainableFluid", at = @At("HEAD"), cancellable = true)
    private void getVoidFluid(BlockPos rootPos, CallbackInfoReturnable<FluidStack> cir) {
        if (rootPos.getY() < -72) cir.setReturnValue(new FluidStack(VoidboundFluids.RAW_VOID_ESSENCE, 1));
    }

    @Inject(method = "pullNext", at = @At("HEAD"), cancellable = true)
    private void pullVoidFluid(BlockPos root, boolean simulate, CallbackInfoReturnable<Boolean> cir) {
        if (root.getY() < -72) cir.setReturnValue(true);
    }
}
