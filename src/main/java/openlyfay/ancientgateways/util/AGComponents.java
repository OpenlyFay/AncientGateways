package openlyfay.ancientgateways.util;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import openlyfay.ancientgateways.AncientGateways;

public class AGComponents implements LevelComponentInitializer {

    public static final ComponentKey<MasterListComponent> MASTERLIST = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(AncientGateways.MOD_ID,"masterlist"),MasterListComponent.class);

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(MASTERLIST, worldProperties -> new MasterList());
    }

    public static void addElement(String address, Vec3d position, RegistryKey<World> world, World serverWorld){
        MASTERLIST.get(serverWorld.getLevelProperties()).addElement(address, position, world);
    }

    public static boolean doesElementExist(String address, World serverWorld){
        return MASTERLIST.get(serverWorld.getLevelProperties()).doesElementExist(address);
    }

    public static int getIndex(String address, Vec3d position, RegistryKey<World> world, World serverWorld){
        return MASTERLIST.get(serverWorld.getLevelProperties()).getIndex(address,position,world);
    }

    public static int getAddressLength(String address, World serverWorld){
        return MASTERLIST.get(serverWorld.getLevelProperties()).getAddressLength(address);
    }

    public static Vec3d getPosition(String address, int index, World serverWorld){
        return MASTERLIST.get(serverWorld.getLevelProperties()).getPosition(address, index);
    }

    public static Vec3d getPosition(String address, World serverWorld){
        return MASTERLIST.get(serverWorld.getLevelProperties()).getPosition(address);
    }

    public static RegistryKey<World> getWorld(String address, World serverWorld){
        return MASTERLIST.get(serverWorld.getLevelProperties()).getWorld(address);
    }


    public static RegistryKey<World> getWorld(String address, int index, World serverWorld){
        return MASTERLIST.get(serverWorld.getLevelProperties()).getWorld(address, index);
    }

    public static void removeElement(String address, World serverWorld){
        MASTERLIST.get(serverWorld.getLevelProperties()).removeElement(address);
    }

    public static void removeElement(String address, Vec3d pos, RegistryKey<World> world, World serverWorld){
        MASTERLIST.get(serverWorld.getLevelProperties()).removeElement(address,pos,world);
    }
}
