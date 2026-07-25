package dev.neddslayer.voidbound.network;

import dev.neddslayer.voidbound.Voidbound;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record StopVoidVFXPacket(int index) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StopVoidVFXPacket> TYPE = new CustomPacketPayload.Type<>(Voidbound.path("stop_void"));

    public static final StreamCodec<ByteBuf, StopVoidVFXPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            StopVoidVFXPacket::index,
            StopVoidVFXPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
