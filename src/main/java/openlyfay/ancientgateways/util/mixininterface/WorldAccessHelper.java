package openlyfay.ancientgateways.util.mixininterface;

import net.minecraft.world.World;

public interface WorldAccessHelper {

    default World getWorld(){
        return null;
    }

}
