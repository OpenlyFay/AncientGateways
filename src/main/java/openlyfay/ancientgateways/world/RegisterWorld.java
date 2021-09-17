package openlyfay.ancientgateways.world;



import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import static openlyfay.ancientgateways.AncientGateways.*;


public class RegisterWorld {

    public static final int POCKET_MAX_SIZE = agConfig.maxPocketExpansions * agConfig.pocketDimensionInitRadius * 2;

    public static Biome.Builder pocketBuilder = new Biome.Builder()
            .category(Biome.Category.NONE)
            .depth(0)
            .downfall(0)
            .scale(0)
            .temperature(0.7f)
            .temperatureModifier(Biome.TemperatureModifier.NONE)
            .precipitation(Biome.Precipitation.NONE)
            .spawnSettings(new SpawnSettings.Builder().build())
            .generationSettings(GenerationSettings.INSTANCE);

    public static Biome POCKET_BIOME = pocketBuilder.effects(new BiomeEffects.Builder()
            .grassColor(11272039)
            .waterColor(3625215)
            .foliageColor(5242667)
            .fogColor(8421536)
            .waterFogColor(3625215)
            .skyColor(12632319).build()).build();

    public static final RegistryKey<Biome> POCKETS_KEY = RegistryKey.of(Registry.BIOME_KEY, new Identifier(MOD_ID,"pockets_biome"));

    private static final RegistryKey<DimensionOptions> DIMENSION_KEY = RegistryKey.of(Registry.DIMENSION_OPTIONS,DIM_ID);

    private static RegistryKey<World> WORLD_KEY = RegistryKey.of(
            Registry.DIMENSION,
            DIMENSION_KEY.getValue()
    );

    private static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(
            Registry.DIMENSION_TYPE_KEY,
            new Identifier(MOD_ID,"pocket_type")
    );

    public static void register(){
        Registry.register(BuiltinRegistries.BIOME, POCKETS_KEY.getValue(),POCKET_BIOME);

        Registry.register(Registry.CHUNK_GENERATOR, DIM_ID, EmptySpaceChunkGenerator.CODEC);

        WORLD_KEY = RegistryKey.of(Registry.DIMENSION, DIM_ID);

    }
}
