package dev.neddslayer.voidbound.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class PurifyParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public PurifyParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spriteSet = spriteSet;
        this.gravity = 0.01f;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.lifetime = 20 + random.nextIntBetweenInclusive(-18, 30);
        this.friction = 0.9f;
        this.scale(2);

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(spriteSet);

        double originalYd = this.yd;
        this.yd = Mth.lerp(0.1, this.yd, 0) + 0.005f * random.nextFloat();
        double ydDiff = originalYd - yd;
        this.xd += ydDiff * (0.1 * random.nextDouble() - 0.05);
        this.zd += ydDiff * (0.1 * random.nextDouble() - 0.05);

        super.tick();
    }

    @Override
    protected int getLightColor(float partialTick) {
        float f = ((float)this.age + partialTick) / (float)this.lifetime;
        f = Mth.clamp(1.0f - f, 0.0F, 1.0F);
        int i = super.getLightColor(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
