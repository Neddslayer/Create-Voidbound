package dev.neddslayer.voidbound.registrar;

import dev.neddslayer.voidbound.particle.provider.AttractParticleProvider;
import dev.neddslayer.voidbound.particle.provider.PurifyParticleProvider;
import dev.neddslayer.voidbound.particle.provider.SparkConnectedProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.neddslayer.voidbound.Voidbound.MODID;

public class VoidboundParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PURIFY = PARTICLE_TYPES.register(
            "purify",
            () -> new SimpleParticleType(false)
    );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ATTRACT = PARTICLE_TYPES.register(
            "attract",
            () -> new SimpleParticleType(false)
    );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARK_CONNECTED = PARTICLE_TYPES.register(
            "spark_connected",
            () -> new SimpleParticleType(false)
    );

    public static void registerTypes(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }

    public static void registerProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(PURIFY.get(), PurifyParticleProvider::new);
        event.registerSpriteSet(ATTRACT.get(), AttractParticleProvider::new);
        event.registerSpriteSet(SPARK_CONNECTED.get(), SparkConnectedProvider::new);
    }
}