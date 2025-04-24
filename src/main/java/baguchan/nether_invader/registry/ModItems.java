package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, NetherInvader.MODID);
    public static final RegistryObject<SpawnEggItem> CHAINED_GHAST_SPAWN_EGG = ITEMS.register("chained_ghast_spawn_egg", () -> new SpawnEggItem(ModEntitys.CHAINED_GHAST.get(), 16382457, 12369084, (new Item.Properties())));
    public static final RegistryObject<SpawnEggItem> AGRESSIVE_PIGLIN_SPAWN_EGG = ITEMS.register("agressive_piglin_spawn_egg", () -> new SpawnEggItem(ModEntitys.AGRESSIVE_PIGLIN.get(), 10051392, 16380836, (new Item.Properties())));

}
