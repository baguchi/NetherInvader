package baguchan.nether_invader.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

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
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel && this.tickCount > 2) {
            Chainable.tickChain(serverLevel, this);
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
    public boolean supportQuadChain() {
        return true;
    }

    @Override
    public Vec3[] getQuadChainOffsets() {
        return Leashable.createQuadLeashOffsets(this, 0.0, 0.64, 0.382, 0.88);
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
    public void addAdditionalSaveData(ValueOutput p_21145_) {
        super.addAdditionalSaveData(p_21145_);
        this.writeChainData(p_21145_, this.chainData);
    }

    @Override
    public void readAdditionalSaveData(ValueInput p_21096_) {
        super.readAdditionalSaveData(p_21096_);
        this.readChainData(p_21096_);
    }
}
