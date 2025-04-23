package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NetherInvader.MODID);
    public static final DeferredItem<SpawnEggItem> CHAINED_GHAST_SPAWN_EGG = ITEMS.registerItem("chained_ghast_spawn_egg", (properties) -> new SpawnEggItem(ModEntitys.CHAINED_GHAST.get(), (properties)));
    public static final DeferredItem<SpawnEggItem> AGRESSIVE_PIGLIN_SPAWN_EGG = ITEMS.registerItem("agressive_piglin_spawn_egg", (properties) -> new SpawnEggItem(ModEntitys.AGRESSIVE_PIGLIN.get(), (properties)));

}
