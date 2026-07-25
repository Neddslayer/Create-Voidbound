package dev.neddslayer.voidbound.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static net.minecraft.util.Mth.PI;
import static net.minecraft.util.Mth.sqrt;
import static org.joml.Math.acos;

public class AttractParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    private final double originX, originY, originZ;

    private double speed;

    public AttractParticle(ClientLevel level, double x, double y, double z, double radius, double speed, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.originX = x;
        this.originY = y;
        this.originZ = z;

        this.speed = speed;

        this.x = x + random.nextGaussian() * radius;
        this.y = y + random.nextGaussian() * radius;
        this.z = z + random.nextGaussian() * radius;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.setPos(this.x, this.y, this.z);

        this.lifetime = Mth.ceil(30 / speed);
        this.hasPhysics = false;

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(spriteSet);

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime || Math.abs(this.x - this.originX) < 0.1 || this.speed < 0.01) {
            this.remove();
        } else {
            this.x = Mth.lerp(this.speed, this.x, this.originX);
            this.y = Mth.lerp(this.speed, this.y, this.originY);
            this.z = Mth.lerp(this.speed, this.z, this.originZ);
            this.speed += 0.01f;
            this.setPos(this.x, this.y, this.z);
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {

        Quaternionf quaternionf = angleTo(new Vector3f((float) originX, (float) originY, (float) originZ), new Vector3f((float) x, (float) y, (float) z));

        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
        this.renderRotatedQuad(buffer, renderInfo, quaternionf.rotateY(PI), partialTicks); // make sure it renders on the back too
    }

    protected Quaternionf angleTo(Vector3f target, Vector3f position) {
        ///Derived from pseudocode found here:
        ///https://stackoverflow.com/questions/13014973/quaternion-rotate-to

        //Get the normalized vector from the camera position to Target
        Vector3f VectorTo = new Vector3f(target.x - position.x,
                target.y - position.y,
                target.z - position.z).normalize();

        //Straight-ahead vector
        Vector3f LocalVector = new Vector3f(0, 0, -1);

        //Get the cross product as the axis of rotation
        Vector3f Axis = new Vector3f(VectorTo.y*LocalVector.z - VectorTo.z*LocalVector.y,
                VectorTo.z*LocalVector.x - VectorTo.x*LocalVector.z,
                VectorTo.x*LocalVector.y - VectorTo.y*LocalVector.x).normalize();

        //Get the dot product to find the angle
        float Angle = acos(VectorTo.x*LocalVector.x +
                VectorTo.y*LocalVector.y +
                VectorTo.z*LocalVector.z);

        //Determine whether the angle is positive
        //Get the cross product of the axis and the local vector
        Vector3f ThirdVect = new Vector3f(Axis.y*LocalVector.z - Axis.z*LocalVector.y,
                Axis.z*LocalVector.x - Axis.x*LocalVector.z,
                Axis.x*LocalVector.y - Axis.y*LocalVector.x);
        //If the dot product of that and the local vector is negative, so is the angle
        if (ThirdVect.x*VectorTo.x + ThirdVect.y*VectorTo.y + ThirdVect.z*VectorTo.z < 0)
        {
            Angle = -Angle;
        }

        //Finally, create a quaternion
        Quaternionf AxisAngle = new Quaternionf();
        AxisAngle.fromAxisAngleRad(Axis.x, Axis.y, Axis.z, Angle);

        return AxisAngle;
    }

    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float f = this.getQuadSize(partialTicks);
        float f1 = this.getU0();
        float f2 = this.getU1();
        float f3 = this.getV0();
        float f4 = this.getV1();
        int i = this.getLightColor(partialTicks);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, f1, f4, i);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float zOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(0, yOffset, -zOffset)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(packedLight);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
