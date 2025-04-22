package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NetherInvader.MODID);
    public static final DeferredItem<DeferredSpawnEggItem> CHAINED_GHAST_SPAWN_EGG = ITEMS.registerItem("chained_ghast_spawn_egg", (properties) -> new DeferredSpawnEggItem(ModEntitys.CHAINED_GHAST, 16382457, 12369084, (properties)));
    public static final DeferredItem<DeferredSpawnEggItem> AGRESSIVE_PIGLIN_SPAWN_EGG = ITEMS.registerItem("agressive_piglin_spawn_egg", (properties) -> new DeferredSpawnEggItem(ModEntitys.AGRESSIVE_PIGLIN, 10051392, 16380836, (properties)));

}
