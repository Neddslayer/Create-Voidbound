package dev.neddslayer.voidbound;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.neddslayer.voidbound.blockentity.VoidCatalystAnchorBlockEntity;
import dev.neddslayer.voidbound.blockentity.VoidMotorBlockEntity;
import dev.neddslayer.voidbound.item.AstralProjectionMobEffect;
import dev.neddslayer.voidbound.network.*;
import dev.neddslayer.voidbound.registrar.*;
import dev.neddslayer.voidbound.renderer.VFXRenderer;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.joml.Vector3d;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

import static net.createmod.catnip.lang.FontHelper.styleFromColor;

@Mod(Voidbound.MODID)
public class Voidbound {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "voidbound";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<DamageType> RAW_VOID_ESSENCE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "raw_void_essence"));
    public static DamageSource damageSource(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }

    public static final FontHelper.Palette VOIDBOUND_CREATE = new FontHelper.Palette(styleFromColor(0xC9974C), styleFromColor(0xD8B3ED));

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, VOIDBOUND_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item))));

    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.voidbound"))
                    .icon(() -> new ItemStack(VoidboundFluids.rawVoidEssenceBucket()))
                    .displayItems((itemDisplayParameters, output) -> Voidbound.registrate().getAll(Registries.ITEM).forEach(entry -> {
                        if (!CreateRegistrate.isInCreativeTab(entry, Voidbound.CREATIVE_TAB)) return;
                        Item item = entry.get();
                        output.accept(item);
                    }))
                    .build());

    public Voidbound(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        CREATIVE_TABS.register(modEventBus);
        REGISTRATE.setCreativeTab(CREATIVE_TAB);

        VoidboundFluids.register();
        VoidboundBlocks.register();
        VoidboundBlockEntityTypes.register();
        VoidboundParticles.registerTypes(modEventBus);
        VoidboundEntityTypes.register();
        VoidboundItems.register();
        VoidboundSounds.register(modEventBus);

        modEventBus.addListener(EventPriority.HIGH, VoidboundDatagen::gatherDataHighPriority);
        modEventBus.addListener(this::onRegisterCapabilities);
        modEventBus.addListener(this::onRegisterPayloads);

        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        VoidMotorBlockEntity.registerCapabilities(event);
        VoidCatalystAnchorBlockEntity.registerCapabilities(event);
    }

    public void onRegisterPayloads(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                RepulsePacket.TYPE,
                RepulsePacket.STREAM_CODEC,
                (repulsePacket, iPayloadContext) -> VFXRenderer.addRepulsionVFX(new Vec3(repulsePacket.pos()), repulsePacket.radius())
        );

        registrar.playToClient(
                PushPlayerPacket.TYPE,
                PushPlayerPacket.STREAM_CODEC,
                (pushPlayerPacket, iPayloadContext) -> iPayloadContext.player().push(pushPlayerPacket.amount().x, pushPlayerPacket.amount().y, pushPlayerPacket.amount().z)
        );

        registrar.playToClient(
                BeginVoidVFXPacket.TYPE,
                BeginVoidVFXPacket.STREAM_CODEC,
                (beginVoidVFXPacket, iPayloadContext) -> VFXRenderer.addVoidVFX(beginVoidVFXPacket.index(), new Vec3(beginVoidVFXPacket.position()))
        );
        registrar.playToClient(
                UpdateVoidVFXPositionPacket.TYPE,
                UpdateVoidVFXPositionPacket.STREAM_CODEC,
                (VFXPacket, iPayloadContext) -> VFXRenderer.updateVoidVFX(VFXPacket.index(), new Vec3(VFXPacket.position()))
        );
        registrar.playToClient(
                StopVoidVFXPacket.TYPE,
                StopVoidVFXPacket.STREAM_CODEC,
                (VFXPacket, iPayloadContext) -> VFXRenderer.stopVoidVFX(VFXPacket.index())
        );

        registrar.playToClient(
                SpawnQuasarParticlePacket.TYPE,
                SpawnQuasarParticlePacket.STREAM_CODEC,
                (spawnQuasarParticlePacket, iPayloadContext) -> RenderSystem.recordRenderCall(() -> {
                    ParticleEmitter emitter = VeilRenderSystem.renderer().getParticleManager().createEmitter(spawnQuasarParticlePacket.location());
                    if (emitter == null) return;
                    emitter.setPosition(spawnQuasarParticlePacket.position().get(new Vector3d()));
                    VeilRenderSystem.renderer().getParticleManager().addParticleSystem(emitter);
                })
        );
    }

    @SubscribeEvent // forge event bus
    public void onEntityDamage(LivingIncomingDamageEvent event) {
        if (event.getSource().is(Voidbound.RAW_VOID_ESSENCE_DAMAGE)) {
            event.getEntity().lastHurtByPlayerTime = event.getEntity().tickCount;
        }

        if (event.getEntity().hasEffect(VoidboundItems.ASTRAL_PROJECTION.getDelegate()) && event.getEntity().getHealth() <= event.getAmount()) {
            event.getEntity().removeEffect(VoidboundItems.ASTRAL_PROJECTION.getDelegate());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRemoveEffect(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        if (event.getEffect().is(VoidboundItems.ASTRAL_PROJECTION.getKey())) {
            AstralProjectionMobEffect.resetEntity(entity);
        }
    }

    @SubscribeEvent
    public void onRemoveEffect(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
        if (event.getEffectInstance() == null) return;
        if (event.getEffectInstance().is(VoidboundItems.ASTRAL_PROJECTION.getDelegate())) {
            AstralProjectionMobEffect.resetEntity(entity);
        }
    }

    public static ResourceLocation path(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
    public static String string(String name) {
        return Voidbound.MODID + ":" + name;
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }


}
