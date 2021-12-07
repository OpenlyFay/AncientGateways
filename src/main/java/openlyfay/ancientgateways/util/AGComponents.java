package openlyfay.ancientgateways.util;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import openlyfay.ancientgateways.AncientGateways;

public class AGComponents implements LevelComponentInitializer {

    public static final ComponentKey<MasterListComponent> MASTERLIST = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(AncientGateways.MOD_ID,"masterlist"),MasterListComponent.class);

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(MASTERLIST, worldProperties -> new MasterList());
    }

    public static void addElement(String address, Vec3d position, RegistryKey<World> world, ServerWorld serverWorld){
        MASTERLIST.get(serverWorld).addElement(address, position, world);
    }

    public static boolean doesElementExist(String address, ServerWorld serverWorld){
        return MASTERLIST.get(serverWorld).doesElementExist(address);
    }

    public static int getIndex(String address, Vec3d position, RegistryKey<World> world, ServerWorld serverWorld){
        return MASTERLIST.get(serverWorld).getIndex(address,position,world);
    }

    public static int getAddressLength(String address, ServerWorld serverWorld){
        return MASTERLIST.get(serverWorld).getAddressLength(address);
    }

    public static Vec3d getPosition(String address, int index, ServerWorld serverWorld){
        return MASTERLIST.get(serverWorld).getPosition(address, index);
    }

    public static Vec3d getPosition(String address, ServerWorld serverWorld){
        return MASTERLIST.get(serverWorld).getPosition(address);
    }

    public static RegistryKey<World> getWorld(String address, ServerWorld serverWorld){
        return MASTERLIST.get(serverWorld).getWorld(address);
    }


    public static RegistryKey<World> getWorld(String address, int index, ServerWorld serverWorld){
        return MASTERLIST.get(serverWorld).getWorld(address, index);
    }

    public static void removeElement(String address, ServerWorld serverWorld){
        MASTERLIST.get(serverWorld).removeElement(address);
    }

    public static void removeElement(String address, Vec3d pos, RegistryKey<World> world, ServerWorld serverWorld){
        MASTERLIST.get(serverWorld).removeElement(address,pos,world);
    }
}
