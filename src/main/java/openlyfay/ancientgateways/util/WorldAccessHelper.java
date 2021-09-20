package openlyfay.ancientgateways.util;

import net.minecraft.world.World;

public interface WorldAccessHelper {

    default World getWorld(){
        return null;
    }

}
