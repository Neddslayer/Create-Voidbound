package dev.neddslayer.voidbound.datagen;

import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class VoidboundPressingRecipeGen extends PressingRecipeGen {
    GeneratedRecipe VOID_GEM_SHEET = create("void_gem_sheet", b -> b
            .require(VoidboundItems.VOID_GEM)
            .output(VoidboundItems.VOID_GEM_SHEET)
    );

    public VoidboundPressingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Voidbound.MODID);
    }
}
