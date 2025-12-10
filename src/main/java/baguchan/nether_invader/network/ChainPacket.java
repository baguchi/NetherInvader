package baguchan.nether_invader.network;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.entity.Chainable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import javax.annotation.Nullable;

public class ChainPacket implements CustomPacketPayload, IPayloadHandler<ChainPacket> {

    public static final StreamCodec<FriendlyByteBuf, ChainPacket> STREAM_CODEC = CustomPacketPayload.codec(
            ChainPacket::write, ChainPacket::new
    );
    public static final CustomPacketPayload.Type<ChainPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NetherInvader.MODID, "chain"));


    private final int sourceId;
    private final int destId;

    public ChainPacket(Entity p_133164_, @Nullable Entity p_133165_) {
        this.sourceId = p_133164_.getId();
        this.destId = p_133165_ != null ? p_133165_.getId() : 0;
    }

    public ChainPacket(int sourceId, int destId) {
        this.sourceId = sourceId;
        this.destId = destId;
    }

    public ChainPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.sourceId);
        buf.writeInt(this.destId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ChainPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                Entity entity = Minecraft.getInstance().player.level().getEntity(message.sourceId);
                if (entity instanceof Chainable chainable) {
                    chainable.setDelayedChainHolderId(message.destId);
                }
            }
        });
    }
}