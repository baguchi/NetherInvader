package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.item.LavaInfusedSwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NetherInvader.MODID);

    public static final DeferredItem<Item> LAVA_INFUSED_SWORD = ITEMS.registerItem("lava_infused_sword", (properties) -> new LavaInfusedSwordItem((properties.fireResistant().rarity(Rarity.UNCOMMON).sword(ToolMaterial.NETHERITE, 4, -2.6F))));

    public static final DeferredItem<SpawnEggItem> CHAINED_GHAST_SPAWN_EGG = ITEMS.registerItem("chained_ghast_spawn_egg", (properties) -> new SpawnEggItem((properties.spawnEgg(ModEntities.CHAINED_GHAST.get()))));
    public static final DeferredItem<SpawnEggItem> AGRESSIVE_PIGLIN_SPAWN_EGG = ITEMS.registerItem("agressive_piglin_spawn_egg", (properties) -> new SpawnEggItem((properties.spawnEgg(ModEntities.AGRESSIVE_PIGLIN.get()))));
    public static final DeferredItem<SpawnEggItem> BASTION_GENERAL_SPAWN_EGG = ITEMS.registerItem("bastion_general_spawn_egg", (properties) -> new SpawnEggItem((properties.spawnEgg(ModEntities.BASTION_GENERAL.get()))));

}
