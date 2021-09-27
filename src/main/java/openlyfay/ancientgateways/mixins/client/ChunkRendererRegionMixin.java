package openlyfay.ancientgateways.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.world.World;
import openlyfay.ancientgateways.util.mixininterface.WorldAccessHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(ChunkRendererRegion.class)
public class ChunkRendererRegionMixin implements WorldAccessHelper {
    @Final
    @Shadow
    protected World world;

    @Override
    public World getWorld() {
        return world;
    }
}
