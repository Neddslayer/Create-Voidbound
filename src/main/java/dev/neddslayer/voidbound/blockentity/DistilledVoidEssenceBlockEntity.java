package dev.neddslayer.voidbound.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DistilledVoidEssenceBlockEntity extends BlockEntity {
    public DistilledVoidEssenceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
