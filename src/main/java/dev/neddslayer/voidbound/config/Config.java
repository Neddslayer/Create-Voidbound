package dev.neddslayer.voidbound.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;


public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue BEDROCK_DRILL_SPEED = BUILDER
            .comment("How fast it takes for a Brass Drill to break bedrock.")
            .worldRestart()
            .defineInRange("bedrock_drill_speed", 0.2, 0.01, 2.0);

    public static final ModConfigSpec.DoubleValue EXTRACT_RATE = BUILDER
            .comment("How fast raw void essence extracts from the void")
            .worldRestart()
            .defineInRange("extract_rate", 0.25, 0.01, 2.0);

    public static final ModConfigSpec.IntValue PURIFY_SPEED = BUILDER
            .comment("How fast purification spreads")
            .worldRestart()
            .defineInRange("purify_speed", 5, 1, 25);

    public static final ModConfigSpec.DoubleValue ATTRACTION_RADIUS = BUILDER
            .comment("The range of Attraction Coils (value * signal strength)")
            .worldRestart()
            .defineInRange("attraction_radius", 0.5, 0, 2.0);

    public static final ModConfigSpec.DoubleValue REPULSION_RADIUS = BUILDER
            .comment("The range of Repulsion Coils (value * signal strength)")
            .worldRestart()
            .defineInRange("repulsion_radius", 0.5, 0, 2.0);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void acceptLang(BiConsumer<String, String> consumer) {
        consumer.accept("voidbound.configuration.bedrock_drill_speed", "Bedrock Drill Speed");
        consumer.accept("voidbound.configuration.extract_rate", "Extract Rate");
        consumer.accept("voidbound.configuration.purify_speed", "Purification Speed");
        consumer.accept("voidbound.configuration.attraction_radius", "Attraction Radius");
        consumer.accept("voidbound.configuration.repulsion_radius", "Repulsion Radius");
    }
}
