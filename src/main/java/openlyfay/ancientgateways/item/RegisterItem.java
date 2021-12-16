package openlyfay.ancientgateways.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import openlyfay.ancientgateways.entity.ChorusInkBottleEntity;
import openlyfay.ancientgateways.util.RegHandler;

import static openlyfay.ancientgateways.AncientGateways.ANCIENT_GATEWAYS_MAIN;
import static openlyfay.ancientgateways.AncientGateways.MOD_ID;

public class RegisterItem {

    public static Item CHORUS_INK_ITEM;
    public static Item RECALL_TABLET;
    public static Item HARDENED_RECALL_TABLET;
    public static Item WISE_TABLET;
    public static Item HARDENED_WISE_TABLET;




    public static void register(){


        CHORUS_INK_ITEM = RegHandler.item(new Identifier(MOD_ID,"chorus_ink_item"),new ChorusInkBottleItem(new Item.Settings().group(ANCIENT_GATEWAYS_MAIN).maxCount(16)));
        DispenserBlock.registerBehavior(CHORUS_INK_ITEM, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                ChorusInkBottleEntity entity = new ChorusInkBottleEntity(world,position.getX(),position.getY(),position.getZ());
                entity.setItem(stack);
                return entity;
            }
        });
        RECALL_TABLET = RegHandler.item(new Identifier(MOD_ID,"recall_tablet"), new RecallTablet(new Item.Settings().group(ANCIENT_GATEWAYS_MAIN).maxCount(1).rarity(Rarity.UNCOMMON)));
        HARDENED_RECALL_TABLET = RegHandler.item(new Identifier(MOD_ID,"hardened_recall_tablet"), new RecallTablet(new Item.Settings().group(ANCIENT_GATEWAYS_MAIN).maxCount(1).rarity(Rarity.RARE).fireproof()));
        WISE_TABLET = RegHandler.item(new Identifier(MOD_ID,"wise_tablet"), new WiseTablet(new Item.Settings().group(ANCIENT_GATEWAYS_MAIN).maxCount(1).rarity(Rarity.UNCOMMON)));
        HARDENED_WISE_TABLET = RegHandler.item(new Identifier(MOD_ID,"hardened_wise_tablet"), new WiseTablet(new Item.Settings().group(ANCIENT_GATEWAYS_MAIN).maxCount(1).rarity(Rarity.RARE).fireproof()));
    }

}
