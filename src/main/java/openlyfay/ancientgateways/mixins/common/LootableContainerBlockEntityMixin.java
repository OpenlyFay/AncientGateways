package openlyfay.ancientgateways.mixins.common;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootableContainerBlockEntity.class)
public class LootableContainerBlockEntityMixin extends BlockEntity{


    public LootableContainerBlockEntityMixin(BlockEntityType<?> type) {
        super(type);
    }

   @Inject(method = "canPlayerUse", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void chorusPearlOverride(PlayerEntity player, CallbackInfoReturnable<Boolean> cir){
            cir.setReturnValue(true);

    }
}
