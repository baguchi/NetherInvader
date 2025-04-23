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
        public final ModConfigSpec.ConfigValue<List<? extends String>> ENABLE_DIMENSIONS;
        public Common(ModConfigSpec.Builder builder) {
            Predicate<Object> validator = o -> o instanceof String;
            ENABLE_DIMENSIONS = builder
                    .comment("Enable the PiglinRaid in dimension. Use the full name(, eg: minecraft:the_nether.")
                    .define("EnablePiglinRaidDimensions", Lists.newArrayList("minecraft:overworld"));
        }
    }

}
