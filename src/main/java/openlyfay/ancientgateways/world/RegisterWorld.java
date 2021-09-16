package openlyfay.ancientgateways.world;


import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import static openlyfay.ancientgateways.AncientGateways.*;


public class RegisterWorld {

    public static final int POCKET_MAX_SIZE = agConfig.maxPocketExpansions * agConfig.pocketDimensionInitRadius * 2;

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
        Registry.register(Registry.CHUNK_GENERATOR, DIM_ID, EmptySpaceChunkGenerator.CODEC);

        WORLD_KEY = RegistryKey.of(Registry.DIMENSION, DIM_ID);

    }
}
