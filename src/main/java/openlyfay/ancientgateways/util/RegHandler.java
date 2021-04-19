package openlyfay.ancientgateways.util;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegHandler {


    public static <T extends Entity> EntityType<T> entity(Identifier id, EntityType<T> entityType) {
        return Registry.register(Registry.ENTITY_TYPE, id, entityType);
    }

    public static <T extends BlockEntity> BlockEntityType<T> blockEntity(Identifier id, BlockEntityType<T> blockEntityType){
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    public static Block block(Identifier id, Block block){
        return Registry.register(Registry.BLOCK, id, block);
    }

    public static Item item(Identifier id, Item item){
        return Registry.register(Registry.ITEM, id, item);
    }

}
