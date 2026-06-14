package baguchan.nether_invader.client;

import baguchan.nether_invader.NetherInvader;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModModelLayers {
    public static final ModelLayerLocation BASTION_GENERAL = new ModelLayerLocation(Identifier.fromNamespaceAndPath(NetherInvader.MODID, "bastion_general"), "main");
    public static final ModelLayerLocation PIGLIN_WARRIOR = new ModelLayerLocation(Identifier.fromNamespaceAndPath(NetherInvader.MODID, "piglin_warrior"), "main");
    public static final ModelLayerLocation PIGLIN_SPEARER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(NetherInvader.MODID, "piglin_spearer"), "main");

}
