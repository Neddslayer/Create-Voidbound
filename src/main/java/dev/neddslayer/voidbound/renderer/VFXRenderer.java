package dev.neddslayer.voidbound.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.neddslayer.voidbound.Config;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.VoidboundClient;
import dev.neddslayer.voidbound.registrar.VoidboundParticles;
import dev.neddslayer.voidbound.registrar.VoidboundSounds;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class VFXRenderer {
    private static final Map<Vec3, RepulseInstance> REPULSION_VFX = new HashMap<>();
    private static final Map<Integer, VoidInstance> VOID_VFX = new HashMap<>();

    private static final RandomSource random = RandomSource.create();

    private static final List<Vertex> sphereList = generateSphere(16, 32, 1f);
    private static final List<Integer> sphereIndicesList = generateSphereIndices(16, 32);

    public static boolean renderingHeart = false;

    private VFXRenderer() {}

    public static void tickVFX() {
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        List<Object> toRemove = new ArrayList<>();
        for (Vec3 pos : REPULSION_VFX.keySet()) {
            RepulseInstance instance = REPULSION_VFX.get(pos);
            instance.ticks++;

            float radius = instance.getRadius(0.5f);
            float finalRadius = instance.getFinalRadius();
            float power = (1f - (radius / finalRadius)) * finalRadius * 0.25f;

            Vec3 p = player.position().subtract(pos);
            if (p.length() < radius) {
                player.push(p.normalize().multiply(power, power, power));
            }
            if (instance.ticks >= 20) {
                toRemove.add(pos);
            }
        }

        for (Object pos : toRemove) {
            REPULSION_VFX.remove((Vec3)pos);
        }
        toRemove.clear();

        for (int idx : VOID_VFX.keySet()) {
            VoidInstance instance = VOID_VFX.get(idx);
            instance.ticks++;

            if (instance.ticks > 20 && instance.ticks < 60) {
                level.addParticle(VoidboundParticles.ATTRACT.get(), instance.pos.x, instance.pos.y, instance.pos.z, 6, 0.1, 0);
            }

            if (instance.ticks >= 80) {
                for (int i = 0; i < 20; i++) {
                    level.addParticle(ParticleTypes.FLASH, instance.pos.x + random.nextGaussian() * 0.025, instance.pos.y + random.nextGaussian() * 0.025, instance.pos.z + random.nextGaussian() * 0.025, random.nextGaussian() * 0.01, random.nextGaussian() * 0.01, random.nextGaussian() * 0.01);
                }
                toRemove.add(idx);
            }
        }

        for (Object pos : toRemove) {
            VOID_VFX.remove((int)pos);
        }
        toRemove.clear();
    }

    public static void renderRepulsionVFX(Camera camera, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        for (Vec3 pos : REPULSION_VFX.keySet()) {
            ms.pushPose();

            Vec3 cameraPos = camera.getPosition();
            ms.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

            RepulseInstance instance = REPULSION_VFX.get(pos);
            float radius = instance.getRadius(partialTicks);

            VertexConsumer consumer = buffer.getBuffer(VeilRenderType.get(VoidboundClient.REPULSION_SPHERE));

            ms.pushPose();
            ms.scale(radius, radius, radius);
            Vec3 t = pos.multiply(1 / radius, 1 / radius, 1 / radius);
            ms.translate(t.x, t.y, t.z);
            PoseStack.Pose pose = ms.last();

            renderSphere(pose, light, overlay, consumer, 1, 1, 1, 1 - radius / instance.getFinalRadius());
            ms.popPose();

            ms.popPose();
        }
    }

    public static void renderVoidVFX(Camera camera, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        for (VoidInstance instance : VOID_VFX.values()) {
            ms.pushPose();

            Vec3 cameraPos = camera.getPosition();
            ms.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

            float x = (instance.ticks + partialTicks) / 80f;
            float radius = (float) easeInOutQuint(x) * 6;
            float innerRadius = -24 * x *x + 24 * x;

            VeilRenderSystem.renderer().getShaderManager().getShader(VoidboundClient.VOID_SPHERE).getUniformSafe("bloom").setFloat((float) Config.BLOOM.getAsDouble());

            VertexConsumer consumer = buffer.getBuffer(VeilRenderType.get(VoidboundClient.VOID_SPHERE));

            // render outer sphere (the one that turns white)
            ms.pushPose();
            ms.scale(radius, radius, radius);
            Vec3 t = instance.pos.multiply(1 / radius, 1 / radius, 1 / radius);
            ms.translate(t.x + random.nextGaussian() * x * 0.01, t.y + random.nextGaussian() * x * 0.01, t.z + random.nextGaussian() * x * 0.01);
            PoseStack.Pose pose = ms.last();

            renderSphere(pose, light, overlay, consumer, 0.6f + x * x * 0.4f, x * x, 0.8f + x * x * 0.2f, x * 0.9f);

            ms.popPose();

            // render inner sphere (the one that turns black)
            ms.pushPose();
            ms.scale(innerRadius, innerRadius, innerRadius);
            t = instance.pos.multiply(1 / innerRadius, 1 / innerRadius, 1 / innerRadius);
            ms.translate(t.x, t.y, t.z);
            pose = ms.last();

            renderSphere(pose, light, overlay, consumer, 0.2f - x * 0.2f, 0, 0.3f - x * 0.3f, 0.5f + x * 0.2f);

            ms.popPose();

            ms.popPose();
        }
    }

    public static void addRepulsionVFX(Vec3 pos, float radius) {
        REPULSION_VFX.putIfAbsent(pos, new RepulseInstance(radius));
    }

    public static void addVoidVFX(int index, Vec3 pos) {
        if (VOID_VFX.containsKey(index)) {
            Voidbound.LOGGER.warn("Attempted to add duplicate void VFX index, discarding.");
            return;
        }

        VoidSoundInstance voidSoundInstance = new VoidSoundInstance(VoidboundSounds.CATALYST_IMPLODE.get(), SoundSource.BLOCKS, 1, 1 + (float)random.nextGaussian() * 0.005f, RandomSource.create(random.nextLong()), pos);
        Minecraft.getInstance().getSoundManager().play(voidSoundInstance);
        VOID_VFX.putIfAbsent(index, new VoidInstance(pos, voidSoundInstance));
    }

    public static void updateVoidVFX(int index, Vec3 pos) {
        VOID_VFX.computeIfPresent(index, (k, v) -> {
            v.pos = pos;
            v.sound.setPos(pos);
            return v;
        });
    }

    public static void stopVoidVFX(int index) {
        VOID_VFX.get(index).sound.stopSound();
        VOID_VFX.remove(index);
    }

    private static double easeInOutQuint(double x) {
        return x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2;
    }

    public static void renderSphere(PoseStack.Pose pose, int light, int overlay, VertexConsumer consumer, float r, float g, float b, float a) {
        for (Integer i : sphereIndicesList) {
            Vertex vertex = sphereList.get(i);

            consumer.addVertex(pose, vertex.position);
            consumer.setColor(r, g, b, a);
            consumer.setUv(0, 0);
            consumer.setLight(light);
            consumer.setOverlay(overlay);
            consumer.setNormal(pose, vertex.normal.x, vertex.normal.y, vertex.normal.z);
        }
    }

    private static List<Vertex> generateSphere(int stacks, int slices, float radius) {
        List<Vertex> vertices = new ArrayList<>();

        float x, y, z, xy;                              // vertex position
        float nx, ny, nz, lengthInv = 1.0f / radius;    // vertex normal

        float sectorStep = (float) (2 * PI
                / slices);
        float stackStep = (float) (PI / stacks);
        float sectorAngle, stackAngle;

        for(int i = 0; i <= stacks; ++i)
        {
            stackAngle = (float) (PI / 2 - i * stackStep);        // starting from pi/2 to -pi/2
            xy = (float) (radius * cos(stackAngle));             // r * cos(u)
            z = (float) (radius * sin(stackAngle));              // r * sin(u)

            // add (sectorCount+1) vertices per stack
            // first and last vertices have same position and normal, but different tex coords
            for(int j = 0; j <= slices; ++j)
            {
                sectorAngle = j * sectorStep;           // starting from 0 to 2pi

                // vertex position (x, y, z)
                x = (float) (xy * cos(sectorAngle));             // r * cos(u) * cos(v)
                y = (float) (xy * sin(sectorAngle));             // r * cos(u) * sin(v)

                // normalized vertex normal (nx, ny, nz)
                nx = x * lengthInv;
                ny = y * lengthInv;
                nz = z * lengthInv;

                vertices.add(new Vertex(new Vector3f(x, y, z), new Vector3f(nx, ny, nz)));
            }
        }

        return vertices;
    }

    private static List<Integer> generateSphereIndices(int stacks, int slices) {
        List<Integer> indices = new ArrayList<>();

        int k1, k2;
        for(int i = 0; i < stacks; ++i)
        {
            k1 = i * (slices + 1);     // beginning of current stack
            k2 = k1 + slices + 1;      // beginning of next stack

            for(int j = 0; j < slices; ++j, ++k1, ++k2)
            {
                // 2 triangles per sector excluding first and last stacks
                // k1 => k2 => k1+1
                if(i != 0)
                {
                    indices.add(k1);
                    indices.add(k2);
                    indices.add(k1 + 1);
                }

                // k1+1 => k2 => k2+1
                if(i != (stacks-1))
                {
                    indices.add(k1 + 1);
                    indices.add(k2);
                    indices.add(k2 + 1);
                }
            }
        }

        return indices;
    }

    private static class VoidSoundInstance extends AbstractTickableSoundInstance {

        public VoidSoundInstance(SoundEvent p_235076_, SoundSource p_235077_, float volume, float pitch, RandomSource p_235078_, Vec3 pos) {
            super(p_235076_, p_235077_, p_235078_);
            this.volume = volume;
            this.pitch = pitch;
            this.x = pos.x;
            this.y = pos.y;
            this.z = pos.z;
        }

        @Override
        public void tick() {

        }

        public void stopSound() {
            this.stop();
        }

        public void setPos(Vec3 pos) {
            this.x = pos.x;
            this.y = pos.y;
            this.z = pos.z;
        }
    }

    private static class RepulseInstance {
        private final float radius;
        public int ticks = 0;

        public RepulseInstance(float radius) {
            this.radius = radius;
        }

        public float getRadius(float tickDelta) {
            return (float) (1 - Math.pow(1 - ((ticks + tickDelta) / 20f), 7)) * this.radius;
        }

        public float getFinalRadius() {
            return this.radius;
        }
    }

    private static class VoidInstance {
        public Vec3 pos;
        public final VoidSoundInstance sound;
        public int ticks = 0;

        public VoidInstance(Vec3 pos, VoidSoundInstance sound) {
            this.pos = pos;
            this.sound = sound;
        }
    }

    private static class Vertex {
        public Vector3f position;
        public Vector3f normal;
        public Vertex(Vector3f position, Vector3f normal) {
            this.position = position;
            this.normal = normal;
        }
    };
}
