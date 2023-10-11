package baguchan.nether_invader;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = NetherInvader.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetherConfigs {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> nether_reactor_spawn_whitelist;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> nether_reactor_spawn_rare_whitelist;

        public Common(ForgeConfigSpec.Builder builder) {
            Predicate<Object> validator = o -> o instanceof String;
            nether_reactor_spawn_whitelist = builder
                    .comment("Add what Nether reactor spawn the mobs [example: minecraft:chicken]")
                    .defineList("Nether Reactor Spawn Whitelist"
                            , Lists.newArrayList("minecraft:piglin"
                                    , "minecraft:zombified_piglin")
                            , validator);
            nether_reactor_spawn_rare_whitelist = builder
                    .comment("Add what Nether reactor spawn the mobs but rare [example: minecraft:chicken]")
                    .defineList("Nether Reactor Spawn Rare Whitelist"
                            , Lists.newArrayList("minecraft:piglin_brute")
                            , validator);
        }
    }

}
