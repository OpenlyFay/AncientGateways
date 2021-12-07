package openlyfay.ancientgateways.entity;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import openlyfay.ancientgateways.util.RegHandler;

import static openlyfay.ancientgateways.AncientGateways.ANCIENT_GATEWAYS_MAIN;
import static openlyfay.ancientgateways.AncientGateways.MOD_ID;


public class RegisterEntity {

    public static EntityType<ChorusInkBottleEntity> CHORUS_INK_ENTITY;
    public static Identifier BOTTLE_ID = new Identifier(MOD_ID,"chorus_ink_entity");
    public static Identifier PEARL_ID = new Identifier(MOD_ID,"chorus_pearl_entity");

    public static void register(){
        CHORUS_INK_ENTITY = RegHandler.entity(BOTTLE_ID, EntityType.Builder.<ChorusInkBottleEntity>create(ChorusInkBottleEntity::new, SpawnGroup.MISC).maxTrackingRange(4).trackingTickInterval(10).setDimensions(0.25f,0.25f).build(BOTTLE_ID.getPath()));
    }
}
