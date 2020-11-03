package openlyfay.ancientgateways.mixins;

import openlyfay.ancientgateways.maths.TeleportPatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;

/**
 * Mostly from Applied Energistics 2, used with permission
 */
@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true, allow = 1)
    public void getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cri) {
        // Check if a destination has been set for the entity currently being teleported
        //if (destination.getRegistryKey() == SpatialStorageDimensionIds.WORLD_ID) {
            TeleportTarget target = TeleportPatch.getInstance().getTeleportTarget();
            //remember to check for NBT tags on entities
            if (target != null) {
                cri.setReturnValue(target);
            }
        //}
    }

}
