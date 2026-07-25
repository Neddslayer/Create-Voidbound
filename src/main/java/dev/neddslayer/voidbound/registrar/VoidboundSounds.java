package dev.neddslayer.voidbound.registrar;

import dev.neddslayer.voidbound.Voidbound;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiConsumer;

public class VoidboundSounds {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Voidbound.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PURIFY = SOUND_EVENTS.register(
            "purify",
            () -> SoundEvent.createVariableRangeEvent(Voidbound.path("purify"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> CATALYST_IMPLODE = SOUND_EVENTS.register(
            "catalyst_implode",
            () -> SoundEvent.createVariableRangeEvent(Voidbound.path("catalyst_implode"))
    );

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }

    public static void acceptLang(BiConsumer<String, String> consumer) {
        consumer.accept("sound.voidbound.purify", "Void Essence purifies");
        consumer.accept("sound.voidbound.catalyst_implode", "Void Catalyst implodes");
    }
}
