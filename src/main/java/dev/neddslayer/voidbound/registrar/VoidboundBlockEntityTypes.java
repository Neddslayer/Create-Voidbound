package dev.neddslayer.voidbound.registrar;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.blockentity.*;
import dev.neddslayer.voidbound.renderer.blockentity.*;
import dev.neddslayer.voidbound.renderer.visual.BrassDrillVisual;
import dev.neddslayer.voidbound.renderer.visual.VoidMotorVisual;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@SuppressWarnings("unused")
public class VoidboundBlockEntityTypes {

    private static final CreateRegistrate REGISTRATE = Voidbound.registrate();

    public static final BlockEntityEntry<RawVoidEssenceBlockEntity> RAW_VOID_ESSENCE_BLOCK_ENTITY = REGISTRATE
            .blockEntity("raw_void_essence", RawVoidEssenceBlockEntity::new)
            .validBlock(Voidbound.registrate().get("raw_void_essence", BuiltInRegistries.BLOCK.key()))
            .renderer(() -> RawVoidEssenceRenderer::new)
            .register();
    public static final BlockEntityEntry<DistilledVoidEssenceBlockEntity> DISTILLED_VOID_ESSENCE_BLOCK_ENTITY = REGISTRATE
            .blockEntity("distilled_void_essence", DistilledVoidEssenceBlockEntity::new)
            .validBlock(Voidbound.registrate().get("distilled_void_essence", BuiltInRegistries.BLOCK.key()))
            .renderer(() -> DistilledVoidEssenceRenderer::new)
            .register();

    public static final BlockEntityEntry<BrassDrillBlockEntity> BRASS_DRILL_BLOCK_ENTITY = REGISTRATE
            .blockEntity("brass_drill", BrassDrillBlockEntity::new)
            .visual(() -> BrassDrillVisual::new)
            .validBlocks(VoidboundBlocks.BRASS_DRILL)
            .renderer(() -> BrassDrillRenderer::new)
            .register();

    public static final BlockEntityEntry<VoidMotorBlockEntity> VOID_MOTOR_BLOCK_ENTITY = REGISTRATE
            .blockEntity("void_motor", VoidMotorBlockEntity::new)
            .visual(() -> VoidMotorVisual::new)
            .validBlocks(VoidboundBlocks.VOID_MOTOR)
            .register();

    public static final BlockEntityEntry<RepulsionCoilBlockEntity> REPULSION_COIL_BLOCK_ENTITY = REGISTRATE
            .blockEntity("repulsion_coil", RepulsionCoilBlockEntity::new)
            .visual(() -> ofOpposite(AllPartialModels.SHAFT_HALF))
            .validBlocks(VoidboundBlocks.REPULSION_COIL)
            .renderer(() -> RepulsionCoilRenderer::new)
            .register();

    public static final BlockEntityEntry<AttractionCoilBlockEntity> ATTRACTION_COIL_BLOCK_ENTITY = REGISTRATE
            .blockEntity("attraction_coil", AttractionCoilBlockEntity::new)
            .visual(() -> ofOpposite(AllPartialModels.SHAFT_HALF))
            .validBlocks(VoidboundBlocks.ATTRACTION_COIL)
            .renderer(() -> AttractionCoilRenderer::new)
            .register();

    public static final BlockEntityEntry<VoidCatalystAnchorBlockEntity> VOID_CATALYST_ANCHOR_BLOCK_ENTITY = REGISTRATE
            .blockEntity("void_catalyst_anchor", VoidCatalystAnchorBlockEntity::new)
            .visual(() -> ofDirection(AllPartialModels.SHAFT_HALF, Direction.DOWN))
            .validBlocks(VoidboundBlocks.VOID_CATALYST_ANCHOR)
            .renderer(() -> VoidCatalystAnchorRenderer::new)
            .register();

    public static void register() {}

    private static <T extends KineticBlockEntity> SimpleBlockEntityVisualizer.Factory<T> ofOpposite(PartialModel model) {
        return (context, blockEntity, partialTick) -> {
            Direction facing = blockEntity.getBlockState()
                    .getValue(BlockStateProperties.FACING)
                    .getOpposite();
            return new OrientedRotatingVisual<>(context, blockEntity, partialTick, Direction.SOUTH, facing, Models.partial(model));
        };
    }

    private static <T extends KineticBlockEntity> SimpleBlockEntityVisualizer.Factory<T> ofDirection(PartialModel model, Direction facing) {
        return (context, blockEntity, partialTick) -> new OrientedRotatingVisual<>(context, blockEntity, partialTick, Direction.SOUTH, facing, Models.partial(model));
    }


}
