package dev.neddslayer.voidbound.ponder;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.registrar.VoidboundBlocks;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class VoidboundPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return Voidbound.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(VoidboundBlocks.BRASS_DRILL, "brass_drill", VoidboundPonderScenes::brassDrill, AllCreatePonderTags.KINETIC_APPLIANCES);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addToTag(AllCreatePonderTags.KINETIC_SOURCES)
                .add(VoidboundBlocks.VOID_MOTOR);

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES)
                .add(VoidboundBlocks.REPULSION_COIL)
                .add(VoidboundBlocks.ATTRACTION_COIL)
                .add(VoidboundBlocks.BRASS_DRILL);


    }
}
