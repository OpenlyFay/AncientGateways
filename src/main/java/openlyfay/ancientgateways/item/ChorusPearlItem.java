package openlyfay.ancientgateways.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import openlyfay.ancientgateways.entity.ChorusPearlEntity;

public class ChorusPearlItem extends Item {
    public ChorusPearlItem(Settings settings){ super(settings);}


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        ItemStack itemStack2;
        Hand hand2 = Hand.OFF_HAND;
        if (hand == Hand.MAIN_HAND){
            itemStack2 = user.getOffHandStack();
        }
        else {
            itemStack2 = user.getMainHandStack();
            hand2 = Hand.MAIN_HAND;
        }
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            ChorusPearlEntity entity = new ChorusPearlEntity(world,user,itemStack2, hand2);
            entity.setItem(itemStack);
            entity.setProperties(user,user.pitch,user.yaw,0,1.5F,1.0F);
            world.spawnEntity(entity);
        }

        if (!user.abilities.creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
