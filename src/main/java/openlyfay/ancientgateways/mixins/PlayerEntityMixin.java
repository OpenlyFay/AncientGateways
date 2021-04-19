package openlyfay.ancientgateways.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {


    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickRiding", at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/player/PlayerEntity;stopRiding()V"), cancellable = true)
    public void overridePlayerDismount(CallbackInfo ci){
        if (world.isClient){
            ci.cancel();
        }
    }
}
