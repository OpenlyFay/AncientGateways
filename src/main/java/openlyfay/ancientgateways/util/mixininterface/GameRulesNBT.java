package openlyfay.ancientgateways.util.mixininterface;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.GameRules;

public interface GameRulesNBT {
    default void fromNBT(CompoundTag tag){
    }
}
