package openlyfay.ancientgateways;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.fabric.mixin.biome.modification.BiomeAccessor;
import net.fabricmc.fabric.mixin.object.builder.PointOfInterestTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.Material;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestType;
import openlyfay.ancientgateways.block.AnchorMonolith;
import openlyfay.ancientgateways.block.Barrier;
import openlyfay.ancientgateways.block.GatewayBlock;
import openlyfay.ancientgateways.block.RegisterBlocks;
import openlyfay.ancientgateways.block.blockentity.AnchorMonolithEntity;
import openlyfay.ancientgateways.block.blockentity.GatewayBlockEntity;
import openlyfay.ancientgateways.block.blockitem.*;
import openlyfay.ancientgateways.block.runes.*;
import openlyfay.ancientgateways.entity.ChorusInkBottleEntity;
import openlyfay.ancientgateways.entity.ChorusPearlEntity;
import openlyfay.ancientgateways.entity.RegisterEntity;
import openlyfay.ancientgateways.item.*;
import openlyfay.ancientgateways.util.RegHandler;
import openlyfay.ancientgateways.util.SpiralHelper;
import openlyfay.ancientgateways.world.EmptySpaceChunkGenerator;
import openlyfay.ancientgateways.world.RegisterWorld;

import java.util.function.Predicate;

import static openlyfay.ancientgateways.block.RegisterBlocks.gateway_block;
import static openlyfay.ancientgateways.block.RegisterBlocks.register;


public class AncientGateways implements ModInitializer {

    public static final String MOD_ID = "ancientgateways";
    public static final Identifier DIM_ID = new Identifier(MOD_ID,"pockets");

    public static AncientGatewaysConfig agConfig;

    @Override
    public void onInitialize(){
        AutoConfig.register(AncientGatewaysConfig.class, GsonConfigSerializer::new);
        agConfig = AutoConfig.getConfigHolder(AncientGatewaysConfig.class).getConfig();

        RegisterBlocks.register();
        RegisterEntity.register();
        RegisterItem.register();
        RegisterWorld.register();

    }

    public static final ItemGroup ANCIENT_GATEWAYS_MAIN = FabricItemGroupBuilder.create(
            new Identifier(MOD_ID, "general")).icon(() -> new ItemStack(gateway_block))
            .build();
}


