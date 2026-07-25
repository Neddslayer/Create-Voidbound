package dev.neddslayer.voidbound.registrar;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.block.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static dev.neddslayer.voidbound.Voidbound.CREATIVE_TAB;

@SuppressWarnings("unused")
public class VoidboundBlocks {
    private static final CreateRegistrate REGISTRATE = Voidbound.registrate();

    static {
        REGISTRATE.setCreativeTab(CREATIVE_TAB);
    }

    public static final BlockEntry<DrilledBedrockBlock> DRILLED_BEDROCK = REGISTRATE
            .block("drilled_bedrock", p -> new DrilledBedrockBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)))
            .lang("Drilled Bedrock")
            .blockstate(((blockBlockDataGenContext, registrateBlockstateProvider) -> {}))
            .simpleItem()
            .register();

    public static final BlockEntry<BrassDrillBlock> BRASS_DRILL = REGISTRATE
            .block("brass_drill", BrassDrillBlock::new)
            .lang("Brass Drill")
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .transform(pickaxeOnly())
            .onRegister((block) -> BlockStressValues.IMPACTS.register(block, () -> 16))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<VoidMotorBlock> VOID_MOTOR = REGISTRATE
            .block("void_motor", VoidMotorBlock::new)
            .lang("Void Motor")
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_PURPLE))
            .blockstate((ctx, provider) -> BlockStateGen.directionalBlockIgnoresWaterlogged(ctx, provider, s -> AssetLookup.partialBaseModel(ctx, provider)))
            .onRegister((block) -> BlockStressValues.CAPACITIES.register(block, () -> 512))
            .onRegister(BlockStressValues.setGeneratorSpeed(32))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<RepulsionCoilBlock> REPULSION_COIL = REGISTRATE
            .block("repulsion_coil", RepulsionCoilBlock::new)
            .lang("Repulsion Coil")
            .initialProperties(SharedProperties::copperMetal)
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(p -> p.mapColor(MapColor.RAW_IRON))
            .blockstate((ctx, provider) -> BlockStateGen.directionalBlockIgnoresWaterlogged(ctx, provider, s -> AssetLookup.partialBaseModel(ctx, provider)))
            .onRegister((block) -> BlockStressValues.IMPACTS.register(block, () -> 8))
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<AttractionCoilBlock> ATTRACTION_COIL = REGISTRATE
            .block("attraction_coil", AttractionCoilBlock::new)
            .lang("Attraction Coil")
            .initialProperties(SharedProperties::copperMetal)
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(p -> p.mapColor(MapColor.RAW_IRON))
            .blockstate((ctx, provider) -> BlockStateGen.directionalBlockIgnoresWaterlogged(ctx, provider, s -> AssetLookup.partialBaseModel(ctx, provider)))
            .onRegister((block) -> BlockStressValues.IMPACTS.register(block, () -> 8))
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<VoidCatalystAnchorBlock> VOID_CATALYST_ANCHOR = REGISTRATE
            .block("void_catalyst_anchor", VoidCatalystAnchorBlock::new)
            .lang("Void Catalyst Anchor")
            .initialProperties(SharedProperties::netheriteMetal)
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
            .blockstate((ctx, provider) -> BlockStateGen.simpleBlock(ctx, provider, s -> AssetLookup.partialBaseModel(ctx, provider)))
            .onRegister(b -> BlockStressValues.IMPACTS.register(b, () -> 128))
            .transform(pickaxeOnly())
            .item()
            .properties(Item.Properties::fireResistant)
            .transform(customItemModel())
            .register();


    public static void register() {}
}
