package dev.neddslayer.voidbound.particle.provider;

import dev.neddslayer.voidbound.particle.PurifyParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class PurifyParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public PurifyParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new PurifyParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
    }
}
