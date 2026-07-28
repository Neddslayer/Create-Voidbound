package dev.neddslayer.voidbound.registrar;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.item.AstralProjectionMobEffect;
import dev.neddslayer.voidbound.item.DistilledVoidEssenceBottle;
import dev.neddslayer.voidbound.item.RawVoidEssenceBottle;
import dev.neddslayer.voidbound.item.VoidCatalystItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static dev.neddslayer.voidbound.Voidbound.CREATIVE_TAB;

public class VoidboundItems {
    private static final CreateRegistrate REGISTRATE = Voidbound.registrate();

    static {
        REGISTRATE.setCreativeTab(CREATIVE_TAB);
    }

    public static final ItemEntry<Item> VOID_GEM = REGISTRATE.item("void_gem", Item::new)
            .lang("Void Gem")
            .properties(p -> p.rarity(Rarity.UNCOMMON).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .register();

    public static final ItemEntry<Item> VOID_GEM_SHEET = REGISTRATE.item("void_gem_sheet", Item::new)
            .lang("Void Gem Sheet")
            .properties(p -> p.rarity(Rarity.UNCOMMON).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .register();

    public static final ItemEntry<Item> PURIFICATION_CRYSTAL = REGISTRATE.item("purification_crystal", Item::new)
            .lang("Purification Crystal")
            .register();

    public static final ItemEntry<VoidCatalystItem> VOID_CATALYST_ITEM = REGISTRATE.item("void_catalyst", VoidCatalystItem::new)
            .lang("Void Catalyst")
            .properties(p -> p.stacksTo(1).rarity(Rarity.RARE).fireResistant())
            .register();

    public static final ItemEntry<RawVoidEssenceBottle> RAW_VOID_ESSENCE_BOTTLE = REGISTRATE.item("raw_void_essence_bottle", RawVoidEssenceBottle::new)
            .lang("Raw Void Essence Bottle")
            .properties(p -> p.stacksTo(16))
            .register();

    public static final ItemEntry<DistilledVoidEssenceBottle> DISTILLED_VOID_ESSENCE_BOTTLE = REGISTRATE.item("distilled_void_essence_bottle", DistilledVoidEssenceBottle::new)
            .lang("Distilled Void Essence Bottle")
            .properties(p -> p.stacksTo(16))
            .register();

    public static final ItemEntry<Item> INFUSED_GOLD = REGISTRATE.item("infused_gold", Item::new)
            .lang("Void-Infused Crushed Gold")
            .properties(p -> p.stacksTo(16).food(new FoodProperties.Builder().alwaysEdible().effect(() -> new MobEffectInstance(VoidboundItems.ASTRAL_PROJECTION.getDelegate(), 15 * 20), 1).build()))
            .register();

    public static final RegistryEntry<MobEffect, AstralProjectionMobEffect> ASTRAL_PROJECTION = REGISTRATE.generic("astral_projection", Registries.MOB_EFFECT, AstralProjectionMobEffect::new).register();

    public static void register() {}
}
