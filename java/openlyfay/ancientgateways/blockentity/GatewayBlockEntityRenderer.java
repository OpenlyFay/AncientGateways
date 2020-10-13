package openlyfay.ancientgateways.blockentity;

import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.util.math.Quaternion;



public class GatewayBlockEntityRenderer extends BlockEntityRenderer<GatewayBlockEntity> {
    private Identifier PORTAL_SPRITE = new Identifier("ancientgateways","assets/animations.portal_7.portal_201.png");

    public GatewayBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }



    @Override
    public void render(GatewayBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction direction = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);
        int x = direction.getOffsetX();
        int z = direction.getOffsetZ();
        int offset;
        int procession = 1;
        Quaternion renderDir = Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation());
        MinecraftClient client = MinecraftClient.getInstance();

        if(direction.getAxis() == Direction.Axis.X){
            offset = 120;
            renderDir = Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation() - 180);
        }
        else {
            offset = -60;
            procession = -1;
        }
        matrices.push();
        DefaultedList<ItemStack> items = blockEntity.getItems();
        int lightBelow = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().down());
        for(int i = 0; i < items.size(); i++){
            double rads = Math.toRadians((60*i+offset)*procession);
            matrices.push();
            matrices.translate(0.5D + x * 0.75D, 0.5D, 0.5D + z * 0.75D);
            matrices.translate(Math.cos(rads)*z, Math.sin(rads),Math.cos(rads)*x);
            matrices.multiply(renderDir);
            client.getItemRenderer().renderItem(items.get(i), ModelTransformation.Mode.GROUND,lightBelow,overlay,matrices,vertexConsumers);
            matrices.pop();
        }
        //if(blockEntity.getCountdown() > 0){
            matrices.push();

            double x1 = blockEntity.getPos().getX();
            double y1 = blockEntity.getPos().getY();
            double z1 = blockEntity.getPos().getZ();

            client.getTextureManager().bindTexture(PORTAL_SPRITE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(7, VertexFormats.POSITION_COLOR_TEXTURE);
            buffer.vertex(x1-2.0D,y1-1.0D,z1)
                    .color(255,255,255,255)
                    .texture(0f,0f)
                    .next();
            buffer.vertex(x1-2.0D,y1-6.0D,z1)
                    .color(255,255,255,255)
                    .texture(0f,1f)
                    .next();
            buffer.vertex(x1+2.0D,y1-1.0D,z1)
                    .color(255,255,255,255)
                    .texture(1f,1f)
                    .next();
            buffer.vertex(x1+2.0D,y1-6.0D,z1)
                    .color(255,255,255,255)
                    .texture(1f,0f)
                    .next();
            tessellator.draw();

            matrices.pop();
        //}


        matrices.pop();
    }

}
