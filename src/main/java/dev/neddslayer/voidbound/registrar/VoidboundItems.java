package dev.neddslayer.voidbound.registrar;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.item.DistilledVoidEssenceBottle;
import dev.neddslayer.voidbound.item.RawVoidEssenceBottle;
import dev.neddslayer.voidbound.item.VoidCatalystItem;
import net.minecraft.core.component.DataComponents;
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

    public static void register() {}
}
