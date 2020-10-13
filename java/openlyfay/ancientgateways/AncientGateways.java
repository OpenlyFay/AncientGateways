package openlyfay.ancientgateways;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import openlyfay.ancientgateways.block.GatewayBlock;
import openlyfay.ancientgateways.blockentity.GatewayBlockEntity;

public class AncientGateways implements ModInitializer {

    public static final Block gateway_block = new GatewayBlock(FabricBlockSettings.of(Material.STONE).hardness(8.0f));
    public static BlockEntityType<GatewayBlockEntity> GATEWAY_BLOCK_ENTITY;
    public static final String MOD_ID = "ancientgateways";


    @Override
    public void onInitialize(){
        Registry.register(Registry.BLOCK, new Identifier("ancientgateways", "gatewayblock"), gateway_block);
        Registry.register(Registry.ITEM, new Identifier("ancientgateways", "gatewayblock"),new BlockItem(gateway_block, new Item.Settings().group(ItemGroup.TRANSPORTATION)));
        GATEWAY_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,"ancientgateways:gateway_block_entity", BlockEntityType.Builder.create(GatewayBlockEntity::new, gateway_block).build(null));
    }

}
/** To do list:
 *  - Rune items (waiting on textures)
 *  - Rune blocks (waiting on textures)
 *  - Implement master list - DONE
 *  - Master list read from/write to - IMPLEMENTED but not yet connected to gatewayBE
 *  - Portal plane - DONE
 *  - Multiblock structure check - DONE
 *  - Misc fixes noted at locations where they're needed
 *  - Portal graphics
 *  - Floating item graphics
 */
