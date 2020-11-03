package openlyfay.ancientgateways;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import openlyfay.ancientgateways.blockentity.GatewayBlockEntityRenderer;


public class  AncientGatewaysClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(AncientGateways.GATEWAY_BLOCK_ENTITY, GatewayBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.black_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.blue_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.brown_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.cyan_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.green_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.grey_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.light_blue_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.light_grey_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.lime_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.magenta_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.orange_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.pink_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.purple_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.red_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.white_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AncientGateways.yellow_rune_block, RenderLayer.getCutout());
    }

}
