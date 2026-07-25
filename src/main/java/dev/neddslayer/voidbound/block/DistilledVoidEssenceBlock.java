package dev.neddslayer.voidbound.block;

import com.simibubi.create.foundation.block.IBE;
import dev.neddslayer.voidbound.blockentity.DistilledVoidEssenceBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundBlockEntityTypes;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.FlowingFluid;

public class DistilledVoidEssenceBlock extends LiquidBlock implements IBE<DistilledVoidEssenceBlockEntity> {
    public DistilledVoidEssenceBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public Class<DistilledVoidEssenceBlockEntity> getBlockEntityClass() {
        return DistilledVoidEssenceBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DistilledVoidEssenceBlockEntity> getBlockEntityType() {
        return VoidboundBlockEntityTypes.DISTILLED_VOID_ESSENCE_BLOCK_ENTITY.get();
    }
}
