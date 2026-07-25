package dev.neddslayer.voidbound;

import dev.neddslayer.voidbound.ponder.VoidboundPonderPlugin;
import dev.neddslayer.voidbound.registrar.VoidboundPartialModels;
import dev.neddslayer.voidbound.registrar.VoidboundParticles;
import dev.neddslayer.voidbound.renderer.VFXRenderer;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.framebuffer.AdvancedFbo;
import foundry.veil.api.client.render.framebuffer.FramebufferManager;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static org.lwjgl.opengl.GL11C.*;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Voidbound.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Voidbound.MODID, value = Dist.CLIENT)
public class VoidboundClient {
    public static final ResourceLocation VOID_FLUID_DEPTH_FBO = Voidbound.path("post_entity");
    public static final ResourceLocation PURIFICATION_CRYSTAL_FBO = Voidbound.path("post_level");

    public static final ResourceLocation RAW_VOID_ESSENCE = Voidbound.path("raw_void_essence");
    public static final ResourceLocation DISTILLED_VOID_ESSENCE = Voidbound.path("distilled_void_essence");
    public static final ResourceLocation PURIFICATION_CRYSTAL = Voidbound.path("purification_crystal");
    public static final ResourceLocation DISTILLED_VOID_ESSENCE_BUCKET = Voidbound.path("distilled_void_essence_bucket");
    public static final ResourceLocation RAW_VOID_ESSENCE_BUCKET = Voidbound.path("raw_void_essence_bucket");

    public static final ResourceLocation REPULSION_SPHERE = Voidbound.path("repulsion_sphere");
    public static final ResourceLocation VOID_SPHERE = Voidbound.path("void_sphere");

    public VoidboundClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        VoidboundPartialModels.register();
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new VoidboundPonderPlugin());


    }

    @SubscribeEvent
    static void renderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            FramebufferManager framebufferManager = VeilRenderSystem.renderer().getFramebufferManager();
            AdvancedFbo fbo = framebufferManager.getFramebuffer(VOID_FLUID_DEPTH_FBO);
            AdvancedFbo main = AdvancedFbo.getMainFramebuffer();
            main.resolveToAdvancedFbo(fbo, GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT, GL_NEAREST);
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            FramebufferManager framebufferManager = VeilRenderSystem.renderer().getFramebufferManager();
            AdvancedFbo fbo = framebufferManager.getFramebuffer(PURIFICATION_CRYSTAL_FBO);
            AdvancedFbo main = AdvancedFbo.getMainFramebuffer();
            main.resolveToAdvancedFbo(fbo, GL_COLOR_BUFFER_BIT, GL_NEAREST);
        }
    }

    @SubscribeEvent
    static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        VoidboundParticles.registerProviders(event);
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Pre event) {
        VFXRenderer.tickVFX();
    }
}
