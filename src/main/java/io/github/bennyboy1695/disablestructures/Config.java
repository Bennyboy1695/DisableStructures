package io.github.bennyboy1695.disablestructures;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber
public class Config {

    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static final Common COMMON = new Common();

    public static final class Common {

        public final ForgeConfigSpec.BooleanValue debug;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> disabledStructures;

        public Common() {
            debug = COMMON_BUILDER.comment("If enabled will output to the debug.log when a structure is disabled.").define("Enable Debug", false);
            disabledStructures = COMMON_BUILDER.comment("This list defines the structures that should be disabled from generating.").defineListAllowEmpty(List.of("Disabled Structures"),() -> List.of("minecraft:desert_pyramid"), key -> ForgeRegistries.STRUCTURE_FEATURES.containsKey(new ResourceLocation((String) key)));
        }
    }

    public static final ForgeConfigSpec COMMON_CONFIG = COMMON_BUILDER.build();
}
