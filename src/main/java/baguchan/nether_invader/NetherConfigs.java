package baguchan.nether_invader;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

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
        public final ModConfigSpec.BooleanValue enable_nether_invader_feature_default;

        public Common(ModConfigSpec.Builder builder) {
            Predicate<Object> validator = o -> o instanceof String;
            enable_nether_invader_feature_default = builder
                    .comment("Make Enable Nether Reactor feature by default(If you want to set it individually, turn this off, restart, and set it again in Experimental Feature.)")
                    .define("Enable Nether Reactor feature by default"
                            , false);
        }
    }

}
