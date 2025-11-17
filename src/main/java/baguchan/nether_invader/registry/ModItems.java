package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NetherInvader.MODID);

    public static final DeferredItem<Item> LAVA_INFUSED_SWORD = ITEMS.registerItem("lava_infused_sword", (properties) -> new Item((properties.fireResistant().sword(ToolMaterial.NETHERITE, 4, -2.6F))));

    public static final DeferredItem<SpawnEggItem> CHAINED_GHAST_SPAWN_EGG = ITEMS.registerItem("chained_ghast_spawn_egg", (properties) -> new SpawnEggItem((properties.spawnEgg(ModEntitys.CHAINED_GHAST.get()))));
    public static final DeferredItem<SpawnEggItem> AGRESSIVE_PIGLIN_SPAWN_EGG = ITEMS.registerItem("agressive_piglin_spawn_egg", (properties) -> new SpawnEggItem((properties.spawnEgg(ModEntitys.AGRESSIVE_PIGLIN.get()))));
    public static final DeferredItem<SpawnEggItem> BASTION_GENERAL_SPAWN_EGG = ITEMS.registerItem("bastion_general_spawn_egg", (properties) -> new SpawnEggItem((properties.spawnEgg(ModEntitys.BASTION_GENERAL.get()))));

}
