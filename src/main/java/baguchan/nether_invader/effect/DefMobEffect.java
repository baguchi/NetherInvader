package baguchan.nether_invader.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DefMobEffect extends MobEffect {
    public DefMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    public DefMobEffect(MobEffectCategory mobEffectCategory, int i, ParticleOptions particleOptions) {
        super(mobEffectCategory, i, particleOptions);
    }
}
