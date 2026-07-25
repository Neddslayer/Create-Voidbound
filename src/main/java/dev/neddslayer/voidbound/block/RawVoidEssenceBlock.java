package dev.neddslayer.voidbound.block;

import com.simibubi.create.foundation.block.IBE;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.blockentity.RawVoidEssenceBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class RawVoidEssenceBlock extends LiquidBlock implements IBE<RawVoidEssenceBlockEntity> {
    public RawVoidEssenceBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (entity instanceof LivingEntity) {
            entity.hurt(Voidbound.damageSource(level, Voidbound.RAW_VOID_ESSENCE_DAMAGE), 1);
            entity.push(0.1f * Math.random() - 0.05, 0.02, 0.1f * Math.random() - 0.05);
        } else if (entity instanceof ItemEntity item) {
            if (item.getItem().getItem() == VoidboundItems.PURIFICATION_CRYSTAL.get() && state.getValue(LEVEL) == 0){
                ((RawVoidEssenceBlockEntity) level.getBlockEntity(pos)).startPurification();
                item.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public Class<RawVoidEssenceBlockEntity> getBlockEntityClass() {
        return RawVoidEssenceBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RawVoidEssenceBlockEntity> getBlockEntityType() {
        return VoidboundBlockEntityTypes.RAW_VOID_ESSENCE_BLOCK_ENTITY.get();
    }
}
