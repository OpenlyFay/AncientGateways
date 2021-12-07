package openlyfay.ancientgateways;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import openlyfay.ancientgateways.block.blockentity.GatewayBlockEntityRenderer;


import static openlyfay.ancientgateways.block.RegisterBlocks.*;
import static openlyfay.ancientgateways.entity.RegisterEntity.CHORUS_INK_ENTITY;


public class  AncientGatewaysClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(CHORUS_INK_ENTITY, ((dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher,context.getItemRenderer())));
        BlockEntityRendererRegistry.INSTANCE.register(GATEWAY_BLOCK_ENTITY, GatewayBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(black_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(blue_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(brown_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(cyan_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(green_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(grey_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(light_blue_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(light_grey_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(lime_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(magenta_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(orange_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(pink_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(purple_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(red_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(white_rune_block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(yellow_rune_block, RenderLayer.getCutout());
    }

}
