package openlyfay.ancientgateways.mixins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import openlyfay.ancientgateways.util.TeleportPatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "createEndSpawnPlatform", at = @At("HEAD"),cancellable = true,allow = 1)
    private void overrideEndSpawnPlatform(ServerWorld world, BlockPos centerPos, CallbackInfo ci){
        if(TeleportPatch.getInstance().getTeleportTarget() != null){
            ci.cancel();
        }
    }
}
