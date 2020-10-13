package openlyfay.ancientgateways.maths;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.List;

public class MasterList extends PersistentState {
    private List<GatewayAddress> locations;
    private final static String key = "ancientgateways_masterlist";

    public MasterList(){
        super(key);
    }

    public static MasterList get(World world){
        ServerWorld serverWorld = (ServerWorld) world;
        return serverWorld.getPersistentStateManager().getOrCreate(MasterList::new, key);
    }

    private GatewayAddress getElement(String addressName){
        for (GatewayAddress gatewayAddress : locations) {
            if (gatewayAddress.getAddress().equals(addressName)) {
                return gatewayAddress;
            }
        }
        return null;
    }

    public void addElement(String address, Vec3d position, RegistryKey<World> world){
        if(getElement(address) == null){
            locations.add(new GatewayAddress(address, position, world));
        }
    }

    public Vec3d getPosition(String address){
        if(getElement(address) != null){
            return getElement(address).getPosition();
        }
        else return null;
    }

    public RegistryKey<World> getWorld(String address){
        if(getElement(address) != null){
            return getElement(address).getWorld();
        }
        else return null;
    }

    public void removeElement(String address){
        locations.remove(getElement(address));
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

        for(int i = 0; i < locations.size(); ++i) {
            CompoundTag compoundTag = new CompoundTag();
            locations.get(i).toTag(compoundTag);
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

        private GatewayAddress(String newAddress, Vec3d newPosition, RegistryKey<World> newWorld){
            address = newAddress;
            position = newPosition;
            world = newWorld;
        }

        private GatewayAddress(CompoundTag tag){
            this.address = tag.getString("address");
            this.position = new Vec3d(tag.getDouble("posX"),tag.getDouble("posY"),tag.getDouble("posZ"));
            this.world = RegistryKey.of(Registry.DIMENSION, new Identifier(tag.getString("world")));
        }

        private RegistryKey<World> getWorld() {
            return world;
        }

        private String getAddress() {
            return address;
        }

        private Vec3d getPosition() {
            return position;
        }

        private GatewayAddress fromTag(CompoundTag tag){
            return new GatewayAddress(tag);
        }

        private CompoundTag toTag(CompoundTag tag){
            tag.putString("address", address);
            tag.putDouble("posX",position.x);
            tag.putDouble("posY",position.y);
            tag.putDouble("posZ",position.z);
            tag.putString("world",world.toString());
            return tag;
        }

    }
}

