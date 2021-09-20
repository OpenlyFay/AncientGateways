package openlyfay.ancientgateways.util;

import net.minecraft.client.render.WorldRenderer;

public interface WorldRendererAccessHelper {

    default WorldRenderer getWorldRenderer() {
        return null;
    }

}
