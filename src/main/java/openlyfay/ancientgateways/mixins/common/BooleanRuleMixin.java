package openlyfay.ancientgateways.mixins.common;

import net.minecraft.world.GameRules;
import openlyfay.ancientgateways.util.mixininterface.RuleAccessHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRules.BooleanRule.class)
public class BooleanRuleMixin implements RuleAccessHelper {
    @Shadow
    private boolean value;

    public void setValue(String value) {
        this.value = Boolean.parseBoolean(value);
    }

    @Override
    public void flipValue() {
        value = !value;
    }
}
