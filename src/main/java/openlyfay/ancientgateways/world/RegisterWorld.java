package openlyfay.ancientgateways.world;


import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.function.Predicate;

import static openlyfay.ancientgateways.AncientGateways.*;
import static openlyfay.ancientgateways.block.RegisterBlocks.ANCHOR;
import static openlyfay.ancientgateways.block.RegisterBlocks.ANCHOR_BLOCK;


public class RegisterWorld {


    private static PointOfInterestType ANCHOR_TYPE;

    private static int pocketMaxSize;

    public static final Predicate<PointOfInterestType> isAnchor = (pointOfInterestType) -> {
        if (pointOfInterestType == ANCHOR_TYPE){
            return true;
        }
        else {
            return false;
        }
    };

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

        pocketMaxSize = agConfig.maxPocketExpansions * agConfig.pocketDimensionInitRadius * 2;

        ANCHOR_TYPE = PointOfInterestHelper.register(ANCHOR,1, isAnchor,pocketMaxSize,ANCHOR_BLOCK);

        WORLD_KEY = RegistryKey.of(Registry.DIMENSION, DIM_ID);

    }

    public static int getPocketMaxSize() {
        return pocketMaxSize;
    }
}
