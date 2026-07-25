package dev.neddslayer.voidbound.network;

import dev.neddslayer.voidbound.Voidbound;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.joml.Vector3f;

public record BeginVoidVFXPacket(Vector3f position, int index) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BeginVoidVFXPacket> TYPE = new CustomPacketPayload.Type<>(Voidbound.path("begin_void"));

    public static final StreamCodec<ByteBuf, BeginVoidVFXPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            BeginVoidVFXPacket::position,
            ByteBufCodecs.INT,
            BeginVoidVFXPacket::index,
            BeginVoidVFXPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
