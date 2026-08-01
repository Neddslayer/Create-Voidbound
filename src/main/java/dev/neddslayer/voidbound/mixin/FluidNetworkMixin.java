package dev.neddslayer.voidbound.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.FluidNetwork;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyFluidHandler;
import com.simibubi.create.foundation.fluid.FluidHelper;
import dev.neddslayer.voidbound.config.Config;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidNetwork.class)
public class FluidNetworkMixin {

    // The main idea behind this is to make void fluid difficult to extract, but simple to transfer as to not unnecessarily impede gameplay.
    @Unique
    private static boolean veilTest$extractingVoidFluidFromHose = false;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/fluid/FluidHelper;copyStackWithAmount(Lnet/neoforged/neoforge/fluids/FluidStack;I)Lnet/neoforged/neoforge/fluids/FluidStack;"))
    private FluidStack slowDrainVoidFluid(FluidStack fs, int amount) {
        amount = veilTest$extractingVoidFluidFromHose ? Mth.ceil(amount * Config.EXTRACT_RATE.getAsDouble()) : amount;
        if (veilTest$extractingVoidFluidFromHose) veilTest$extractingVoidFluidFromHose = false;
        return FluidHelper.copyStackWithAmount(fs, amount);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/fluid/FluidHelper;copyStackWithAmount(Lnet/neoforged/neoforge/fluids/FluidStack;I)Lnet/neoforged/neoforge/fluids/FluidStack;", shift = At.Shift.BEFORE))
    private void slowExtractVoidFluid(CallbackInfo ci, @Local(name = "sourceCap") IFluidHandler sourceCap, @Local(name = "contained") FluidStack contained) {
        if (sourceCap instanceof HosePulleyFluidHandler && contained.getFluidType() == VoidboundFluids.RAW_VOID_ESSENCE.getType()) {
            veilTest$extractingVoidFluidFromHose = true;
        }
    }

}

