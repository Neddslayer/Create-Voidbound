package dev.neddslayer.voidbound.datagen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.advancement.SimpleCreateTrigger;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.VoidboundDatagen;
import dev.neddslayer.voidbound.registrar.VoidboundBlocks;
import dev.neddslayer.voidbound.registrar.VoidboundFluids;
import dev.neddslayer.voidbound.registrar.VoidboundItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static dev.neddslayer.voidbound.Voidbound.MODID;

public class VoidboundAdvancementProvider extends AdvancementProvider {
    private static final List<VoidboundDatagen.VoidboundAdvancementHolder> advancementsToBuild = new ArrayList<>();
    public static final Map<String, SimpleCreateTrigger> ADVANCEMENT_TRIGGERS = new HashMap<>();

    public VoidboundAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new VeilTestAdvancementGenerator()));
    }

    /**
     * Create an advancement to be displayed in the advancement menu.
     */
    private static void advancementTriggerable(ItemLike displayItem, String name, AdvancementType type, boolean toast, boolean chat, boolean hidden, String parent) {
        Advancement.Builder builder = Advancement.Builder.advancement();
        String langBase = "advancement." + MODID + "." + name;
        builder.display(displayItem, Component.translatable(langBase + ".title"), Component.translatable(langBase + ".description"), null, type, toast, chat, hidden);
        SimpleCreateTrigger req = new SimpleCreateTrigger(name + "_builtin");
        ADVANCEMENT_TRIGGERS.put(name, req);

        builder.addCriterion("0", req.createCriterion(req.instance()));
        builder.requirements(AdvancementRequirements.allOf(List.of("0")));

        advancementsToBuild.add(new VoidboundDatagen.VoidboundAdvancementHolder(builder, parent, name));
    }

    public static void registerAdvancements() {
        advancementTriggerable(VoidboundBlocks.DRILLED_BEDROCK.asItem(), "drill_bedrock", AdvancementType.TASK, true, true, false, "root");
        advancementTriggerable(VoidboundItems.PURIFICATION_CRYSTAL, "essence_distill", AdvancementType.TASK, true, false, false, "drill_bedrock");
        advancementTriggerable(VoidboundBlocks.VOID_MOTOR.asItem(), "power_void_motor", AdvancementType.TASK, true, false, false, "essence_distill");
        advancementTriggerable(AllBlocks.ENCASED_FAN.asItem(), "damage_via_raw_essence", AdvancementType.TASK, true, false, false, "drill_bedrock");
        advancementTriggerable(VoidboundBlocks.VOID_CATALYST_ANCHOR.asItem(), "destroy_anchor", AdvancementType.TASK, true, false, false, "essence_distill");

        for (String name : ADVANCEMENT_TRIGGERS.keySet()) {
            Registry.register(BuiltInRegistries.TRIGGER_TYPES, name, ADVANCEMENT_TRIGGERS.get(name));
        }
    }

    public static void acceptLang(BiConsumer<String, String> consumer) {
        addAdvancement("root", "Create: Voidbound", "Torque via void", consumer);
        addAdvancement("drill_bedrock", "Hole in One", "Successfully drill through bedrock", consumer);
        addAdvancement("essence_distill", "Purifier", "Use a Purification Crystal to distill Raw Void Essence", consumer);
        addAdvancement("power_void_motor", "Void Harnesser", "Use Distilled Void Essence to power a Void Motor", consumer);
        addAdvancement("destroy_anchor", "Oops!", "Destroy a Void Catalyst Anchor with itself", consumer);
        addAdvancement("damage_via_raw_essence", "owie", "Use an Encased Fan to kill a mob", consumer);
    }

    private static void addAdvancement(String name, String title, String description, BiConsumer<String, String> consumer) {
        consumer.accept("advancement.voidbound." + name + ".title", title);
        consumer.accept("advancement.voidbound." + name + ".description", description);
    }

    @ParametersAreNonnullByDefault
    private static final class VeilTestAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {

            // root node
            AdvancementHolder root = Advancement.Builder.advancement().display(
                            VoidboundFluids.rawVoidEssenceBucket(),
                            Component.translatable("advancement.voidbound.root.title"),
                            Component.translatable("advancement.voidbound.root.description"),
                            Voidbound.path("textures/gui/advancements.png"),
                            AdvancementType.TASK,
                            false,
                            false,
                            false
                    )
                    .addCriterion("0", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{}))
                    .requirements(AdvancementRequirements.allOf(List.of("0")))
                    .save(saver, ResourceLocation.fromNamespaceAndPath(MODID, "root"), existingFileHelper);

            Map<String, AdvancementHolder> builtAdvancements = new HashMap<>(Map.of("root", root));
            for (VoidboundDatagen.VoidboundAdvancementHolder holder : advancementsToBuild) {
                if (holder.parent() != null) {
                    holder.builder().parent(builtAdvancements.get(holder.parent()));
                }
                builtAdvancements.put(holder.name(), holder.builder().save(saver, ResourceLocation.fromNamespaceAndPath(MODID, holder.name()), existingFileHelper));
            }


        }




    }
}
