package dev.neddslayer.voidbound.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BottleItem.class)
public abstract class BottleItemMixin {

    @Shadow
    protected abstract ItemStack turnBottleIntoItem(ItemStack bottleStack, Player player, ItemStack filledBottleStack);

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z", shift = At.Shift.AFTER), cancellable = true)
    private void getVoidFluid(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local BlockPos blockpos, @Local ItemStack itemStack) {
        if (level.getFluidState(blockpos).getFluidType() == VoidboundFluids.RAW_VOID_ESSENCE.getType()) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
            cir.setReturnValue(
                    InteractionResultHolder.sidedSuccess(turnBottleIntoItem(itemStack, player, new ItemStack(VoidboundItems.RAW_VOID_ESSENCE_BOTTLE.get())), level.isClientSide())
            );
        }

        if (level.getFluidState(blockpos).getFluidType() == VoidboundFluids.DISTILLED_VOID_ESSENCE.getType()) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
            cir.setReturnValue(
                    InteractionResultHolder.sidedSuccess(turnBottleIntoItem(itemStack, player, new ItemStack(VoidboundItems.DISTILLED_VOID_ESSENCE_BOTTLE.get())), level.isClientSide())
            );
        }
    }

}
