package dev.neddslayer.voidbound.network;

import dev.neddslayer.voidbound.Voidbound;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.joml.Vector3f;

public record UpdateVoidVFXPositionPacket(Vector3f position, int index) implements CustomPacketPayload {
    public static final Type<UpdateVoidVFXPositionPacket> TYPE = new Type<>(Voidbound.path("update_void"));

    public static final StreamCodec<ByteBuf, UpdateVoidVFXPositionPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            UpdateVoidVFXPositionPacket::position,
            ByteBufCodecs.INT,
            UpdateVoidVFXPositionPacket::index,
            UpdateVoidVFXPositionPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
