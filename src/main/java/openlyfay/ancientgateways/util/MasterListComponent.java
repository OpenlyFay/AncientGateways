package openlyfay.ancientgateways.util;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public interface MasterListComponent extends Component {
    void addElement(String address, Vec3d position, RegistryKey<World> world);
    boolean doesElementExist(String address);
    int getIndex(String address, Vec3d position, RegistryKey<World> world);
    int getAddressLength(String address);
    Vec3d getPosition(String address,int index);
    Vec3d getPosition(String address);
    RegistryKey<World> getWorld(String address);
    RegistryKey<World> getWorld(String address, int index);
    void removeElement(String address);
    void removeElement(String address, Vec3d pos, RegistryKey<World> world);

}
