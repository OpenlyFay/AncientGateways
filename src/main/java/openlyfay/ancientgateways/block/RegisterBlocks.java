package openlyfay.ancientgateways.block;


import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import openlyfay.ancientgateways.block.blockentity.GatewayBlockEntity;
import openlyfay.ancientgateways.block.blockitem.*;
import openlyfay.ancientgateways.block.runes.*;
import openlyfay.ancientgateways.util.RegHandler;

import static openlyfay.ancientgateways.AncientGateways.ANCIENT_GATEWAYS_MAIN;
import static openlyfay.ancientgateways.AncientGateways.MOD_ID;

public class RegisterBlocks {

    public static final Block gateway_block = new GatewayBlock(FabricBlockSettings.of(Material.STONE).requiresTool().luminance(10).hardness(50).resistance(1200));
    public static BlockEntityType<GatewayBlockEntity> GATEWAY_BLOCK_ENTITY;
    public static final Block black_rune_block = new BlackRuneBlock(FabricBlockSettings.of(new Material(MapColor.BLACK,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block red_rune_block = new RedRuneBlock(FabricBlockSettings.of(new Material(MapColor.RED,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block green_rune_block = new GreenRuneBlock(FabricBlockSettings.of(new Material(MapColor.GREEN,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block brown_rune_block = new BrownRuneBlock(FabricBlockSettings.of(new Material(MapColor.BROWN,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block blue_rune_block = new BlueRuneBlock(FabricBlockSettings.of(new Material(MapColor.BLUE,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block purple_rune_block = new PurpleRuneBlock(FabricBlockSettings.of(new Material(MapColor.PURPLE,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block cyan_rune_block = new CyanRuneBlock(FabricBlockSettings.of(new Material(MapColor.CYAN,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block light_grey_rune_block = new LightGreyRuneBlock(FabricBlockSettings.of(new Material(MapColor.LIGHT_GRAY,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block grey_rune_block = new GreyRuneBlock(FabricBlockSettings.of(new Material(MapColor.GRAY,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block pink_rune_block = new PinkRuneBlock(FabricBlockSettings.of(new Material(MapColor.PINK,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block lime_rune_block = new LimeRuneBlock(FabricBlockSettings.of(new Material(MapColor.LIME,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block yellow_rune_block = new YellowRuneBlock(FabricBlockSettings.of(new Material(MapColor.YELLOW,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block light_blue_rune_block = new LightBlueRuneBlock(FabricBlockSettings.of(new Material(MapColor.LIGHT_BLUE,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block magenta_rune_block = new MagentaRuneBlock(FabricBlockSettings.of(new Material(MapColor.MAGENTA,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block orange_rune_block = new OrangeRuneBlock(FabricBlockSettings.of(new Material(MapColor.ORANGE,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());
    public static final Block white_rune_block = new WhiteRuneBlock(FabricBlockSettings.of(new Material(MapColor.WHITE,false,false,false,false,false, false, PistonBehavior.NORMAL)).nonOpaque());


    public static void register(){
        RegHandler.block(new Identifier(MOD_ID, "gatewayblock"), gateway_block);
        RegHandler.item(new Identifier(MOD_ID, "gatewayblock"),new GatewayBlockItem(gateway_block, new Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)
                .rarity(Rarity.UNCOMMON)));
        GATEWAY_BLOCK_ENTITY = RegHandler.blockEntity(new Identifier( MOD_ID, "gateway_block_entity"), FabricBlockEntityTypeBuilder.create(GatewayBlockEntity::new, gateway_block).build(null));

        RegHandler.block(new Identifier(MOD_ID, "rune_black"),black_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_black"), new BlackRuneItem(black_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_red"),red_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_red"), new RedRuneItem(red_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_green"),green_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_green"), new GreenRuneItem(green_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_brown"),brown_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_brown"), new BrownRuneItem(brown_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_blue"),blue_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_blue"), new BlueRuneItem(blue_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_purple"),purple_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_purple"), new PurpleRuneItem(purple_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_cyan"),cyan_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_cyan"), new CyanRuneItem(cyan_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_light_grey"),light_grey_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_light_grey"), new LightGreyRuneItem(light_grey_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_grey"),grey_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_grey"), new GreyRuneItem(grey_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_pink"),pink_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_pink"), new PinkRuneItem(pink_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_lime"),lime_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_lime"), new LimeRuneItem(lime_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_yellow"),yellow_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_yellow"), new YellowRuneItem(yellow_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_light_blue"),light_blue_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_light_blue"), new LightBlueRuneItem(light_blue_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_magenta"),magenta_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_magenta"), new MagentaRuneItem(magenta_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_orange"),orange_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_orange"), new OrangeRuneItem(orange_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));

        RegHandler.block(new Identifier(MOD_ID, "rune_white"),white_rune_block);
        RegHandler.item(new Identifier(MOD_ID, "rune_white"), new WhiteRuneItem(white_rune_block, new  Item.Settings()
                .maxCount(16)
                .group(ANCIENT_GATEWAYS_MAIN)));


    }
}
