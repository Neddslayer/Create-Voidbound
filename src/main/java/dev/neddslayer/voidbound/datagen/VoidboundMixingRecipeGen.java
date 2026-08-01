package dev.neddslayer.voidbound.datagen;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class VoidboundMixingRecipeGen extends MixingRecipeGen {
    GeneratedRecipe
            VOID_GEM = create("void_gem", b -> b
                    .require(VoidboundFluids.RAW_VOID_ESSENCE.get(), 500)
                    .require(Items.DIAMOND)
                    .require(Items.DIAMOND)
                    .output(VoidboundItems.VOID_GEM)
                    .duration(200)
            ),
            PURIFICATION_CRYSTAL = create("purification_crystal", b -> b
                    .require(Fluids.WATER, 250)
                    .require(Items.QUARTZ)
                    .require(Items.QUARTZ)
                    .require(Items.AMETHYST_SHARD)
                    .require(Items.DIAMOND)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(VoidboundItems.PURIFICATION_CRYSTAL)
                    .duration(250)
            ),
            VOID_CATALYST = create("void_catalyst", b -> b
                    .require(VoidboundFluids.DISTILLED_VOID_ESSENCE.get(), 1000)
                    .require(VoidboundItems.VOID_GEM)
                    .require(VoidboundItems.VOID_GEM)
                    .require(AllItems.POLISHED_ROSE_QUARTZ)
                    .require(AllItems.POLISHED_ROSE_QUARTZ)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(VoidboundItems.VOID_CATALYST_ITEM)
                    .averageProcessingDuration()
            ),
            INFUSED_GOLD = create("infused_gold", b -> b
                    .require(VoidboundFluids.DISTILLED_VOID_ESSENCE.get(), 250)
                    .require(AllItems.CRUSHED_GOLD)
                    .output(VoidboundItems.INFUSED_GOLD)
                    .averageProcessingDuration()
            );

    public VoidboundMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Voidbound.MODID);
    }
}
