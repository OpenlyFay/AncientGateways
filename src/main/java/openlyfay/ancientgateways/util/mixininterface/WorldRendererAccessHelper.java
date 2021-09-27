package openlyfay.ancientgateways.util.mixininterface;

import net.minecraft.client.render.WorldRenderer;

public interface WorldRendererAccessHelper {

    default WorldRenderer getWorldRenderer() {
        return null;
    }

}
