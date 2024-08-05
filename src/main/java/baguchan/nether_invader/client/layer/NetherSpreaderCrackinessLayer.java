package baguchan.nether_invader.client.layer;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.model.NetherSpreaderModel;
import baguchan.nether_invader.entity.NetherSpreader;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class NetherSpreaderCrackinessLayer<T extends NetherSpreader> extends RenderLayer<T, NetherSpreaderModel<T>> {
    private static final Map<NetherSpreader.Crackiness, ResourceLocation> resourceLocations;

    public NetherSpreaderCrackinessLayer(RenderLayerParent<T, NetherSpreaderModel<T>> p_117135_) {
        super(p_117135_);
    }

    public void render(PoseStack p_117148_, MultiBufferSource p_117149_, int p_117150_, T p_117151_, float p_117152_, float p_117153_, float p_117154_, float p_117155_, float p_117156_, float p_117157_) {
        if (!p_117151_.isInvisible()) {
            NetherSpreader.Crackiness irongolem$crackiness = p_117151_.getCrackiness();
            if (irongolem$crackiness != NetherSpreader.Crackiness.NONE) {
                ResourceLocation resourcelocation = resourceLocations.get(irongolem$crackiness);
                renderColoredCutoutModel(this.getParentModel(), resourcelocation, p_117148_, p_117149_, p_117150_, p_117151_, -1);
            }
        }

    }

    static {
        resourceLocations = ImmutableMap.of(NetherSpreader.Crackiness.LOW, ResourceLocation.fromNamespaceAndPath(NetherInvader.MODID, "textures/entity/nether_spreader/nether_spreader_cracked_1.png"), NetherSpreader.Crackiness.MEDIUM, ResourceLocation.fromNamespaceAndPath(NetherInvader.MODID, "textures/entity/nether_spreader/nether_spreader_cracked_2.png"), NetherSpreader.Crackiness.HIGH, ResourceLocation.fromNamespaceAndPath(NetherInvader.MODID, "textures/entity/nether_spreader/nether_spreader_cracked_3.png"));
    }
}
