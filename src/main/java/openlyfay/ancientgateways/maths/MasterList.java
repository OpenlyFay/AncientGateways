package openlyfay.ancientgateways.maths;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.*;

public class MasterList extends PersistentState {
    private static ArrayList<GatewayAddress> locations = new ArrayList<>();
    private final static String key = "ancientgateways_masterlist";

    public MasterList(){
        super(key);
    }

    public static MasterList get(World world){
        ServerWorld serverWorld = (ServerWorld) world;
        return serverWorld.getPersistentStateManager().getOrCreate(MasterList::new, key);
    }

    private static GatewayAddress getElement(String addressName){
        if (locations.isEmpty()){
            return null;
        }
        for (GatewayAddress gatewayAddress : locations) {
            if (gatewayAddress.getAddress().equals(addressName)) {
                return gatewayAddress;
            }
        }
        return null;
    }

    public void addElement(String address, Vec3d position, RegistryKey<World> world){
        if(getElement(address) == null){
            GatewayAddress gatewayAddress = new GatewayAddress(address, position, world);
            locations.add(gatewayAddress);
            markDirty();
        }
    }

    public static boolean doesElementExist(String address){
        return getElement(address) != null;
    }

    public static Vec3d getPosition(String address){
        if(getElement(address) != null){
            return getElement(address).getPosition();
        }
        else return null;
    }

    public static RegistryKey<World> getWorld(String address){
        if(getElement(address) != null){
            return getElement(address).getWorld();
        }
        else return null;
    }

    public void removeElement(String address){
        locations.remove(getElement(address));
        markDirty();
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag listTag = tag.getList("gateways", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            locations.add(new GatewayAddress(compoundTag));

        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag listTag = new ListTag();

        for (GatewayAddress location : locations) {
            CompoundTag compoundTag = new CompoundTag();
            location.toTag(compoundTag);
            listTag.add(compoundTag);
        }

        if (!listTag.isEmpty()) {
            tag.put("gateways", listTag);
        }

        return tag;
    }


    private class GatewayAddress {

        private final String address;
        private final Vec3d position;
        private final RegistryKey<World> world;

        public GatewayAddress(String newAddress, Vec3d newPosition, RegistryKey<World> newWorld){
            address = newAddress;
            position = newPosition;
            world = newWorld;
        }

        public GatewayAddress(CompoundTag tag){

            this.address = tag.getString("address");
            this.position = new Vec3d(tag.getDouble("posX"),tag.getDouble("posY"),tag.getDouble("posZ"));
            this.world = RegistryKey.of(Registry.DIMENSION, new Identifier(tag.getString("world")));

        }

        public RegistryKey<World> getWorld() {
            return world;
        }

        public String getAddress() {
            return address;
        }

        public Vec3d getPosition() {
            return position;
        }

        public GatewayAddress fromTag(CompoundTag tag){
            return new GatewayAddress(tag);
        }

        public CompoundTag toTag(CompoundTag tag){
            tag.putString("address", address);
            tag.putDouble("posX",position.x);
            tag.putDouble("posY",position.y);
            tag.putDouble("posZ",position.z);
            tag.putString("world",world.getValue().toString());
            return tag;
        }

    }

}

