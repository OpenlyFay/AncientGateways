package openlyfay.ancientgateways.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import openlyfay.ancientgateways.util.mixininterface.WorldRendererAccessHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin implements WorldRendererAccessHelper {
    @Final
    @Shadow
    private WorldRenderer worldRenderer;

    @Override
    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }
}
