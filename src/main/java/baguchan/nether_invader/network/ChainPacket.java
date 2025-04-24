package baguchan.nether_invader.network;

import baguchan.nether_invader.entity.Chainable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ChainPacket {
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

    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(this.sourceId);
        buffer.writeInt(this.destId);
    }

    public static ChainPacket deserialize(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        int destId = buffer.readInt();
        return new ChainPacket(entityId, destId);
    }

    public static boolean handle(ChainPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = (Minecraft.getInstance()).player.level().getEntity(message.sourceId);
                if (entity instanceof Chainable chainable)
                    chainable.setDelayedLeashHolderId(message.destId);
            });
        }
        return true;
    }
}