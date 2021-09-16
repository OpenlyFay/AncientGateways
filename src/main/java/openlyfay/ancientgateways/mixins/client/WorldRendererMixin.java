package openlyfay.ancientgateways.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import openlyfay.ancientgateways.util.SpiralHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static openlyfay.ancientgateways.AncientGateways.DIM_ID;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private ClientWorld world;

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "renderSky",at = @At("HEAD"), cancellable = true)
    public void onRenderSky(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        if (world.getRegistryKey() == RegistryKey.of(Registry.DIMENSION,DIM_ID)){
            //BlockPos blockPos = SpiralHelper.findNearestAnchor(client.player.getPos());
            ci.cancel();
        }
    }

    //TODO: custom skyboxes, custom cloud heights

}
