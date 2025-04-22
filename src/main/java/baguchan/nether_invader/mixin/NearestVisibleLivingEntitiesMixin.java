package baguchan.nether_invader.mixin;

import bagu_chan.bagus_lib.util.BrainUtils;
import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(NearestVisibleLivingEntities.class)
public class NearestVisibleLivingEntitiesMixin {

    @Shadow
    @Final
    private Predicate<LivingEntity> lineOfSightTest;

    @Shadow
    @Final
    private List<LivingEntity> nearbyEntities;
    private Predicate<LivingEntity> lineOfSightTestRevamp;

    @Inject(method = "<init>(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/List;)V", at = @At("TAIL"))
    private void isWithinAttackRange(LivingEntity p_186104_, List p_186105_, CallbackInfo ci) {

        Object2BooleanOpenHashMap<LivingEntity> object2booleanopenhashmap = new Object2BooleanOpenHashMap<>(p_186105_.size());
        Predicate<LivingEntity> predicate = p_186111_ -> BrainUtils.isEntityTargetable(p_186104_, p_186111_, (int) p_186104_.getAttributeValue(Attributes.FOLLOW_RANGE));
        this.lineOfSightTestRevamp = p_186115_ -> object2booleanopenhashmap.computeIfAbsent(p_186115_, predicate);

    }

    @Inject(method = "findClosest", at = @At("HEAD"), cancellable = true)
    public void findClosest(Predicate<LivingEntity> p_186117_, CallbackInfoReturnable<Optional<LivingEntity>> cir) {
        for (LivingEntity livingentity : this.nearbyEntities) {
            if (p_186117_.test(livingentity) && this.lineOfSightTestRevamp.test(livingentity)) {
                cir.setReturnValue(Optional.of(livingentity));
            }
        }

        cir.setReturnValue(Optional.empty());
    }

    @Inject(method = "findAll", at = @At("HEAD"), cancellable = true)
    public void findAll(Predicate<LivingEntity> p_186124_, CallbackInfoReturnable<Iterable<LivingEntity>> cir) {
        cir.setReturnValue(Iterables.filter(this.nearbyEntities, p_186127_ -> p_186124_.test(p_186127_) && this.lineOfSightTestRevamp.test(p_186127_)));
    }

    @Inject(method = "find", at = @At("HEAD"), cancellable = true)
    public void find(Predicate<LivingEntity> p_186129_, CallbackInfoReturnable<Stream<LivingEntity>> cir) {
        cir.setReturnValue(this.nearbyEntities.stream().filter(p_186120_ -> p_186129_.test(p_186120_) && this.lineOfSightTestRevamp.test(p_186120_)));
    }

    @Inject(method = "contains(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    public void contains(LivingEntity p_186108_, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.nearbyEntities.contains(p_186108_) && this.lineOfSightTestRevamp.test(p_186108_));
    }

    @Inject(method = "contains(Ljava/util/function/Predicate;)Z", at = @At("HEAD"), cancellable = true)
    public void contains(Predicate<LivingEntity> p_186131_, CallbackInfoReturnable<Boolean> cir) {
        for (LivingEntity livingentity : this.nearbyEntities) {
            if (p_186131_.test(livingentity) && this.lineOfSightTestRevamp.test(livingentity)) {
                cir.setReturnValue(true);
            }
        }

        cir.setReturnValue(false);
    }
}