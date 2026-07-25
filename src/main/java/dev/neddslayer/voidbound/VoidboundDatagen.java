package dev.neddslayer.voidbound;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import dev.neddslayer.voidbound.datagen.VoidboundAdvancementProvider;
import dev.neddslayer.voidbound.datagen.VoidboundProcessingRecipeProvider;
import dev.neddslayer.voidbound.datagen.VoidboundSoundProvider;
import dev.neddslayer.voidbound.datagen.VoidboundStandardRecipeProvider;
import dev.neddslayer.voidbound.fluid.VoidingFanProcessingType;
import dev.neddslayer.voidbound.ponder.VoidboundPonderPlugin;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundSounds;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.advancements.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static dev.neddslayer.voidbound.Voidbound.MODID;

@EventBusSubscriber(modid = MODID)
public class VoidboundDatagen {
    public static void gatherDataHighPriority(GatherDataEvent event) {
        Voidbound.registrate().addDataGenerator(ProviderType.FLUID_TAGS, provIn -> {
            TagGen.CreateTagsProvider<Fluid> prov = new TagGen.CreateTagsProvider<>(provIn, Fluid::builtInRegistryHolder);

            prov.tag(VoidingFanProcessingType.FAN_PROCESSING_CATALYST_FLUID).add(VoidboundFluids.RAW_VOID_ESSENCE.getSource().getSource(), VoidboundFluids.RAW_VOID_ESSENCE.getSource().getFlowing());
        });

        Voidbound.registrate().addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> consumer = provider::add;

            VoidboundAdvancementProvider.acceptLang(consumer);
            VoidboundSounds.acceptLang(consumer);
            acceptTooltipLang(consumer);
            providePonderLang(consumer);
            consumer.accept("itemGroup.voidbound", "Create: Voidbound");

            consumer.accept("death.attack.raw_void_essence", "%s was dissolved");
            consumer.accept("death.attack.raw_void_essence.player", "%s was dissolved whilst fighting %s");
        });
    }

    @SubscribeEvent // on the mod event bus
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new VoidboundSoundProvider(output, existingFileHelper));

        // Advancements
        generator.addProvider(
                event.includeServer(),
                new VoidboundAdvancementProvider(output, lookupProvider, existingFileHelper)
        );
        generator.addProvider(
                event.includeServer(),
                new VoidboundStandardRecipeProvider(output, lookupProvider)
        );

        if (event.includeServer()) {
            VoidboundProcessingRecipeProvider.registerProcessing(generator, output, lookupProvider);
        }
    }

    @SubscribeEvent
    public static void onRegister(final RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES) VoidboundAdvancementProvider.registerAdvancements();
        else if (event.getRegistry() == CreateBuiltInRegistries.FAN_PROCESSING_TYPE) {
            Registry.register(CreateBuiltInRegistries.FAN_PROCESSING_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "voiding"), new VoidingFanProcessingType());
        }
    }

    private static void acceptTooltipLang(BiConsumer<String, String> consumer) {
        consumer.accept(
                "item.voidbound.purification_crystal.tooltip.summary",
                "A _mysterious crystal_."
        );
        consumer.accept(
                "item.voidbound.purification_crystal.tooltip.condition1",
                "When thrown into Raw Void Essence"
        );
        consumer.accept(
                "item.voidbound.purification_crystal.tooltip.behaviour1",
                "_Purifies_ Raw Void Essence, turning it into _Distilled Void Essence._"
        );
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        PonderIndex.addPlugin(new VoidboundPonderPlugin());

        PonderIndex.getLangAccess().provideLang(MODID, consumer);
    }

    public record VoidboundAdvancementHolder(Advancement.Builder builder, String parent, String name) {}
}
