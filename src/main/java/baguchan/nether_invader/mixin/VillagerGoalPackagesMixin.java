package baguchan.nether_invader.mixin;

import baguchan.nether_invader.entity.behavior.SetPiglinRaidStatus;
import baguchan.nether_invader.world.raid.PiglinRaid;
import baguchan.nether_invader.world.savedata.PiglinRaidData;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(VillagerGoalPackages.class)
public class VillagerGoalPackagesMixin {

    @Inject(method = "getCorePackage", cancellable = true, at = @At("RETURN"))
    private static void getCorePackage(Holder<VillagerProfession> profession, float speedModifier, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> cir) {
        List<Pair<Integer, ? extends BehaviorControl<? super Villager>>> villagerList = new ArrayList<>(cir.getReturnValue());
        villagerList.add(Pair.of(0, SetPiglinRaidStatus.create()));
        cir.setReturnValue(ImmutableList.copyOf(villagerList));
    }


    @Inject(method = "raidExistsAndActive", at = @At("HEAD"), cancellable = true)
    private static void raidExistsAndActive(ServerLevel level, LivingEntity body, CallbackInfoReturnable<Boolean> cir) {
        PiglinRaid currentRaid = PiglinRaidData.get(level).getPiglinRaidAt(body.blockPosition());
        cir.setReturnValue(currentRaid != null && currentRaid.isActive() && !currentRaid.isVictory() && !currentRaid.isLoss());
    }

    @Inject(method = "raidExistsAndNotVictory", at = @At("HEAD"), cancellable = true)
    private static void raidExistsAndNotVictory(ServerLevel level, LivingEntity body, CallbackInfoReturnable<Boolean> cir) {
        PiglinRaid currentRaid = PiglinRaidData.get(level).getPiglinRaidAt(body.blockPosition());
        cir.setReturnValue(currentRaid != null && currentRaid.isVictory());
    }
}
