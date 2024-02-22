package baguchan.nether_invader;

import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;

public class NetherConfigs {
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ModConfigSpec.ConfigValue<List<? extends String>> nether_reactor_spawn_whitelist;
        public final ModConfigSpec.ConfigValue<List<? extends String>> nether_reactor_spawn_rare_whitelist;
        public final ModConfigSpec.BooleanValue enable_nether_invader_feature_default;
        public final ModConfigSpec.IntValue nether_reactor_deactive_time;
        public final ModConfigSpec.BooleanValue nether_spawn_in_overworld;


        public Common(ModConfigSpec.Builder builder) {
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
            enable_nether_invader_feature_default = builder
                    .comment("Make Enable Nether Reactor feature by default(If you want to set it individually, turn this off, restart, and set it again in Experimental Feature.)")
                    .define("Enable Nether Reactor feature by default"
                            , false);
            nether_reactor_deactive_time = builder
                    .comment("Change How long does Nether Reactor take to be deactivated")
                    .defineInRange("Nether Reactor Deactive Time"
                            , 3600, 0, 36000);
            nether_spawn_in_overworld = builder
                    .comment("Nether Now Spawn in overworld but Not a complete rewrite to the Nether")
                    .define("Nether Spawn In Overworld"
                            , true);
        }
    }

}
