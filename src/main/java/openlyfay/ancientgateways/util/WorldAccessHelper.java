package openlyfay.ancientgateways.util;

import net.minecraft.world.World;

public interface WorldAccessHelper {

    public default World getWorld(){
        return null;
    }

}
