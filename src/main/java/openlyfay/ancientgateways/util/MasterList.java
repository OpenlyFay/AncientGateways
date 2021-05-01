package openlyfay.ancientgateways.util;

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
    private static Map<String,AddressList> locations = new HashMap<>();
    private final static String key = "ancientgateways_masterlist";

    public MasterList(){
        super(key);
    }

    public static MasterList get(World world){
        ServerWorld serverWorld = (ServerWorld) world;
        return serverWorld.getPersistentStateManager().getOrCreate(MasterList::new, key);
    }

    private static GatewayAddress getElement(String addressName){
        return getElement(addressName,0);
    }

    private static GatewayAddress getElement(String addressName, int index){
        if (locations.get(addressName) != null){
            return locations.get(addressName).getAddress(index);
        }
        else {
            return null;
        }
    }

    public void addElement(String address, Vec3d position, RegistryKey<World> world){
        if(getElement(address) == null){
            locations.put(address,new AddressList(new GatewayAddress(address,position,world)));
        }
        else {
            locations.get(address).addAddress(new GatewayAddress(address,position,world));
        }
        markDirty();
    }

    private void addElement(GatewayAddress gatewayAddress){
        if(getElement(gatewayAddress.address) == null){
            locations.put(gatewayAddress.address, new AddressList(gatewayAddress));
        }
        else {
            locations.get(gatewayAddress.address).addAddress(gatewayAddress);
        }
        markDirty();
    }

    public static boolean doesElementExist(String address){
        return getElement(address) != null;
    }

    public int getIndex(String address, Vec3d position, RegistryKey<World> world){
        GatewayAddress gatewayAddress = new GatewayAddress(address,position,world);
        return locations.get(address).getIndex(gatewayAddress);
    }

    public int getAddressLength(String address){
        return locations.get(address).getLength();
    }

    public static Vec3d getPosition(String address,int index){
        if(getElement(address) != null){
            return getElement(address, index).getPosition();
        }
        else return null;
    }

    public static Vec3d getPosition(String address){
        return getPosition(address,0);
    }

    public static RegistryKey<World> getWorld(String address){
        return getWorld(address,0);
    }

    public static RegistryKey<World> getWorld(String address, int index){
        if(getElement(address) != null){
            return getElement(address, index).getWorld();
        }
        else return null;
    }

    public void removeElement(String address){
        locations.get(address).removeAddress(0);
        if (locations.get(address).getLength() == 0){
            locations.remove(address);
        }
        markDirty();
    }

    public void removeElement(String address, Vec3d pos, RegistryKey<World> world){
        GatewayAddress address1 = new GatewayAddress(address, pos, world);
        if (locations.get(address) != null){
            locations.get(address).removeAddress(address1);
            if (locations.get(address).getLength() == 0){
                locations.remove(address);
            }
            markDirty();
        }
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag listTag = tag.getList("gateways", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            addElement(new GatewayAddress(compoundTag));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag listTag = new ListTag();

        for (String location : locations.keySet()) {
            AddressList addressList = locations.get(location);
            for (int i = 0; i < addressList.getLength();i++){
                CompoundTag compoundTag = new CompoundTag();
                addressList.getAddress(i).toTag(compoundTag);
                listTag.add(compoundTag);
            }
        }

        if (!listTag.isEmpty()) {
            tag.put("gateways", listTag);
        }

        return tag;
    }


    private class AddressList {
        private ArrayList<GatewayAddress> addresses = new ArrayList<>();

        public AddressList(GatewayAddress newAddress){
            addresses.add(newAddress);
        }

        public void addAddress(GatewayAddress address){
            if (getIndex(address) == -1){
                addresses.add(address);
            }
        }

        public void removeAddress(int index) {
            addresses.remove(index);
        }

        public void removeAddress(GatewayAddress gatewayAddress){
            int index = getIndex(gatewayAddress);
            if (index != -1){
                addresses.remove(index);
            }
        }

        public GatewayAddress getAddress(int index){
            return addresses.get(index);
        }

        public int getIndex(GatewayAddress address){
            for (int i = 0; i < addresses.size();i++){
                GatewayAddress address1 = addresses.get(i);
                if (address.equals(address1)){
                    return i;
                }
            }

            return -1;
        }

        public int getLength(){
            return addresses.size();
        }
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

        public boolean equals(GatewayAddress gatewayAddress){
            if (world == gatewayAddress.getWorld() && position.x == gatewayAddress.getPosition().x && position.y == gatewayAddress.getPosition().y && position.z == gatewayAddress.getPosition().z && address.equals(gatewayAddress.getAddress())){
                return true;
            }
            return false;
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

