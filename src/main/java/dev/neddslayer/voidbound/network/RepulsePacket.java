package dev.neddslayer.voidbound.network;

import dev.neddslayer.voidbound.Voidbound;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public record RepulsePacket(Vector3f pos, float radius) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RepulsePacket> TYPE = new CustomPacketPayload.Type<>(Voidbound.path("repulse"));

    public static final StreamCodec<ByteBuf, RepulsePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            RepulsePacket::pos,
            ByteBufCodecs.FLOAT,
            RepulsePacket::radius,
            RepulsePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
