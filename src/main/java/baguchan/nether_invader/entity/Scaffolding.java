package baguchan.nether_invader.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Scaffolding extends LivingEntity implements Chainable {


    public ChainData chainData;

    public Scaffolding(EntityType<? extends Scaffolding> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide && this instanceof Chainable && this.tickCount > 2) {
            this.tickChain((Entity & Chainable) this);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    protected int decreaseAirSupply(int p_28882_) {
        return p_28882_;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }


    @Override
    public void closeRangeChainBehaviour(Entity entity) {

    }

    @Override
    public boolean handleChainAtDistance(Entity entity, float distance) {
        return true;
    }

    @Override
    public ChainData getChainData() {
        return this.chainData;
    }

    @Override
    public void setChainData(ChainData chainData) {
        this.chainData = chainData;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21145_) {
        super.addAdditionalSaveData(p_21145_);
        this.writeChainData(p_21145_, this.chainData);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21096_) {
        super.readAdditionalSaveData(p_21096_);
        this.readChainData(p_21096_);
    }
}
