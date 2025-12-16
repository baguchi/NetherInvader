package baguchan.nether_invader.item;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class LavaInfusedSwordItem extends Item {
    public LavaInfusedSwordItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void postHurtEnemy(ItemStack p_346136_, LivingEntity p_346250_, LivingEntity p_346014_) {
        super.postHurtEnemy(p_346136_, p_346250_, p_346014_);
        p_346250_.setRemainingFireTicks(120);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment) && !enchantment.is(Enchantments.FIRE_ASPECT);
    }
}
