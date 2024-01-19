package pokefenn.totemic;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.registries.IForgeRegistry;
import pokefenn.totemic.api.TotemicAPI;

public final class ModConfig {
    public static class Client {
        public final ConfigValue<Integer> ceremonyHudPositionX;
        public final ConfigValue<Integer> ceremonyHudPositionY;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Totemic client-only configuration settings")
                    .translation("totemic.config.client")
                    .push("client");

            ceremonyHudPositionX = builder
                    .comment("Horizontal position of the ceremony HUD (offset from center of the screen)")
                    .translation("totemic.config.ceremonyHudPositionX")
                    .define("ceremonyHudPositionX", 0);

            ceremonyHudPositionY = builder
                    .comment("Vertical position of the ceremony HUD (offset from center of the screen)")
                    .translation("totemic.config.ceremonyHudPositionY")
                    .define("ceremonyHudPositionY", -70);
        }
    }

    public static class Server {
        public final ConfigValue<List<? extends String>> disabledCeremonies;
        public final ConfigValue<List<? extends String>> disabledTotemCarvings;

        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Totemic server configuration settings. These settings are world specific and are synced from the server to clients.")
                    .translation("totemic.config.server")
                    .push("server");

            disabledCeremonies = builder
                    .comment("List of Ceremonies that should be disabled. Note that disabling some of the Ceremonies will prevent progression in Totemic.")
                    .comment("Example: [\"totemic:rain\", \"totemic:drought\"]")
                    .comment("See the Totempedia with advanced tooltips enabled (F3+H) to look up the Ceremonies' IDs.")
                    .translation("totemic.config.disabledCeremonies")
                    .defineListAllowEmpty(List.of("disabledCeremonies"), List::of, isValidRegistryKey(() -> TotemicAPI.get().registry().ceremonies()));

            disabledTotemCarvings = builder
                    .comment("List of Totem Carvings that should be disabled from being carved.")
                    .comment("Example: [\"totemic:spider\"]")
                    .comment("Use advanced tooltips (F3+H) to look up the Totem Carvings' IDs.")
                    .translation("totemic.config.disabledTotemCarvings")
                    .defineListAllowEmpty(List.of("disabledTotemCarvings"), List::of, isValidRegistryKey(() -> TotemicAPI.get().registry().totemCarvings()));
        }
    }

    /**
     * Returns a Predicate that checks whether the given object is a valid String defining a valid ResourceLocation
     * contained in the given registry,
     *
     * Using a Supplier for the registry since our registries are not initialized before the config spec is built.
     */
    private static Predicate<Object> isValidRegistryKey(Supplier<IForgeRegistry<?>> registry) {
        return obj -> {
            if(!(obj instanceof String str))
                return false;
            var key = ResourceLocation.tryParse(str);
            if(key == null)
                return false;
            return registry.get().containsKey(key);
        };
    }

    public static final Client CLIENT;
    public static final Server SERVER;

    private static final ForgeConfigSpec clientSpec;
    private static final ForgeConfigSpec serverSpec;

    static {
        var clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = clientPair.getLeft();
        clientSpec = clientPair.getRight();

        var serverPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = serverPair.getLeft();
        serverSpec = serverPair.getRight();
    }

    public static void register(ModLoadingContext context) {
        context.registerConfig(Type.CLIENT, clientSpec);
        context.registerConfig(Type.SERVER, serverSpec);
    }
}
