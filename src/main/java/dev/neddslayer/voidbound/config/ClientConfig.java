package dev.neddslayer.voidbound.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;


public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue BLOOM = BUILDER
            .comment("Bloom intensity for VFX")
            .gameRestart()
            .defineInRange("bloom", 2.0, 1.0, 10.0);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void acceptLang(BiConsumer<String, String> consumer) {
        consumer.accept("voidbound.configuration.bloom", "Bloom");
    }
}
