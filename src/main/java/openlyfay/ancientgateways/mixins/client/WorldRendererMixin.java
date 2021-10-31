package openlyfay.ancientgateways.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import openlyfay.ancientgateways.block.blockentity.AnchorBaseEntity;
import openlyfay.ancientgateways.util.CustomSkybox;
import openlyfay.ancientgateways.util.SpiralHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static openlyfay.ancientgateways.AncientGateways.DIM_ID;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private ClientWorld world;

    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow @Final private TextureManager textureManager;

    @Shadow private int ticks;

    @Shadow public abstract void tick();

    @Inject(method = "renderSky",at = @At("HEAD"), cancellable = true)
    public void onRenderSky(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        if (world.getRegistryKey() == RegistryKey.of(Registry.DIMENSION,DIM_ID)){
            BlockPos blockPos = SpiralHelper.findNearestAnchor(client.player.getPos());
            BlockEntity entity = world.getBlockEntity(blockPos);
            if (entity instanceof AnchorBaseEntity && ((AnchorBaseEntity) entity).getSkybox() != null){
                CustomSkybox skybox = ((AnchorBaseEntity) entity).getSkybox();

                boolean day = skybox.isDay(world.getTime() + tickDelta);

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
                                System.out.println(skybox.getNorthNight());
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
                    bufferBuilder.vertex(matrix4f, -128.0F, -128.0F, -128.0F).texture(0.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix4f, -128.0F, -128.0F, 128.0F).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix4f, 128.0F, -128.0F, 128.0F).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix4f, 128.0F, -128.0F, -128.0F).texture(1.0F, 0.0F).next();
                    tessellator.draw();
                    matrices.pop();
                }
                if (skybox.isDawn((float) world.getTime() + tickDelta)){
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
                        float dawnTime = skybox.getDawnProgress(world.getTime() + tickDelta);
                        bufferBuilder.vertex(matrix4f, -127.0F, -127.0F, -127.0F).texture(0.0F, 0.0F).color(1.0f,1.0f,1.0f,dawnTime).next();
                        bufferBuilder.vertex(matrix4f, -127.0F, -127.0F, 127.0F).texture(0.0F, 1.0F).color(1.0f,1.0f,1.0f,dawnTime).next();
                        bufferBuilder.vertex(matrix4f, 127.0F, -127.0F, 127.0F).texture(1.0F, 1.0F).color(1.0f,1.0f,1.0f,dawnTime).next();
                        bufferBuilder.vertex(matrix4f, 127.0F, -127.0F, -127.0F).texture(1.0F, 0.0F).color(1.0f,1.0f,1.0f,dawnTime).next();
                        tessellator.draw();
                        matrices.pop();
                    }
                }
                if (skybox.isDusk((float) world.getTime() + tickDelta)){
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
                        float duskTime = skybox.getDuskProgress((float) world.getTime() + tickDelta);
                        bufferBuilder.vertex(matrix4f, -127.0F, -127.0F, -127.0F).texture(0.0F, 0.0F).color(1.0f,1.0f,1.0f,duskTime).next();
                        bufferBuilder.vertex(matrix4f, -127.0F, -127.0F, 127.0F).texture(0.0F, 1.0F).color(1.0f,1.0f,1.0f,duskTime).next();
                        bufferBuilder.vertex(matrix4f, 127.0F, -127.0F, 127.0F).texture(1.0F, 1.0F).color(1.0f,1.0f,1.0f,duskTime).next();
                        bufferBuilder.vertex(matrix4f, 127.0F, -127.0F, -127.0F).texture(1.0F, 0.0F).color(1.0f,1.0f,1.0f,duskTime).next();
                        tessellator.draw();
                        matrices.pop();
                    }
                }
                float i = -126.0f;
                for (CustomSkybox.DynamicSkyboxObject object : skybox.getDYN()){
                    i++;
                    matrices.push();
                    textureManager.bindTexture(object.getCurrentTexture((world.getTime() + tickDelta)));
                    matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion((float) object.getEquatorAngle()));
                    matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((float) object.getOrbitPosition((float) world.getTime() + tickDelta)));
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


                ci.cancel();
            }
        }
    }

    @ModifyVariable(
            method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;FDDD)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/SkyProperties;getCloudsHeight()F"),
            ordinal = 1
    )
    private float modifyCloudHeight(float f){
        if (world.getRegistryKey() == RegistryKey.of(Registry.DIMENSION,DIM_ID)){
            BlockPos blockPos = SpiralHelper.findNearestAnchor(client.player.getPos());
            BlockEntity entity = world.getBlockEntity(blockPos);

            if (entity instanceof AnchorBaseEntity && ((AnchorBaseEntity) entity).getSkybox() != null){
                return ((AnchorBaseEntity) entity).getSkybox().getCloudHeight();
            }
        }
        return f;
    }
    //TODO: skybox rotation, sunsets

}
