package openlyfay.ancientgateways.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import openlyfay.ancientgateways.block.blockentity.AnchorBaseEntity;
import openlyfay.ancientgateways.util.CustomSkybox;

public class CustomSkyboxRenderer {
    private static final float sunsetFactor = 1.3f;

    public static void render(MatrixStack matrices, int ticks, float tickDelta, World world, AnchorBaseEntity entity, TextureManager textureManager){
        CustomSkybox skybox = entity.getSkybox();

        boolean day = skybox.isDay(ticks + tickDelta);

        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        for (int i = 0; i < 6;i++){
            matrices.push();
            switch (i){
                case 0:
                    if (day){
                        textureManager.bindTexture(skybox.getDown());
                    }
                    else {
                        textureManager.bindTexture(skybox.getDownNight());
                    }

                    break;
                case 1:
                    if (day){
                        textureManager.bindTexture(skybox.getWest());
                    }
                    else {
                        textureManager.bindTexture(skybox.getWestNight());
                    }
                    matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
                    matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

                    break;
                case 2:
                    if (day){
                        textureManager.bindTexture(skybox.getEast());
                    }
                    else {
                        textureManager.bindTexture(skybox.getEastNight());
                    }
                    matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
                    matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270.0F));

                    break;
                case 3:
                    if (day){
                        textureManager.bindTexture(skybox.getUp());
                    }
                    else {
                        textureManager.bindTexture(skybox.getUpNight());
                    }
                    matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));

                    break;
                case 4:
                    if (day){
                        textureManager.bindTexture(skybox.getNorth());
                    }
                    else {
                        textureManager.bindTexture(skybox.getNorthNight());
                    }
                    matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));

                    break;
                case 5:
                    if (day){
                        textureManager.bindTexture(skybox.getSouth());
                    }
                    else {
                        textureManager.bindTexture(skybox.getSouthNight());
                    }
                    matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                    matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

                    break;
            }
            Matrix4f matrix4f = matrices.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 1.0F).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(1.0F, 0.0F).next();
            tessellator.draw();
            matrices.pop();
        }
        if (skybox.isDawn((float) ticks + tickDelta)){
            for (int i = 0; i < 6;i++) {
                matrices.push();
                switch (i) {
                    case 0:
                        textureManager.bindTexture(skybox.getDownNight());

                        break;
                    case 1:
                        textureManager.bindTexture(skybox.getWestNight());
                        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

                        break;
                    case 2:
                        textureManager.bindTexture(skybox.getEastNight());
                        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270.0F));

                        break;
                    case 3:
                        textureManager.bindTexture(skybox.getUpNight());
                        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));

                        break;
                    case 4:
                        textureManager.bindTexture(skybox.getNorthNight());
                        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));

                        break;
                    case 5:
                        textureManager.bindTexture(skybox.getSouthNight());
                        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

                        break;
                }
                Matrix4f matrix4f = matrices.peek().getModel();
                bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
                float dawnTime = skybox.getDawnProgress(ticks + tickDelta);
                float red = 0f;
                float green = 0f;
                float blue = 0f;
                int j = 0;
                for (CustomSkybox.DynamicSkyboxObject object : skybox.getHorizonObjects(ticks + tickDelta)){
                    j++;
                    red += (float) object.getSunsetColour().getRed()/255 * (1 - ((float) object.getOrbitPosition(ticks) - 90) / 15);
                    green += (float) object.getSunsetColour().getGreen()/255 * (1 - ((float) object.getOrbitPosition(ticks) - 90) / 15);
                    blue += (float) object.getSunsetColour().getBlue()/255 * (1 - ((float) object.getOrbitPosition(ticks) - 90) / 15);
                }
                red = red/j;
                green = green/j;
                blue = blue/j;
                bufferBuilder.vertex(matrix4f, -99.9F, -99.9F, -99.9F).texture(0.0F, 0.0F).color(red,green,blue,dawnTime/sunsetFactor).next();
                bufferBuilder.vertex(matrix4f, -99.9F, -99.9F, 99.9F).texture(0.0F, 1.0F).color(red,green,blue,dawnTime/sunsetFactor).next();
                bufferBuilder.vertex(matrix4f, 99.9F, -99.9F, 99.9F).texture(1.0F, 1.0F).color(red,green,blue,dawnTime/sunsetFactor).next();
                bufferBuilder.vertex(matrix4f, 99.9F, -99.9F, -99.9F).texture(1.0F, 0.0F).color(red,green,blue,dawnTime/sunsetFactor).next();
                tessellator.draw();
                matrices.pop();
            }
        }
        if (skybox.isDusk((float) ticks + tickDelta)){
            for (int i = 0; i < 6;i++) {
                matrices.push();
                switch (i) {
                    case 0:
                        textureManager.bindTexture(skybox.getDown());

                        break;
                    case 1:
                        textureManager.bindTexture(skybox.getWest());
                        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

                        break;
                    case 2:
                        textureManager.bindTexture(skybox.getEast());
                        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270.0F));

                        break;
                    case 3:
                        textureManager.bindTexture(skybox.getUp());
                        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));

                        break;
                    case 4:
                        textureManager.bindTexture(skybox.getNorth());
                        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                        break;
                    case 5:
                        textureManager.bindTexture(skybox.getSouth());
                        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
                        break;
                }
                Matrix4f matrix4f = matrices.peek().getModel();
                bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
                float duskTime = skybox.getDuskProgress((float) ticks + tickDelta);
                float red = 0f;
                float green = 0f;
                float blue = 0f;
                int j = 0;
                for (CustomSkybox.DynamicSkyboxObject object : skybox.getHorizonObjects(ticks + tickDelta)){
                    j++;
                    red += (float) object.getSunsetColour().getRed()/255 * (((float) object.getOrbitPosition(ticks) - 270)/-15);
                    green += (float) object.getSunsetColour().getGreen()/255 * (((float) object.getOrbitPosition(ticks) - 270)/-15);
                    blue += (float) object.getSunsetColour().getBlue()/255 * (((float) object.getOrbitPosition(ticks) - 270)/-15);
                }
                red = red/j;
                green = green/j;
                blue = blue/j;
                bufferBuilder.vertex(matrix4f, -99.9F, -99.9F, -99.9F).texture(0.0F, 0.0F).color(red,green,blue,duskTime/sunsetFactor).next();
                bufferBuilder.vertex(matrix4f, -99.9F, -99.9F, 99.9F).texture(0.0F, 1.0F).color(red,green,blue,duskTime/sunsetFactor).next();
                bufferBuilder.vertex(matrix4f, 99.9F, -99.9F, 99.9F).texture(1.0F, 1.0F).color(red,green,blue,duskTime/sunsetFactor).next();
                bufferBuilder.vertex(matrix4f, 99.9F, -99.9F, -99.9F).texture(1.0F, 0.0F).color(red,green,blue,duskTime/sunsetFactor).next();
                tessellator.draw();
                matrices.pop();
            }
        }
        float i = -99.9f;
        for (CustomSkybox.DynamicSkyboxObject object : skybox.getDYN()){
            i += 0.1;
            matrices.push();
            textureManager.bindTexture(object.getCurrentTexture((ticks + tickDelta)));
            matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion((float) object.getEquatorAngle()));
            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((float) object.getOrbitPosition((float) ticks + tickDelta)));
            float x = (float) object.getSizeX()/2;
            float y = (float) object.getSizeY()/2;
            Matrix4f matrix4f = matrices.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -x, i, -y).texture(0.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f, -x, i, y).texture(0.0F, 1.0F).next();
            bufferBuilder.vertex(matrix4f, x, i, y).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(matrix4f, x, i, -y).texture(1.0F, 0.0F).next();
            tessellator.draw();
            matrices.pop();
        }
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }
}
