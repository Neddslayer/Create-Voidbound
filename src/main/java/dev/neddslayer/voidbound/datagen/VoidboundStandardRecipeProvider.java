package dev.neddslayer.voidbound.datagen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.registrar.VoidboundBlocks;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class VoidboundStandardRecipeProvider extends BaseRecipeProvider {
    public VoidboundStandardRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Voidbound.MODID);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, VoidboundBlocks.VOID_MOTOR.asStack())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().of(VoidboundItems.VOID_GEM)))
                .define('F', AllBlocks.FLUID_TANK.asItem())
                .define('V', VoidboundItems.VOID_GEM_SHEET)
                .define('S', AllBlocks.SHAFT.asItem())
                .pattern("FVF")
                .pattern("VSV")
                .pattern("FVF")
                .save(recipeOutput, Voidbound.path("void_motor"));

        new ShapedRecipeBuilder(RecipeCategory.MISC, VoidboundBlocks.BRASS_DRILL.asStack())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().of(AllBlocks.MECHANICAL_DRILL)))
                .define('B', AllBlocks.BRASS_CASING)
                .define('M', AllBlocks.MECHANICAL_DRILL)
                .pattern(" B ")
                .pattern("BMB")
                .pattern(" B ")
                .save(recipeOutput, Voidbound.path("brass_drill"));

        new ShapedRecipeBuilder(RecipeCategory.MISC, VoidboundBlocks.REPULSION_COIL.asStack())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().of(VoidboundItems.RAW_VOID_ESSENCE_BOTTLE)))
                .define('B', VoidboundItems.RAW_VOID_ESSENCE_BOTTLE)
                .define('S', AllBlocks.SHAFT)
                .define('C', AllBlocks.BRASS_CASING)
                .pattern(" B ")
                .pattern(" S ")
                .pattern(" C ")
                .save(recipeOutput, Voidbound.path("repulsion_coil"));

        new ShapedRecipeBuilder(RecipeCategory.MISC, VoidboundBlocks.ATTRACTION_COIL.asStack())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().of(VoidboundItems.DISTILLED_VOID_ESSENCE_BOTTLE)))
                .define('B', VoidboundItems.DISTILLED_VOID_ESSENCE_BOTTLE)
                .define('S', AllBlocks.SHAFT)
                .define('C', AllBlocks.BRASS_CASING)
                .pattern(" B ")
                .pattern(" S ")
                .pattern(" C ")
                .save(recipeOutput, Voidbound.path("attraction_coil"));

        new ShapedRecipeBuilder(RecipeCategory.MISC, VoidboundBlocks.VOID_CATALYST_ANCHOR.asStack())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().of(Items.NETHER_STAR)))
                .define('G', Items.GLASS_PANE)
                .define('S', Items.NETHER_STAR)
                .define('N', Items.NETHERITE_INGOT)
                .pattern(" G ")
                .pattern("GSG")
                .pattern("NNN")
                .save(recipeOutput, Voidbound.path("void_catalyst_anchor"));

        new ShapelessRecipeBuilder(RecipeCategory.MISC, VoidboundBlocks.REPULSION_COIL.asStack())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().of(VoidboundItems.RAW_VOID_ESSENCE_BOTTLE)))
                .requires(VoidboundItems.RAW_VOID_ESSENCE_BOTTLE)
                .requires(VoidboundBlocks.ATTRACTION_COIL)
                .save(recipeOutput, Voidbound.path("repulsion_coil_convert"));

        new ShapelessRecipeBuilder(RecipeCategory.MISC, VoidboundBlocks.ATTRACTION_COIL.asStack())
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().of(VoidboundItems.DISTILLED_VOID_ESSENCE_BOTTLE)))
                .requires(VoidboundItems.DISTILLED_VOID_ESSENCE_BOTTLE)
                .requires(VoidboundBlocks.REPULSION_COIL)
                .save(recipeOutput, Voidbound.path("attraction_coil_convert"));
    }

}
