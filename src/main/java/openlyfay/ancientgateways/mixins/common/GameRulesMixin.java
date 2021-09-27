package openlyfay.ancientgateways.mixins.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.GameRules;
import openlyfay.ancientgateways.util.mixininterface.GameRulesNBT;
import openlyfay.ancientgateways.util.mixininterface.RuleAccessHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Optional;

@Mixin(GameRules.class)
public class GameRulesMixin implements GameRulesNBT {
    @Final
    @Shadow
    private Map<GameRules.Key<?>, GameRules.Rule<?>> rules;

    @Override
    public void fromNBT(CompoundTag tag) {
        this.rules.forEach((key, rule) -> {
            Optional<String> val = Optional.ofNullable(tag.getString(key.getName()));
            val.ifPresent(((RuleAccessHelper) rule)::setValue);
        });
    }
}
