package openlyfay.ancientgateways.mixins.common;

import net.minecraft.world.GameRules;
import openlyfay.ancientgateways.util.mixininterface.RuleAccessHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRules.IntRule.class)
public class IntRuleMixin implements RuleAccessHelper {
    @Shadow
    private int value;


    public void setValue(String value) {
        this.value = Integer.parseInt(value);
    }
}
