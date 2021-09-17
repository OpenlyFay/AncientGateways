package openlyfay.ancientgateways.mixins.common;

import openlyfay.ancientgateways.util.TeleportPatch;
import openlyfay.ancientgateways.util.Teleportable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;

@Mixin(Entity.class)
public class EntityMixin implements Teleportable {
    private int portalCoolDown = 0;

    @Override
    public void setPortalCoolDown(int portalCD){
        portalCoolDown = portalCD;
    }

    @Override
    public int getPortalCoolDown(){
        return portalCoolDown;
    }


    @Inject(method = "baseTick", at = @At("TAIL"))
    public void teleportCoolDownTick(CallbackInfo ci){
        if (portalCoolDown > 0){
            portalCoolDown--;
        }
    }

    /**
     * From Applied Energistics 2, used with permission
     */
    @Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true, allow = 1)
    public void getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cri) {
        // Check if a destination has been set for the entity currently being teleported
        TeleportTarget target = TeleportPatch.getInstance().getTeleportTarget();
        if (target != null) {
            cri.setReturnValue(target);
        }
    }


}
