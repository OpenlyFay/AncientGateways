package openlyfay.ancientgateways;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import openlyfay.ancientgateways.maths.MasterList;

import javax.swing.plaf.nimbus.State;
import java.util.function.Supplier;

public class StateConfig extends PersistentState {


    private final static String key = "ancientgateways_config";

    private static int delay = 80;

    public StateConfig() {
        super(key);
    }

    public static StateConfig get(World world){
        ServerWorld serverWorld = (ServerWorld) world;
        return serverWorld.getPersistentStateManager().getOrCreate(StateConfig::new, key);
    }

    public int getDelay(){
        return delay;
    }

    public void setDelay(int delay) { StateConfig.delay = delay; markDirty(); }

    @Override
    public void fromTag(CompoundTag tag) {
        delay = tag.getInt("portal-close-delay");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("portal-close-delay", delay);
        return tag;
    }
}
