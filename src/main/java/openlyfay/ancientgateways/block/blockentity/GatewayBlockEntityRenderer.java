package openlyfay.ancientgateways.block.blockentity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;


@Environment(EnvType.CLIENT)
public class GatewayBlockEntityRenderer extends BlockEntityRenderer<GatewayBlockEntity> {
    private static final Identifier PORTAL_SPRITE_ROOT = new Identifier("ancientgateways","animations/portal_7/portal_");


    public GatewayBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }



    @Override
    public void render(GatewayBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction direction = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);
        DefaultedList<ItemStack> items = blockEntity.getItems();
        int x = direction.getOffsetX();
        int z = direction.getOffsetZ();
        int offset;
        int procession = 1;
        Quaternion renderDir = Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation());
        MinecraftClient client = MinecraftClient.getInstance();

        if (direction.getAxis() == Direction.Axis.X) {
            offset = 120;
            renderDir = Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation() - 180);
        } else {
            offset = -60;
            procession = -1;
        }
        matrices.push();
        int lightBelow = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().offset(direction));
        for (int i = 0; i < items.size(); i++) {
            double rads = Math.toRadians((60 * i + offset) * procession);
            matrices.push();
            matrices.translate(0.5D + x * 0.75D, 0.5D, 0.5D + z * 0.75D);
            matrices.translate(Math.cos(rads) * z, Math.sin(rads), Math.cos(rads) * x);
            matrices.multiply(renderDir);
            client.getItemRenderer().renderItem(items.get(i), ModelTransformation.Mode.GROUND, lightBelow, overlay, matrices, vertexConsumers);
            matrices.pop();
        }
        if(blockEntity.isActive()){
            int ticks = (int)((blockEntity.getWorld().getTime()+tickDelta)/1.25)%64;
            Identifier PORTAL_SPRITE = new Identifier(PORTAL_SPRITE_ROOT.getNamespace(),PORTAL_SPRITE_ROOT.getPath() + (201 + ticks) +".png" );

        matrices.push();

        Matrix4f matrix4f = matrices.peek().getModel();

        client.getTextureManager().bindTexture(PORTAL_SPRITE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int xFactor = 0;
        int zFactor = 0;
        if (direction.getAxis() == Direction.Axis.X) {
            zFactor = 1;
        }
        else {
            xFactor = 1;
        }

            buffer.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.color4f(255,255,255,255);
            buffer.vertex(matrix4f, 0.5F - (xFactor*2.5F), 0.0F, 0.5F - (zFactor*2.5F))
                .texture(0f, 0f)
                .color(255, 255, 255, 255)
                .next();
            buffer.vertex(matrix4f, 0.5F - (xFactor*2.5F), -5.0F, 0.5F - (zFactor*2.5F))
                .texture(0f, 1f)
                .color(255, 255, 255, 255)
                .next();
            buffer.vertex(matrix4f, 0.5F + (xFactor*2.5F), -5.0F, 0.5F + (zFactor*2.5F))
                .texture(1f, 1f)
                .color(255, 255, 255, 255)
                .next();
            buffer.vertex(matrix4f, 0.5F + (xFactor*2.5F), 0.0F,0.5F + (zFactor*2.5F))
                .texture(1f, 0f)
                .color(255, 255, 255, 255)
                .next();
            tessellator.draw();

            buffer.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            buffer.vertex(matrix4f, 0.5F + (xFactor*2.5F), 0.0F,0.5F + (zFactor*2.5F))
                    .texture(0f,0f)
                    .color(255,255,255,255)
                    .next();
            buffer.vertex(matrix4f, 0.5F + (xFactor*2.5F), -5.0F, 0.5F + (zFactor*2.5F))
                    .texture(0f,1f)
                    .color(255,255,255,255)
                    .next();
            buffer.vertex(matrix4f, 0.5F - (xFactor*2.5F), -5.0F, 0.5F - (zFactor*2.5F))
                    .texture(1f,1f)
                    .color(255,255,255,255)
                    .next();
            buffer.vertex(matrix4f, 0.5F - (xFactor*2.5F), 0.0F, 0.5F - (zFactor*2.5F))
                    .texture(1f,0f)
                    .color(255,255,255,255)
                    .next();
            tessellator.draw();

            matrices.pop();
        }


        matrices.pop();
    }

}
