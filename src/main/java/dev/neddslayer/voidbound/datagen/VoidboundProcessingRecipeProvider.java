package dev.neddslayer.voidbound.datagen;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VoidboundProcessingRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();

    public VoidboundProcessingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {}

    public static void registerProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        GENERATORS.add(new VoidboundMixingRecipeGen(output, registries));
        GENERATORS.add(new VoidboundPressingRecipeGen(output, registries));

        gen.addProvider(true, new DataProvider() {
            @Override
            public String getName() {
                return "Create: Voidbound's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        });
    }
}
