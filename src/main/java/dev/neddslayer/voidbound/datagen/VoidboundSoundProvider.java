package dev.neddslayer.voidbound.datagen;

import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.registrar.VoidboundSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import static dev.neddslayer.voidbound.Voidbound.string;

public class VoidboundSoundProvider extends SoundDefinitionsProvider {
    public VoidboundSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Voidbound.MODID, helper);
    }

    @Override
    public void registerSounds() {
        add(VoidboundSounds.PURIFY, SoundDefinition.definition()
                .with(
                        sound(string("purify"), SoundDefinition.SoundType.SOUND)
                                .volume(0.8))
                .subtitle("sound.voidbound.purify")
        );

        add(VoidboundSounds.CATALYST_IMPLODE, SoundDefinition.definition()
                .with(
                        sound(string("catalyst_implode"), SoundDefinition.SoundType.SOUND)
                                .volume(2)
                                .attenuationDistance(32))
                        .subtitle("sound.voidbound.catalyst_implode")
                );
    }
}
