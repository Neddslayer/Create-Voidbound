package dev.neddslayer.voidbound.network;

import dev.neddslayer.voidbound.Voidbound;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.joml.Vector3f;

public record PushPlayerPacket(Vector3f amount) implements CustomPacketPayload {
    public static final Type<PushPlayerPacket> TYPE = new Type<>(Voidbound.path("push_player"));

    public static final StreamCodec<ByteBuf, PushPlayerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            PushPlayerPacket::amount,
            PushPlayerPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
