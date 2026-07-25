package dev.neddslayer.voidbound.particle.provider;

import dev.neddslayer.voidbound.particle.AttractParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class AttractParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public AttractParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double v3, double v4, double v5) {
        return new AttractParticle(clientLevel, x, y, z, v3, v4, spriteSet);
    }
}
