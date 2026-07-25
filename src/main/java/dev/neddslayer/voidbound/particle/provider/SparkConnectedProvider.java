package dev.neddslayer.voidbound.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SparkConnectedProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprite;

    public SparkConnectedProvider(SpriteSet sprites) {
        this.sprite = sprites;
    }

    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        GlowParticle glowparticle = new GlowParticle(level, x, y, z, 0.0, 0.0, 0.0, this.sprite);
        glowparticle.setColor(1.0F, 0.9F, 1.0F);
        glowparticle.setParticleSpeed(xSpeed * (double)0.25F, ySpeed * (double)0.25F, zSpeed * (double)0.25F);
        glowparticle.setLifetime(level.random.nextInt(2) + 2);
        return glowparticle;
    }
}
