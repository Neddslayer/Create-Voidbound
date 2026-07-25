package dev.neddslayer.voidbound.item;

import dev.neddslayer.voidbound.entity.RawVoidEssenceBottleProjectile;
import dev.neddslayer.voidbound.entity.VoidCatalystEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VoidCatalystItem extends Item {
    public VoidCatalystItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            VoidCatalystEntity catalystEntity = new VoidCatalystEntity(level);
            catalystEntity.setPos(player.getEyePosition());
            catalystEntity.setDeltaMovement(player.getLookAngle().normalize().multiply(0.4, 0.4, 0.4).add(player.getDeltaMovement()));
            level.addFreshEntity(catalystEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }


}
