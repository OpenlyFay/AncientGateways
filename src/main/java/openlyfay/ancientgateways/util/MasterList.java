package openlyfay.ancientgateways.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.*;

public class MasterList implements MasterListComponent {
    private final Map<String,AddressList> locations = new HashMap<>();

    public MasterList(){}

    private GatewayAddress getElement(String addressName){
        return getElement(addressName,0);
    }

    private GatewayAddress getElement(String addressName, int index){
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
    }

    private void addElement(GatewayAddress gatewayAddress){
        if(getElement(gatewayAddress.address) == null){
            locations.put(gatewayAddress.address, new AddressList(gatewayAddress));
        }
        else {
            locations.get(gatewayAddress.address).addAddress(gatewayAddress);
        }
    }

    @Override
    public boolean doesElementExist(String address){
        return getElement(address) != null;
    }

    @Override
    public int getIndex(String address, Vec3d position, RegistryKey<World> world){
        GatewayAddress gatewayAddress = new GatewayAddress(address,position,world);
        return locations.get(address).getIndex(gatewayAddress);
    }

    @Override
    public int getAddressLength(String address){
        return locations.get(address).getLength();
    }

    @Override
    public Vec3d getPosition(String address,int index){
        if(getElement(address) != null){
            return getElement(address, index).getPosition();
        }
        else return null;
    }

    @Override
    public Vec3d getPosition(String address){
        return getPosition(address,0);
    }

    @Override
    public RegistryKey<World> getWorld(String address){
        return getWorld(address,0);
    }

    @Override
    public RegistryKey<World> getWorld(String address, int index){
        if(getElement(address) != null){
            return getElement(address, index).getWorld();
        }
        else return null;
    }

    @Override
    public void removeElement(String address){
        locations.get(address).removeAddress(0);
        if (locations.get(address).getLength() == 0){
            locations.remove(address);
        }
    }

    @Override
    public void removeElement(String address, Vec3d pos, RegistryKey<World> world){
        GatewayAddress address1 = new GatewayAddress(address, pos, world);
        if (locations.get(address) != null){
            locations.get(address).removeAddress(address1);
            if (locations.get(address).getLength() == 0){
                locations.remove(address);
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        NbtList list = nbt.getList("gateways", 10);

        for(int i = 0; i < list.size(); ++i) {
            NbtCompound compound = list.getCompound(i);
            addElement(new GatewayAddress(compound));
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (String location : locations.keySet()) {
            AddressList addressList = locations.get(location);
            for (int i = 0; i < addressList.getLength();i++){
                NbtCompound compound = new NbtCompound();
                addressList.getAddress(i).toTag(compound);
                list.add(compound);
            }
        }

        if (!list.isEmpty()) {
            nbt.put("gateways", list);
        }
    }


    private class AddressList {
        private final ArrayList<GatewayAddress> addresses = new ArrayList<>();

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

        public GatewayAddress(NbtCompound nbt){

            this.address = nbt.getString("address");
            this.position = new Vec3d(nbt.getDouble("posX"),nbt.getDouble("posY"),nbt.getDouble("posZ"));
            this.world = RegistryKey.of(Registry.WORLD_KEY, new Identifier(nbt.getString("world")));

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

        public GatewayAddress fromTag(NbtCompound nbt){
            return new GatewayAddress(nbt);
        }

        public NbtCompound toTag(NbtCompound nbt){
            nbt.putString("address", address);
            nbt.putDouble("posX",position.x);
            nbt.putDouble("posY",position.y);
            nbt.putDouble("posZ",position.z);
            nbt.putString("world",world.getValue().toString());
            return nbt;
        }

    }

}

