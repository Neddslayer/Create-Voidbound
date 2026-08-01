package dev.neddslayer.voidbound.network;

import dev.neddslayer.voidbound.Voidbound;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public record SpawnQuasarParticlePacket(Vector3f position, ResourceLocation location) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SpawnQuasarParticlePacket> TYPE = new CustomPacketPayload.Type<>(Voidbound.path("spawn_quasar_particle"));

    public static final StreamCodec<ByteBuf, SpawnQuasarParticlePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            SpawnQuasarParticlePacket::position,
            ResourceLocation.STREAM_CODEC,
            SpawnQuasarParticlePacket::location,
            SpawnQuasarParticlePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
