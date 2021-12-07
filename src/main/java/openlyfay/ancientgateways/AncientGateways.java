package openlyfay.ancientgateways;


import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import openlyfay.ancientgateways.block.RegisterBlocks;
import openlyfay.ancientgateways.entity.RegisterEntity;
import openlyfay.ancientgateways.item.*;
import openlyfay.ancientgateways.util.MasterListComponent;

import static openlyfay.ancientgateways.block.RegisterBlocks.gateway_block;


public class AncientGateways implements ModInitializer {

    public static final String MOD_ID = "ancientgateways";
    public static AncientGatewaysConfig agConfig;

    @Override
    public void onInitialize(){
        AutoConfig.register(AncientGatewaysConfig.class, GsonConfigSerializer::new);
        agConfig = AutoConfig.getConfigHolder(AncientGatewaysConfig.class).getConfig();


        RegisterBlocks.register();
        RegisterEntity.register();
        RegisterItem.register();

    }

    public static final ItemGroup ANCIENT_GATEWAYS_MAIN = FabricItemGroupBuilder.create(
            new Identifier(MOD_ID, "general")).icon(() -> new ItemStack(gateway_block))
            .build();
}


