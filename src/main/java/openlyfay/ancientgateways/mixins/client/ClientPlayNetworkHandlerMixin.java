/* Adapted from Origins' mixin of the same name, used under MIT License */

package openlyfay.ancientgateways.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Vec3d;
import openlyfay.ancientgateways.entity.ChorusInkBottleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static openlyfay.ancientgateways.entity.RegisterEntity.CHORUS_INK_ENTITY;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;

    @Inject(
            method = "onEntitySpawn", at = @At("TAIL")
    )
    private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        EntityType<?> type = packet.getEntityTypeId();
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();
        Entity entity = null;
        if (type == CHORUS_INK_ENTITY) {
            entity = new ChorusInkBottleEntity(this.world, x, y, z);
        }
        if (entity != null) {
            int i = packet.getId();
            entity.setVelocity(Vec3d.ZERO);
            entity.updatePosition(x, y, z);
            entity.updateTrackedPosition(x, y, z);
            entity.setPitch((float) (packet.getPitch() * 360) / 256.0F);
            entity.setYaw((float) (packet.getYaw() * 360) / 256.0F);
            entity.setId(i);
            entity.setUuid(packet.getUuid());
            this.world.addEntity(i, entity);
            ci.cancel();
        }
    }
}