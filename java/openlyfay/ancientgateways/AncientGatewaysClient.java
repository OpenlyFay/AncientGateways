package openlyfay.ancientgateways;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import openlyfay.ancientgateways.blockentity.GatewayBlockEntityRenderer;

import static openlyfay.ancientgateways.AncientGateways.GATEWAY_BLOCK_ENTITY;

public class  AncientGatewaysClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(GATEWAY_BLOCK_ENTITY, GatewayBlockEntityRenderer::new);
    }

}
