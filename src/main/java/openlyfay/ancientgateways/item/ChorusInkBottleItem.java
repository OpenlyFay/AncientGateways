package openlyfay.ancientgateways.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import openlyfay.ancientgateways.entity.ChorusInkBottleEntity;

public class ChorusInkBottleItem extends Item {
    public ChorusInkBottleItem(Settings settings){super(settings);}

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            ChorusInkBottleEntity entity = new ChorusInkBottleEntity(world,user);
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
