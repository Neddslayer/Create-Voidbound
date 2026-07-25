package dev.neddslayer.voidbound.registrar;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.EntityEntry;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.entity.DistilledVoidEssenceBottleProjectile;
import dev.neddslayer.voidbound.entity.RawVoidEssenceBottleProjectile;
import dev.neddslayer.voidbound.entity.VoidCatalystEntity;
import dev.neddslayer.voidbound.item.DistilledVoidEssenceBottle;
import dev.neddslayer.voidbound.item.RawVoidEssenceBottle;
import dev.neddslayer.voidbound.renderer.entity.VoidCatalystRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class VoidboundEntityTypes {
    private static final CreateRegistrate REGISTRATE = Voidbound.registrate();

    public static final EntityEntry<RawVoidEssenceBottleProjectile> RAW_VOID_ESSENCE_BOTTLE = REGISTRATE
            .<RawVoidEssenceBottleProjectile>entity("raw_void_essence_bottle", RawVoidEssenceBottleProjectile::new, MobCategory.MISC)
            .properties(p -> p.sized(0.4f, 0.4f))
            .renderer(() -> ThrownItemRenderer::new)
            .register();

    public static final EntityEntry<DistilledVoidEssenceBottleProjectile> DISTILLED_VOID_ESSENCE_BOTTLE = REGISTRATE.
            <DistilledVoidEssenceBottleProjectile>entity("distilled_void_essence_bottle", DistilledVoidEssenceBottleProjectile::new, MobCategory.MISC)
            .properties(p -> p.sized(0.4f, 0.4f))
            .renderer(() -> ThrownItemRenderer::new)
            .register();

    public static final EntityEntry<VoidCatalystEntity> VOID_CATALYST_ENTITY = REGISTRATE
            .<VoidCatalystEntity>entity("void_catalyst", VoidCatalystEntity::new, MobCategory.MISC)
            .properties(p -> p.sized(0.4f, 0.4f).clientTrackingRange(4).updateInterval(10))
            .renderer(() -> VoidCatalystRenderer::new)
            .register();

    public static void register() {}
}
