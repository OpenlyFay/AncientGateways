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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import openlyfay.ancientgateways.block.blockentity.AnchorBaseEntity;
import openlyfay.ancientgateways.render.CustomSkyboxRenderer;
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
                CustomSkyboxRenderer.render(matrices, ticks, tickDelta, world, (AnchorBaseEntity) entity, textureManager);
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

}
